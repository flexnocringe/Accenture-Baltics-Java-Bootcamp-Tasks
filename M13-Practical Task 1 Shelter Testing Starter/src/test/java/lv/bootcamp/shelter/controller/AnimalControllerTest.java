package lv.bootcamp.shelter.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalNotFoundException;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task: REST controller tests with MockMvc and @WebMvcTest.
 *
 * Stub the service with @MockitoBean. Use mockMvc.perform() to make requests
 * and chain .andExpect() calls to verify status, JSON content, and error responses.
 */
@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnimalService animalService;

    @Test
    @WithMockUser
    void findAll_shouldReturnListOfAnimals() throws Exception {
        when(animalService.findAll()).thenReturn(List.of(
                new AnimalResponse(1L, "Rex", AnimalType.DOG, "Husky", 4, "Description", AnimalStatus.AVAILABLE),
                new AnimalResponse(2L, "Whiskers", AnimalType.CAT, "Siamese", 6, "Description", AnimalStatus.ADOPTED)
        ));
        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[1].name").value("Whiskers"));
    }

    @Test
    @WithMockUser
    void findById_shouldReturn404WhenNotFound() throws Exception {
        when(animalService.findById(99L)).thenThrow(new AnimalNotFoundException(99L));
        mockMvc.perform(get("/api/animals/99")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_shouldReturn201WithCreatedAnimal() throws Exception {
        // TODO:
        // 1. Stub animalService.create(any()) to return an AnimalResponse with id=1, name="Rex", status=AVAILABLE
        // 2. POST /api/animals with a valid AnimalCreateRequest JSON body
        // 3. Assert status 201 and that the response JSON contains id, name, and status
        AnimalResponse animalResponse = new AnimalResponse(1L, "Rex", AnimalType.DOG, "Husky", 4, "Description", AnimalStatus.AVAILABLE);
        when(animalService.create(any())).thenReturn(animalResponse);
        mockMvc.perform(post("/api/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalResponse)).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    @WithMockUser
    void create_shouldReturn400WhenNameIsBlank() throws Exception {
        AnimalResponse animalResponse = new AnimalResponse(1L, "", AnimalType.DOG, "Husky", 4, "Description", AnimalStatus.AVAILABLE);
        mockMvc.perform(post("/api/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalResponse)).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void create_shouldReturn400WhenTypeIsNull() throws Exception {
        AnimalResponse animalResponse = new AnimalResponse(1L, "Rex", null, "Husky", 4, "Description", AnimalStatus.AVAILABLE);
        mockMvc.perform(post("/api/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalResponse)).with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
