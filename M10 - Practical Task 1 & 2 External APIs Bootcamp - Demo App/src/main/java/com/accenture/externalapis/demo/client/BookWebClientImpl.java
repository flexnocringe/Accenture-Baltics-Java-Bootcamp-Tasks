package com.accenture.externalapis.demo.client;

import com.accenture.externalapis.demo.config.ExternalServiceProperties;
import com.accenture.externalapis.demo.dto.BookApiResponse;
import com.accenture.externalapis.demo.dto.BookDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BookWebClientImpl implements BookWebClient {

    private WebClient webClient;

    public BookWebClientImpl(WebClient.Builder builder, ExternalServiceProperties properties) {
        this.webClient = builder.baseUrl(properties.baseUrl()).build();
    }

    @Override
    public Mono<BookDto> getBookAsync(Long id) {
        return webClient.get()
                .uri("/books/{id}", id)
                .retrieve()
                .bodyToMono(BookApiResponse.class)
                .mapNotNull(response -> new BookDto(response.title(), response.author(), response.genre(), response.price(), response.publishedYear()))
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.error(new ClientException("Book not found. id: " + id)))
                .onErrorResume(WebClientResponseException.BadRequest.class, ex -> Mono.error(new ClientException("Bad request. Make sure number is passed as id", ex)))
                .onErrorResume(WebClientResponseException.BadGateway.class, ex -> Mono.error(new ClientException("Bad Getaway.", ex)))
                .onErrorResume(WebClientResponseException.class, ex -> Mono.error(new ClientException("Client error: " + ex.getStatusCode(), ex)))
                .onErrorResume(WebClientRequestException.class, ex -> Mono.error(new ClientException("External service is unreachable", ex)));
    }

    @Override
    public Flux<BookDto> getAllBooksAsync() {
        return webClient.get()
                .uri("/books")
                .retrieve()
                .bodyToFlux(BookApiResponse.class)
                .mapNotNull(response -> new BookDto(response.title(), response.author(), response.genre(), response.price(), response.publishedYear()))
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Flux.error(new ClientException("Books not found.")))
                .onErrorResume(WebClientResponseException.BadRequest.class, ex -> Flux.error(new ClientException("Bad request. Make sure number is passed as id", ex)))
                .onErrorResume(WebClientResponseException.BadGateway.class, ex -> Mono.error(new ClientException("Bad Getaway.", ex)))
                .onErrorResume(WebClientResponseException.class, ex -> Flux.error(new ClientException("Client error: " + ex.getStatusCode(), ex)))
                .onErrorResume(WebClientRequestException.class, ex -> Flux.error(new ClientException("External service is unreachable", ex)));
    }

    @Override
    public Mono<List<BookDto>> getBooksInParallel(Long id1, Long id2) {
        Mono<BookDto> book1Mono = getBookAsync(id1);
        Mono<BookDto> book2Mono = getBookAsync(id2);

        return Mono.zip(book1Mono, book2Mono)
                .mapNotNull(tuple -> List.of(tuple.getT1(), tuple.getT2()))
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.error(new ClientException("One or two books not found. ids: " + id1 + ", " + id2)))
                .onErrorResume(WebClientResponseException.BadRequest.class, ex -> Mono.error(new ClientException("Bad request. Make sure number is passed as id", ex)))
                .onErrorResume(WebClientResponseException.BadGateway.class, ex -> Mono.error(new ClientException("Bad Getaway.", ex)))
                .onErrorResume(WebClientResponseException.class, ex -> Mono.error(new ClientException("Client error: " + ex.getStatusCode(), ex)))
                .onErrorResume(WebClientRequestException.class, ex -> Mono.error(new ClientException("External service is unreachable", ex)));
    }
}
