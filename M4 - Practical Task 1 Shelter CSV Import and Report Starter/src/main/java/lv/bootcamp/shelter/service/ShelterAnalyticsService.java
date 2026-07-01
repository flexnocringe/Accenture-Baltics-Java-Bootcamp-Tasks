package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShelterAnalyticsService {

    public ShelterReportData buildReportData(ImportResult importResult) {
        List<Animal> allAnimals = importResult.allAnimals();

        Set<String> uniqueSpecies = allAnimals.stream()
                .map(Animal::getSpecies)
                .collect(Collectors.toSet());

        Map<String, List<Animal>> animalsBySpecies = allAnimals.stream()
                .collect(Collectors.groupingBy(Animal::getSpecies));

        List<String> animalsNeedingVetInput = allAnimals.stream()
                .filter(animal -> animal.getAge() == null)
                .map(animal -> animal.getName() + "(" + animal.getSpecies() + ")")
                .toList();

        Map<String, Long> vaccinatedPerSpecies = allAnimals.stream()
                .filter(Animal::isVaccinated)
                .collect(Collectors.groupingBy(Animal::getSpecies, Collectors.counting()));

        Map<String, Long> unvaccinatedPerSpecies = allAnimals.stream()
                .filter(animal -> !animal.isVaccinated())
                .collect(Collectors.groupingBy(Animal::getSpecies, Collectors.counting()));

        Map<String, String> oldestAnimalPerSpecies = allAnimals.stream()
                .filter(animal -> animal.getAge() != null)
                .collect(Collectors.groupingBy(Animal::getSpecies,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Animal::getAge)),
                                optionalAnimal -> optionalAnimal
                                        .map(animal -> animal.getName() + " (age " + animal.getAge() + ")")
                                        .orElse(null)
                        )
                ));

        // TODO Step 2:
        // Fill all collections:
        // - allAnimals (already available from import)
        // - uniqueSpecies
        // - animalsBySpecies
        // - animalsNeedingVetInput with format name(species)

        // TODO Step 3:
        // Add necessary fields to ShelterReportData
        // Use stream pipelines for:
        // - vaccinated vs unvaccinated counts per species
        // - oldest animal per species (excluding unknown ages)

        return new ShelterReportData(
                importResult,
                uniqueSpecies,
                animalsBySpecies,
                animalsNeedingVetInput,
                vaccinatedPerSpecies,
                unvaccinatedPerSpecies,
                oldestAnimalPerSpecies
        );
    }
}
