package lv.bootcamp.shelter.task5;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 5: Nested test classes for CSV parsing
 * <p>
 * Practice:
 * - @Nested to organize by scenario
 * - @DisplayName for readable output
 * - Testing Optional results (isPresent, isEmpty)
 * - Testing file I/O with temp files
 * <p>
 * Instructions:
 * Write tests for AnimalCsvParser. Use @Nested classes to group tests by scenario.
 * For file-based tests, use Files.createTempFile() and Files.writeString() to create test data.
 */
@DisplayName("AnimalCsvParser")
class AnimalCsvParserTest {

    private AnimalCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new AnimalCsvParser();
    }

    // ==================== parseRow tests ====================

    @Nested
    @DisplayName("When parsing valid rows")
    class ValidRows {

        @Test
        @DisplayName("parses a complete row into an Animal")
        void shouldParseCompleteRow() {
            // TODO: Call parser.parseRow("Buddy,Dog,3,true,2026-01-15")
            // TODO: Assert the result isPresent()
            // TODO: Assert the animal's name is "Buddy", species is "Dog", age is 3, etc.
            Optional<Animal> parsedAnimal = parser.parseRow("Buddy,Dog,3,true,2026-01-15");
            assertTrue(parsedAnimal.isPresent());
            Animal animal = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));
            assertEquals(animal, parsedAnimal.get());


        }

        @Test
        @DisplayName("trims whitespace from fields")
        void shouldTrimWhitespace() {
            // TODO: Call parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ")
            // TODO: Assert the parsed name is "Buddy" (trimmed)
            Animal parsedAnimal = parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ").get();
            assertEquals("Buddy", parsedAnimal.getName());
        }

        @Test
        @DisplayName("parses vaccinated=false correctly")
        void shouldParseFalseVaccination() {
            // TODO: Parse a row with "false" for vaccinated
            // TODO: Assert animal.isVaccinated() == false
            Animal parsedAnimal = parser.parseRow("Buddy,Dog,3,false,2026-01-15").get();
            assertFalse(parsedAnimal.isVaccinated());
        }
    }

    @Nested
    @DisplayName("When parsing malformed rows")
    class MalformedRows {

        @Test
        @DisplayName("returns empty for null input")
        void shouldReturnEmptyForNull() {
            // TODO: Call parser.parseRow(null)
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow(null).isEmpty());
        }

        @Test
        @DisplayName("returns empty for blank input")
        void shouldReturnEmptyForBlank() {
            // TODO: Call parser.parseRow("   ")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow("    ").isEmpty());
        }

        @Test
        @DisplayName("returns empty when row has fewer than 5 fields")
        void shouldReturnEmptyForTooFewFields() {
            // TODO: Call parser.parseRow("Buddy,Dog,3")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow("Buddy,Dog,3").isEmpty());
        }

        @Test
        @DisplayName("returns empty when name is missing")
        void shouldReturnEmptyForMissingName() {
            // TODO: Call parser.parseRow(",Dog,3,true,2026-01-15")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow(",Dog,3,true,2026-01-15").isEmpty());
        }

        @Test
        @DisplayName("returns empty when age is not a number")
        void shouldReturnEmptyForBadAge() {
            // TODO: Call parser.parseRow("Buddy,Dog,old,true,2026-01-15")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow("Buddy,Dog,old,true,2026-01-15").isEmpty());
        }

        @Test
        @DisplayName("returns empty when age is negative")
        void shouldReturnEmptyForNegativeAge() {
            // TODO: Call parser.parseRow("Buddy,Dog,-1,true,2026-01-15")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow("Buddy,Dog,-1,true,2026-01-15").isEmpty());
        }

        @Test
        @DisplayName("returns empty when date is invalid")
        void shouldReturnEmptyForBadDate() {
            // TODO: Call parser.parseRow("Buddy,Dog,3,true,not-a-date")
            // TODO: Assert result isEmpty()
            assertTrue(parser.parseRow("Buddy,Dog,3,true,not-a-date").isEmpty());
        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {

        @Test
        @DisplayName("handles vaccinated field as any non-true string → false")
        void shouldTreatNonTrueAsFalse() {
            // TODO: Parse a row with vaccinated="maybe"
            // TODO: Assert isVaccinated() returns false (Boolean.parseBoolean behavior)
            Animal parsedAnimal = parser.parseRow("Buddy,Dog,3,maybe,2026-01-15").get();
            assertFalse(parsedAnimal.isVaccinated());
        }

        @Test
        @DisplayName("handles age 0 as valid")
        void shouldAcceptAgeZero() {
            // TODO: Parse a row with age=0
            // TODO: Assert result isPresent() and age is 0
            Optional<Animal> parsedAnimal = parser.parseRow("Buddy,Dog,0,true,2026-01-15");
            assertTrue(parsedAnimal.isPresent() && parsedAnimal.get().getAge() == 0);
        }
    }

    // ==================== parseFile tests ====================

    @Nested
    @DisplayName("When parsing a CSV file")
    class ParseFile {

        @Test
        @DisplayName("parses valid rows and counts skipped rows")
        void shouldParseFileAndCountSkipped() throws IOException {
            // TODO: Create a temp file with a header + 3 valid rows + 1 malformed row
            //   Hint: Path tempFile = Files.createTempFile("test-intake", ".csv");
            //         Files.writeString(tempFile, content, StandardCharsets.UTF_8);
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() has size 3
            // TODO: Assert result.skippedRows() == 1
            // TODO: Clean up: Files.deleteIfExists(tempFile)
            File tempFile = File.createTempFile("test-intake", ".csv");
            String content = """
                    name,species,age,vaccinated,intakeDate
                    Buddy,Dog,3,true,2026-01-15
                    Luna,Cat,2,true,2026-01-10
                    Max,Dog,5,false,2026-01-20
                    ,Dog,5,,2026-01-20""";
            Files.writeString(tempFile.toPath(), content, StandardCharsets.UTF_8);
            AnimalCsvParser.ParseResult result = parser.parseFile(tempFile.toPath());
            assertEquals(3, result.animals().size());
            assertEquals(1, result.skippedRows());
            Files.deleteIfExists(tempFile.toPath());
        }

        @Test
        @DisplayName("returns empty result for file with only a header")
        void shouldReturnEmptyForHeaderOnly() throws IOException {
            // TODO: Create a temp file with just "name,species,age,vaccinated,intakeDate"
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() is empty and skippedRows == 0
            File tempFile = File.createTempFile("test-intake", ".csv");
            String content = "name,species,age,vaccinated,intakeDate";
            Files.writeString(tempFile.toPath(), content, StandardCharsets.UTF_8);
            AnimalCsvParser.ParseResult result = parser.parseFile(tempFile.toPath());
            assertTrue(result.animals().isEmpty() && result.skippedRows() == 0);
        }

        @Test
        @DisplayName("throws IOException for non-existent file")
        void shouldThrowForMissingFile() {
            // TODO: Call parser.parseFile(Path.of("does-not-exist.csv"))
            // TODO: Assert it throws IOException
            assertThrows(IOException.class, () -> parser.parseFile(Path.of("does-not-exist.csv")));
        }
    }
}
