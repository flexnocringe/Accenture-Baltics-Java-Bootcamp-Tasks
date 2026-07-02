package lv.bootcamp.shelter.task6;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Task 7: Mocking a dependency
 * <p>
 * Practice:
 * - @Mock and @InjectMocks
 * - when(...).thenReturn(...)
 * - verify(...) and verify(..., never())
 * - Testing with mocked dependencies
 * <p>
 * Instructions:
 * Write tests for IntakeService. The AnimalRepository is mocked — you control what it returns.
 * Focus on verifying that IntakeService calls the repository correctly.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IntakeService")
class IntakeServiceTest {

    @Mock
    private AnimalRepository repository;

    @InjectMocks
    private IntakeService service;

    private final Animal buddy = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));

    // ==================== intake() ====================

    @Nested
    @DisplayName("intake")
    class Intake {

        @Test
        @DisplayName("saves valid animal and returns it")
        void shouldSaveValidAnimal() {
            // TODO: Stub repository.save(buddy) to return buddy
            //   Hint: when(repository.save(buddy)).thenReturn(buddy);
            // TODO: Call service.intake(buddy)
            // TODO: Assert the returned animal has name "Buddy"
            // TODO: Verify that repository.save(buddy) was called exactly once
            when(repository.save(buddy)).thenReturn(buddy);
            assertEquals("Buddy", service.intake(buddy).getName());
            verify(repository, times(1)).save(buddy);
        }

        @Test
        @DisplayName("throws for null animal without calling repository")
        void shouldThrowForNullAnimal() {
            // TODO: Call service.intake(null)
            // TODO: Assert it throws NullPointerException
            // TODO: Verify that repository.save(any()) was NEVER called
            assertThrows(NullPointerException.class, () -> service.intake(null));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("throws for invalid animal without calling repository")
        void shouldThrowForInvalidAnimal() {
            // TODO: Create an Animal with blank name
            //   Animal invalid = new Animal("", "Dog", 3, true, LocalDate.now());
            // TODO: Assert that service.intake(invalid) throws IllegalArgumentException
            // TODO: Verify that repository.save(any()) was NEVER called
            Animal invalid = new Animal("", "Dog", 3, true, LocalDate.of(2026, 1, 15));
            assertThrows(IllegalArgumentException.class, () -> service.intake(invalid));
            verify(repository, never()).save(any());
        }
    }

    // ==================== findByName() ====================

    @Nested
    @DisplayName("findByName")
    class FindByName {

        @Test
        @DisplayName("returns animal when repository finds it")
        void shouldReturnAnimalWhenFound() {
            // TODO: Stub repository.findByName("Buddy") to return Optional.of(buddy)
            // TODO: Call service.findByName("Buddy")
            // TODO: Assert result is not null and name equals "Buddy"
            when(repository.findByName("Buddy")).thenReturn(Optional.of(buddy));
            Animal found = service.findByName("Buddy");
            assertNotNull(found);
            assertEquals("Buddy", found.getName());
        }

        @Test
        @DisplayName("returns null when repository does not find it")
        void shouldReturnNullWhenNotFound() {
            // TODO: Stub repository.findByName("Ghost") to return Optional.empty()
            // TODO: Call service.findByName("Ghost")
            // TODO: Assert result is null
            when(repository.findByName("Ghost")).thenReturn(Optional.empty());
            assertNull(service.findByName("Ghost"));
        }

        @Test
        @DisplayName("throws for blank name without calling repository")
        void shouldThrowForBlankName() {
            // TODO: Call service.findByName("")
            // TODO: Assert it throws IllegalArgumentException
            // TODO: Verify repository.findByName(any()) was NEVER called
            assertThrows(IllegalArgumentException.class, () -> service.findByName(""));
            verify(repository, never()).findByName("Ghost");
        }
    }

    // ==================== findBySpecies() ====================

    @Nested
    @DisplayName("findBySpecies")
    class FindBySpecies {

        @Test
        @DisplayName("returns list from repository for valid species")
        void shouldReturnAnimalsForValidSpecies() {
            // TODO: Stub repository.findBySpecies("Dog") to return List.of(buddy)
            // TODO: Call service.findBySpecies("Dog")
            // TODO: Assert result has size 1 and contains buddy
            when(repository.findBySpecies("Dog")).thenReturn(List.of(buddy));
            List<Animal> animals = service.findBySpecies("Dog");
            assertEquals(1, animals.size());
            assertTrue(animals.contains(buddy));
        }

        @Test
        @DisplayName("returns empty list for blank species without calling repository")
        void shouldReturnEmptyForBlankSpecies() {
            // TODO: Call service.findBySpecies("")
            // TODO: Assert result is empty
            // TODO: Verify repository.findBySpecies(any()) was NEVER called
            assertTrue(service.findBySpecies("").isEmpty());
            verify(repository, never()).findBySpecies(any());
        }

        @Test
        @DisplayName("returns empty list for null species without calling repository")
        void shouldReturnEmptyForNullSpecies() {
            // TODO: Call service.findBySpecies(null)
            // TODO: Assert result is empty
            // TODO: Verify repository.findBySpecies(any()) was NEVER called
            assertTrue(service.findBySpecies(null).isEmpty());
            verify(repository, never()).findBySpecies(any());
        }
    }

    // ==================== count() ====================

    @Nested
    @DisplayName("count")
    class Count {

        @Test
        @DisplayName("returns the size of all animals from repository")
        void shouldReturnCountFromRepository() {
            // TODO: Stub repository.findAll() to return a list of 3 animals
            // TODO: Call service.count()
            // TODO: Assert result equals 3
            when(repository.findAll()).thenReturn(List.of(buddy, buddy, buddy));
            assertEquals(3, service.count());
        }

        @Test
        @DisplayName("returns 0 when repository is empty")
        void shouldReturnZeroWhenEmpty() {
            // TODO: Stub repository.findAll() to return List.of()
            // TODO: Call service.count()
            // TODO: Assert result equals 0
            when(repository.findAll()).thenReturn(List.of());
            assertEquals(0, service.count());
        }
    }
}
