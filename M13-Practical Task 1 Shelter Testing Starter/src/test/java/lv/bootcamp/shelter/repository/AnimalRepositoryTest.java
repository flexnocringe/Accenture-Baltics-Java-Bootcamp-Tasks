package lv.bootcamp.shelter.repository;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Task: Repository tests with @DataJpaTest.
 *
 * Use entityManager.persist() + entityManager.flush() to set up test data.
 * Each test rolls back automatically — no cleanup needed.
 */
@DataJpaTest
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_shouldPersistAnimalAndGenerateId() {
        Animal animal = new Animal(null, "Rex", AnimalType.DOG, "Husky", 4, "Friendly dog", AnimalStatus.AVAILABLE);
        Animal savedAnimal = animalRepository.save(animal);
        assertThat(savedAnimal.getId()).isNotNull();
        assertThat(savedAnimal.getName()).isEqualTo("Rex");
    }

    @Test
    void findByStatus_shouldReturnOnlyMatchingAnimals() {
        List<Animal> animals = List.of(
                new Animal(null, "Rex", AnimalType.DOG, "Husky", 4, "Friendly dog", AnimalStatus.AVAILABLE),
                new Animal(null, "Mia", AnimalType.CAT, "Siamese", 3, "Playful cat", AnimalStatus.AVAILABLE),
                new Animal(null, "Buddy", AnimalType.DOG, "Labrador", 5, "Loyal dog", AnimalStatus.ADOPTED)
        );
        animals.forEach(animal -> {entityManager.persist(animal);});
        entityManager.flush();
        List<Animal> availableAnimals = animalRepository.findByStatus(AnimalStatus.AVAILABLE);
        assertThat(availableAnimals).hasSize(2);
        availableAnimals.forEach(animal -> {assertThat(animal.getStatus()).isEqualTo(AnimalStatus.AVAILABLE);});
    }

    @Test
    void findByType_shouldReturnAnimalsOfGivenType() {
        List<Animal> animals = List.of(
                new Animal(null, "Rex", AnimalType.DOG, "Husky", 4, "Friendly dog", AnimalStatus.AVAILABLE),
                new Animal(null, "Mia", AnimalType.CAT, "Siamese", 3, "Playful cat", AnimalStatus.AVAILABLE)
        );
        animals.forEach(animal -> {entityManager.persist(animal);});
        entityManager.flush();
        List<Animal> availableAnimals = animalRepository.findByType(AnimalType.DOG);
        assertThat(availableAnimals).hasSize(1);
        assertThat(availableAnimals.getFirst().getType()).isEqualTo(AnimalType.DOG);
    }

    @Test
    void findByNameContainingIgnoreCase_shouldMatchPartialName() {
        List<Animal> animals = List.of(
                new Animal(null, "Rex", AnimalType.DOG, "Husky", 4, "Friendly dog", AnimalStatus.AVAILABLE),
                new Animal(null, "Rexy Jr", AnimalType.CAT, "Siamese", 3, "Playful cat", AnimalStatus.AVAILABLE),
                new Animal(null, "Mia", AnimalType.DOG, "Labrador", 5, "Loyal dog", AnimalStatus.ADOPTED)
        );
        animals.forEach(animal -> {entityManager.persist(animal);});
        entityManager.flush();
        List<Animal> matchingAnimals = animalRepository.findByNameContainingIgnoreCase("rex");
        assertThat(matchingAnimals).hasSize(2);
    }
}
