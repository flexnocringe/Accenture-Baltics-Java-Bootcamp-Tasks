package lv.bootcamp.shelter.task4;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Task 4: Collection and sorting tests
 * <p>
 * Practice:
 * - AssertJ list assertions (extracting, containsExactly)
 * - Testing sort order
 * - Testing null/empty input handling
 * <p>
 * Instructions:
 * Write tests for AnimalSorter. Use AssertJ's extracting() and containsExactly()
 * to verify the order of results.
 */
@DisplayName("AnimalSorter")
class AnimalSorterTest {

    private AnimalSorter sorter;

    private Animal buddy;
    private Animal luna;
    private Animal max;
    private Animal bella;

    @BeforeEach
    void setUp() {
        sorter = new AnimalSorter();
        buddy = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));
        luna = new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10));
        max = new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20));
        bella = new Animal("Bella", "Cat", 1, true, LocalDate.of(2026, 1, 5));
    }

    // --- sortByAge ---

    @Test
    @DisplayName("sortByAge: returns animals ordered youngest to oldest")
    void shouldSortByAgeAscending() {
        // TODO: Call sorter.sortByAge with [buddy, luna, max, bella]
        // TODO: Use assertThat(result).extracting(Animal::getName)
        //       .containsExactly("Bella", "Luna", "Buddy", "Max")
        List<Animal> sorted = sorter.sortByAge(List.of(buddy, luna, max, bella));
        assertThat(sorted).extracting(Animal::getName).containsExactly("Bella", "Luna", "Buddy", "Max");
    }

    @Test
    @DisplayName("sortByAge: returns empty list for null input")
    void shouldReturnEmptyForNullInput() {
        // TODO: Call sorter.sortByAge(null)
        // TODO: Assert result is empty
        List<Animal> sorted = sorter.sortByAge(null);
        assertThat(sorted).isEmpty();
    }

    @Test
    @DisplayName("sortByAge: returns empty list for empty input")
    void shouldReturnEmptyForEmptyInput() {
        // TODO: Call sorter.sortByAge(List.of())
        // TODO: Assert result is empty
        List<Animal> sorted = sorter.sortByAge(List.of());
        assertThat(sorted).isEmpty();
    }

    @Test
    @DisplayName("sortByAge: does not modify the original list")
    void shouldNotModifyOriginalList() {
        // TODO: Create a list, sort it, then verify the original list order is unchanged\
        List<Animal> original = List.of(buddy, luna, max, bella);
        sorter.sortByAge(original);
        assertThat(original).extracting(Animal::getName).containsExactly("Buddy", "Luna", "Max", "Bella");
    }

    // --- sortByName ---

    @Test
    @DisplayName("sortByName: returns animals in alphabetical order")
    void shouldSortByNameAlphabetically() {
        // TODO: Call sorter.sortByName with [buddy, luna, max, bella]
        // TODO: Verify order is Bella, Buddy, Luna, Max
        List<Animal> sorted = sorter.sortByName(List.of(buddy, luna, max, bella));
        assertThat(sorted).extracting(Animal::getName).containsExactly("Bella", "Buddy", "Luna", "Max");
    }

    @Test
    @DisplayName("sortByName: is case-insensitive")
    void shouldSortNamesCaseInsensitively() {
        // TODO: Create animals with mixed case names (e.g., "zebra", "Alpha")
        // TODO: Verify alphabetical order ignores case\
        Animal zebra = new Animal("zebra", "Horse", 4, true, LocalDate.of(2026, 1, 25));
        Animal alpha = new Animal("Alpha", "Dog", 2, true, LocalDate.of(2026, 1, 25));
        Animal chuck = new Animal("Chuck", "Dog", 3, true, LocalDate.of(2026, 1, 25));
        Animal champ = new Animal("champ", "Dog", 4, true, LocalDate.of(2026, 1, 25));
        List<Animal> sorted = sorter.sortByName(List.of(zebra, alpha, chuck, champ));
        assertThat(sorted).extracting(Animal::getName).containsExactly("Alpha", "champ", "Chuck", "zebra");
    }

    // --- sortByIntakeDate ---

    @Test
    @DisplayName("sortByIntakeDate: returns animals from earliest to latest")
    void shouldSortByIntakeDateAscending() {
        // TODO: Call sorter.sortByIntakeDate with [buddy, luna, max, bella]
        // TODO: Verify order by date: bella (Jan 5), luna (Jan 10), buddy (Jan 15), max (Jan 20)
        List<Animal> sorted = sorter.sortByIntakeDate(List.of(buddy, luna, max, bella));
        assertThat(sorted).extracting(Animal::getName).containsExactly("Bella", "Luna", "Buddy", "Max");
    }

    // --- sortBySpeciesThenAgeDescending ---

    @Test
    @DisplayName("sortBySpeciesThenAgeDescending: groups by species then orders by age desc")
    void shouldSortBySpeciesThenAgeDesc() {
        // TODO: Call sorter.sortBySpeciesThenAgeDescending with [buddy, luna, max, bella]
        // TODO: Expected order: Cat group (Luna age 2, Bella age 1), Dog group (Max age 5, Buddy age 3)
        // TODO: Verify with extracting(Animal::getName).containsExactly(...)
        List<Animal> sorted = sorter.sortBySpeciesThenAgeDescending(List.of(buddy, luna, max, bella));
        assertThat(sorted).extracting(Animal::getName).containsExactly("Luna", "Bella", "Max", "Buddy");
    }
}
