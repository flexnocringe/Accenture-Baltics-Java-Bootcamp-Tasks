package org.example.shelter;

import org.example.model.AdoptionStatus;
import org.example.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class Shelter<T extends Animal> {
    private final List<T> animals = new ArrayList<>();

    public void addAnimal(T animal) {
        animals.add(animal);
    }

    public List<T> getAllAnimals() {
        return animals;
    }

    public List<T> findBySpecies(String species) {
        List<T> selectedAnimals = new ArrayList<>();
        for (T animal : animals) {
            if (animal.getSpecies().equals(species)) {
                selectedAnimals.add(animal);
            }
        }
        return selectedAnimals;
    }

    public List<T> findAvailableAnimals() {
        List<T> availableAnimals = new ArrayList<>();
        for (T animal : animals) {
            if (animal.getAdoptionStatus().equals(AdoptionStatus.AVAILABLE)) {
                availableAnimals.add(animal);
            }
        }
        return availableAnimals;
    }

    public void markAsAdopted(String id) {
        for (T animal : animals) {
            if (animal.getId().toString().equals(id)) {
                animal.markAsAdopted();
                break;
            }
        }
    }
}
