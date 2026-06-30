package org.example.menu;

import org.example.model.*;
import org.example.shelter.Shelter;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Shelter<Animal> shelter;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(Shelter<Animal> shelter) {
        this.shelter = shelter;
    }

    public void start() {
        Scanner input = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = input.nextInt();
            if (choice == 1) {
                addNewAnimal(input);
            } else if (choice == 2) {
                printAnimalList(shelter.getAllAnimals());
            } else if (choice == 3) {
                searchBySpecies(input);
            } else if (choice == 4) {
                printAnimalList(shelter.findAvailableAnimals());
            } else if (choice == 5) {
                markAnimalAsAdopted(input);
            } else if (choice == 0) {
                break;
            }
        }
    }

    private void markAnimalAsAdopted(Scanner input) {
        System.out.println("Enter animal ID to mark as adopted:");
        String id = input.next();
        shelter.markAsAdopted(id);
    }

    private void addNewAnimal(Scanner input) {
        Integer animalType = selectAnimalType(input);
        if (animalType == null) {return;}
        shelter.addAnimal(createNewAnimal(animalType, input));
    }

    private Animal createNewAnimal(int animalType, Scanner input) {
        int age;
        String name;
        System.out.println("Enter animal name:");
        name = input.next();
        System.out.println("Enter animal age:");
        age = input.nextInt();
        switch (animalType) {
            case 1:
                return new Dog(new AnimalId(), name, age);
            case 2:
                return new Cat(new AnimalId(), name, age);
            case 3:
                return new Bird(new AnimalId(), name, age);
        }
        return null;
    }

    private void printAnimalList(List<Animal> animals) {
        for (Animal animal : animals) {
            System.out.println(animal.toString());
        }
        System.out.println();
    }

    private void searchBySpecies(Scanner input) {
        Integer animalType = selectAnimalType(input);
        if (animalType == null) {return;}
        switch (animalType) {
            case 1:
                printAnimalList(shelter.findBySpecies("Dog"));
                break;
            case 2:
                printAnimalList(shelter.findBySpecies("Cat"));
                break;
            case 3:
                printAnimalList(shelter.findBySpecies("Bird"));
                break;
        }
    }

    private static Integer selectAnimalType(Scanner input) {
        System.out.println("""
                Select animal type:
                1. Dog
                2. Cat
                3. Bird
                """);
        int animalType = input.nextInt();
        if (animalType > 3) {
            System.out.println("Invalid animal type!");
            return null;
        }
        return animalType;
    }

    private void printMenu() {
        System.out.println("""
                1. Add animal
                2. List all animals
                3. Find animals by species
                4. List available animals
                5. Mark animal as adopted
                0. Exit
                """);
    }
}
