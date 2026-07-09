package lv.bootcamp.shelter.controller;

import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task: View controller tests with MockMvc and @WebMvcTest.
 *
 * A @Controller returns a view name, not JSON.
 * Use view().name() and model().attribute() instead of jsonPath().
 * Use content().string(containsString(...)) to check rendered HTML.
 */
@WebMvcTest(AnimalPageController.class)
class AnimalPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalService animalService;

    @Test
    @WithMockUser
    void listAnimals_shouldRenderAnimalsView() throws Exception {
        // TODO:
        // 1. Stub animalService.findAll() to return an empty list
        // 2. GET /animals
        // 3. Assert status 200 and view name "animals"
        when(animalService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(view().name("animals"));
    }

    @Test
    @WithMockUser
    void listAnimals_shouldAddAnimalsToModel() throws Exception {
        // TODO:
        // 1. Stub animalService.findAll() to return a list with one animal (name="Rex")
        // 2. GET /animals
        // 3. Assert the model has an attribute named "animals" containing that list
        AnimalResponse animalResponse = new AnimalResponse(1L, "", AnimalType.DOG, "Husky", 4, "Description", AnimalStatus.AVAILABLE);
        when(animalService.findAll()).thenReturn(List.of(animalResponse));
        mockMvc.perform(get("/animals"))
                .andExpect(model().attributeExists("animals"));
    }

    @Test
    @WithMockUser
    void listAnimals_shouldRenderAnimalNameInHtml() throws Exception {
        // TODO:
        // 1. Stub animalService.findAll() to return a list with one animal (name="Rex")
        // 2. GET /animals
        // 3. Assert the response body (rendered HTML) contains the string "Rex"
        //    Hint: content().string(containsString("Rex"))
        AnimalResponse animalResponse = new AnimalResponse(1L, "Rex", AnimalType.DOG, "Husky", 4, "Description", AnimalStatus.AVAILABLE);
        when(animalService.findAll()).thenReturn(List.of(animalResponse));
        mockMvc.perform(get("/animals"))
                .andExpect(content().string(containsString("Rex")));
    }
}
