package lv.bootcamp.shelter;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

// TODO: add imports as you write the test (e.g. assertThat, verify)

/**
 * Task: Integration test with @SpringBootTest.
 *
 * The full application context loads — use @MockitoBean only for the external
 * NotificationClient. Everything else (service, repository, JPA) is real.
 * @Transactional rolls back after each test.
 */
@SpringBootTest
@Transactional
class AdoptionIntegrationTest {

    @Autowired
    private AnimalService animalService;

    @MockitoBean
    private NotificationClient notificationClient;

    @Test
    void adoptionFlow_shouldPersistStatusAndNotifyExternalSystem() {
        AnimalResponse newAnimal = animalService.create(new AnimalCreateRequest("Rex", AnimalType.DOG, "Husky", 4, "Good boy"));
        assertThat(newAnimal.status()).isEqualTo(AnimalStatus.AVAILABLE);
        newAnimal = animalService.adopt(new AdoptionRequest(newAnimal.id(), "John", "john@example.com"));
        assertThat(newAnimal.status()).isEqualTo(AnimalStatus.ADOPTED);
        verify(notificationClient).sendAdoptionNotification(newAnimal.id(), newAnimal.name(), "john@example.com");
        AnimalResponse fetchedAnimal = animalService.findById(newAnimal.id());
        assertThat(fetchedAnimal.status()).isEqualTo(AnimalStatus.ADOPTED);
    }
}
