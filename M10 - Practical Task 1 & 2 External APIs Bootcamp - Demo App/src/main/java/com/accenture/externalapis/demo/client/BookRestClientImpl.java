package com.accenture.externalapis.demo.client;

import com.accenture.externalapis.demo.config.ExternalServiceProperties;
import com.accenture.externalapis.demo.dto.BookApiResponse;
import com.accenture.externalapis.demo.dto.BookDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.List;

@Component
public class BookRestClientImpl implements BookRestClient {

    private RestClient restClient;

    public BookRestClientImpl(RestClient.Builder builder, ExternalServiceProperties properties) {
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Override
    public BookDto getBook(Long id) {
        try {
            BookApiResponse response = restClient.get()
                    .uri("/books/{id}", id)
                    .retrieve()
                    .body(BookApiResponse.class);
            if (response == null) {
                throw new ClientException("Book with id: " + id + " is null");
            }
            return new BookDto(response.title(), response.author(), response.genre(), response.price(), response.publishedYear());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ClientException("Book not found. id: " + id);
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new ClientException("Bad request. Make sure number is passed as id", ex);
        } catch (HttpClientErrorException ex) {
            throw new ClientException("Client error: " + ex.getStatusCode(), ex);
        } catch (HttpServerErrorException.BadGateway ex) {
            throw new ClientException("Bad Gateway.", ex);
        } catch (HttpServerErrorException.ServiceUnavailable ex) {
            throw new ClientException("Service Unavailable. Try again later.");
        } catch (HttpServerErrorException ex) {
            throw new ClientException("Server error: " + ex.getStatusCode(), ex);
        } catch (ResourceAccessException ex) {
            throw new ClientException("External service is unreachable", ex);
        } catch (UnknownContentTypeException ex) {
            throw new ClientException("Response could not be mapped to the response object successfully", ex);
        }
    }

    @Override
    public List<BookDto> getAllBooks() {
        try {
            BookApiResponse[] responses = restClient.get()
                    .uri("/books")
                    .retrieve()
                    .body(BookApiResponse[].class);
            if (responses == null) {
                throw new ClientException("There are no Books in the system.");
            }
            return Arrays.stream(responses).toList().stream()
                    .map(response -> new BookDto(response.title(), response.author(), response.genre(), response.price(), response.publishedYear()))
                    .toList();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ClientException("Books not found.", ex);
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new ClientException("Bad request. Make sure number is passed as id", ex);
        } catch (HttpClientErrorException ex) {
            throw new ClientException("Client error: " + ex.getStatusCode(), ex);
        } catch (HttpServerErrorException.BadGateway ex) {
            throw new ClientException("Bad Gateway.", ex);
        } catch (HttpServerErrorException.ServiceUnavailable ex) {
            throw new ClientException("Service Unavailable. Try again later.");
        } catch (HttpServerErrorException ex) {
            throw new ClientException("Server error: " + ex.getStatusCode(), ex);
        } catch (ResourceAccessException ex) {
            throw new ClientException("External service is unreachable", ex);
        } catch (UnknownContentTypeException ex) {
            throw new ClientException("Response could not be mapped to the response object successfully", ex);
        }

    }
}
