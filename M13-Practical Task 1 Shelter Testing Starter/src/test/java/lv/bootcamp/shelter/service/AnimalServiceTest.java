package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

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

    @Captor
    ArgumentCaptor<List<Long>> idsCaptor;

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
        Animal animal = new Animal(1L, "Rex", AnimalType.DOG, "Husky", 4, "Good boy", AnimalStatus.AVAILABLE);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(animalRepository.save(animal)).thenAnswer(invocation -> invocation.getArgument(0));
        AnimalResponse response = animalService.adopt(new AdoptionRequest(1L, "John", "john@example.com"));
        assertThat(response.status()).isEqualTo(AnimalStatus.ADOPTED);
        verify(notificationClient).sendAdoptionNotification(1L, "Rex", "john@example.com");
    }

    @Test
    void adopt_shouldThrowWhenAnimalAlreadyAdopted() {
        Animal animal = new Animal(1L, "Rex", AnimalType.DOG, "Husky", 4, "Good boy", AnimalStatus.ADOPTED);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        assertThrows(IllegalStateException.class, () -> animalService.adopt(new AdoptionRequest(1L, "John", "john@example.cpm")));
        verifyNoInteractions(notificationClient);
    }

    @Test
    void reserveMultiple_shouldNotifyWithReservedIds() {
        List<Animal> animals = List.of(
                new Animal(1L, "Rex", AnimalType.DOG, "Husky", 4, "Good boy", AnimalStatus.AVAILABLE),
                new Animal(2L, "Mia", AnimalType.CAT, "Siamese", 3, "Playful cat", AnimalStatus.AVAILABLE)
        );
        when(animalRepository.findAllById(List.of(1L, 2L))).thenReturn(animals);
        when(animalRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        List<AnimalResponse> responses = animalService.reserveMultiple(List.of(1L, 2L));
        responses.forEach(response -> assertThat(response.status()).isEqualTo(AnimalStatus.RESERVED));
        verify(notificationClient).sendBulkStatusNotification(idsCaptor.capture(), eq(String.valueOf(AnimalStatus.RESERVED)));
        assertThat(idsCaptor.getValue()).containsExactly(1L, 2L);
    }
}
