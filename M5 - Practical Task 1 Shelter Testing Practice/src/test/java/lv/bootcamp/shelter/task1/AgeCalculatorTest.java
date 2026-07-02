package lv.bootcamp.shelter.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 1: Pure logic tests
 * <p>
 * Practice:
 * - Arrange-Act-Assert pattern
 * - Good test naming
 * - assertEquals for return values
 * - assertThrows for invalid input
 * <p>
 * Instructions:
 * Write tests for AgeCalculator. Each TODO describes one test to write.
 * Remove the TODO comments as you implement each test.
 */
@DisplayName("AgeCalculator")
class AgeCalculatorTest {

    private AgeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new AgeCalculator();
    }

    // --- toMonths() ---

    @Test
    @DisplayName("toMonths: 0 years returns 0 months")
    void shouldReturnZeroMonthsForZeroYears() {
        // TODO: Arrange — nothing extra needed (calculator is set up in @BeforeEach)
        // TODO: Act — call calculator.toMonths(0)
        // TODO: Assert — assertEquals(0, result)
        assertEquals(0, calculator.toMonths(0));
    }

    @Test
    @DisplayName("toMonths: positive years returns correct months")
    void shouldConvertPositiveYearsToMonths() {
        // TODO: Test that 3 years = 36 months
        assertEquals(36, calculator.toMonths(3));
    }

    @Test
    @DisplayName("toMonths: negative years throws IllegalArgumentException")
    void shouldThrowForNegativeYears() {
        // TODO: Use assertThrows to verify that toMonths(-1) throws IllegalArgumentException
        // TODO: Optionally check the exception message contains "negative"
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculator.toMonths(-1));
        assertTrue(ex.getMessage().contains("negative"));
    }

    // --- dogToHumanYears() ---

    @Test
    @DisplayName("dogToHumanYears: age 0 returns 0")
    void shouldReturnZeroHumanYearsForPuppy() {
        // TODO: Test that dogToHumanYears(0) returns 0
        assertEquals(0, calculator.dogToHumanYears(0));
    }

    @Test
    @DisplayName("dogToHumanYears: age 1 returns 15")
    void shouldReturnFifteenForOneYearOldDog() {
        // TODO: Test that dogToHumanYears(1) returns 15
        assertEquals(15, calculator.dogToHumanYears(1));
    }

    @Test
    @DisplayName("dogToHumanYears: age 2 returns 24")
    void shouldReturnTwentyFourForTwoYearOldDog() {
        // TODO: Test that dogToHumanYears(2) returns 24
        assertEquals(24, calculator.dogToHumanYears(2));
    }

    @Test
    @DisplayName("dogToHumanYears: age 5 returns 39")
    void shouldCalculateCorrectlyForOlderDog() {
        // TODO: Test that dogToHumanYears(5) returns 24 + (5-2)*5 = 39
        assertEquals(39, calculator.dogToHumanYears(5));
    }

    @Test
    @DisplayName("dogToHumanYears: negative age throws IllegalArgumentException")
    void shouldThrowForNegativeDogAge() {
        // TODO: Use assertThrows for negative input
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculator.dogToHumanYears(-1));
        assertTrue(ex.getMessage().contains("negative"));
    }

    // --- isBaby() ---

    @Test
    @DisplayName("isBaby: age 0 returns true")
    void shouldReturnTrueForAgZero() {
        // TODO: Test that isBaby(0) returns true
        assertTrue(calculator.isBaby(0));
    }

    @Test
    @DisplayName("isBaby: age 1 returns false")
    void shouldReturnFalseForAgeOne() {
        // TODO: Test that isBaby(1) returns false
        assertFalse(calculator.isBaby(1));
    }
}
