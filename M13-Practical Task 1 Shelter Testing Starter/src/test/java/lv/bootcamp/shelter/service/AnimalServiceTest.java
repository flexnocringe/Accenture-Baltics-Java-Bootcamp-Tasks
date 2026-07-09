package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

// TODO: add any imports you need as you write the tests

/**
 * Task: Service-layer tests with Mockito.
 *
 * Use @Mock, @InjectMocks, stubbing, verify(), and ArgumentCaptor.
 * Write Arrange-Act-Assert for each method.
 */
@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AnimalService animalService;

    @Captor
    ArgumentCaptor<Animal> animalCaptor;

    @Test
    void create_shouldSaveAnimalWithAvailableStatus() {
        AnimalCreateRequest request = new AnimalCreateRequest("Rex", AnimalType.DOG, "Husky", 4, "Good boy");
        Animal animal = new Animal(1L, "Rex", AnimalType.DOG, "Husky", 4, "Good boy", AnimalStatus.AVAILABLE);
        when(animalRepository.save(animalCaptor.capture())).thenReturn(animal);
        AnimalResponse response = animalService.create(request);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Rex");
        assertThat(response.status()).isEqualTo(AnimalStatus.AVAILABLE);
        assertThat(animalCaptor.getValue().getStatus()).isEqualTo(AnimalStatus.AVAILABLE);
    }

    @Test
    void findById_shouldThrowWhenAnimalNotFound() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(AnimalNotFoundException.class, () -> animalService.findById(99L), contains("id"));
    }

    @Test
    void adopt_shouldChangeStatusAndSendNotification() {
        // TODO:
        // 1. Arrange: create an AVAILABLE animal, stub findById() to return it,
        //    stub save() to return the argument passed to it (hint: thenAnswer)
        // 2. Act: call animalService.adopt() with animalId=1 and email="john@example.com"
        // 3. Assert: response status is ADOPTED
        // 4. Verify: notificationClient.sendAdoptionNotification() was called
        //    with the correct animalId, name, and email

    }

    @Test
    void adopt_shouldThrowWhenAnimalAlreadyAdopted() {
        // TODO:
        // 1. Arrange: create an ADOPTED animal, stub findById() to return it
        // 2. Act & Assert: calling adopt() should throw IllegalStateException
        // 3. Verify: notificationClient had NO interactions at all
    }

    @Test
    void reserveMultiple_shouldNotifyWithReservedIds() {
        // TODO:
        // 1. Arrange: create two AVAILABLE animals (id=1, id=2),
        //    stub findAllById() to return them,
        //    stub save() to return the argument (thenAnswer)
        // 2. Act: call animalService.reserveMultiple(List.of(1L, 2L))
        // 3. Assert: both responses have status RESERVED
        // 4. Use @Captor (ArgumentCaptor<List<Long>>) to capture the list
        //    passed to notificationClient.sendBulkStatusNotification()
        //    and assert it containsExactly(1L, 2L)
    }
}
