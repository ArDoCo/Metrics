package edu.kit.kastel.mcse.ardoco.metrics.calculation

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class ClassificationMetricsTest {
    @Test
    fun calculatePrecisionTest() {
        assertAll(
            Executable { assertEquals(.5, calculatePrecision(10, 10), 1e-3) },
            Executable { assertEquals(.857, calculatePrecision(6, 1), 1e-3) },
            Executable { assertEquals(.154, calculatePrecision(10, 55), 1e-3) },
            Executable { assertEquals(.905, calculatePrecision(210, 22), 1e-3) }
        )
    }

    @Test
    fun calculateRecallTest() {
        assertAll(
            Executable { assertEquals(.5, calculateRecall(10, 10), 1e-3) },
            Executable { assertEquals(.75, calculateRecall(6, 2), 1e-3) },
            Executable { assertEquals(.154, calculateRecall(10, 55), 1e-3) },
            Executable { assertEquals(.871, calculateRecall(210, 31), 1e-3) }
        )
    }

    @Test
    fun calculateF1FromPrecisionRecallTest() {
        assertAll(
            Executable { assertEquals(1.0, calculateF1(1.0, 1.0), 1e-2) },
            Executable { assertEquals(0.0, calculateF1(0.0, 1.0), 1e-2) },
            Executable { assertEquals(0.0, calculateF1(1.0, 0.0), 1e-2) },
            Executable { assertEquals(0.18, calculateF1(.9, .1), 1e-2) },
            Executable { assertEquals(0.48, calculateF1(.6, .4), 1e-2) },
            Executable { assertEquals(0.42, calculateF1(.3, .7), 1e-2) },
            Executable { assertEquals(0.9, calculateF1(.9, .9), 1e-2) },
            Executable { assertEquals(0.48, calculateF1(.4, .6), 1e-2) }
        )
    }

    @Test
    fun calculateAccuracyTest() {
        assertAll(
            Executable { assertEquals(.5, calculateAccuracy(10, 10, 10, 10), 1e-3) },
            Executable { assertEquals(.75, calculateAccuracy(6, 1, 2, 3), 1e-3) },
            Executable { assertEquals(.214, calculateAccuracy(10, 55, 55, 20), 1e-3) },
            Executable { assertEquals(.967, calculateAccuracy(210, 22, 31, 1337), 1e-3) }
        )
    }

    @Test
    fun calculatePhiCoefficientTest() {
        assertAll(
            Executable { assertEquals(.0, calculatePhiCoefficient(10, 10, 10, 10), 1e-3) },
            Executable { assertEquals(.478, calculatePhiCoefficient(6, 1, 2, 3), 1e-3) },
            Executable { assertEquals(-.579, calculatePhiCoefficient(10, 55, 55, 20), 1e-3) },
            Executable {
                assertEquals(
                    .869,
                    calculatePhiCoefficient(210, 22, 31, 1337),
                    1e-3
                )
            },
            Executable { assertEquals(.0, calculatePhiCoefficient(0, 0, 11, 11), 1e-3) },
            Executable { assertEquals(.0, calculatePhiCoefficient(11, 0, 11, 0), 1e-3) }
        )
    }

    @Test
    fun calculateSpecificityTest() {
        assertAll(
            Executable { assertEquals(.5, calculateSpecificity(1, 1), 1e-3) },
            Executable { assertEquals(.76, calculateSpecificity(1337, 420), 1e-3) },
            Executable { assertEquals(.0, calculateSpecificity(0, 20), 1e-3) },
            Executable { assertEquals(1.0, calculateSpecificity(20, 0), 1e-3) },
            Executable { assertEquals(1.0, calculateSpecificity(0, 0), 1e-3) },
            Executable { assertEquals(.375, calculateSpecificity(3, 5), 1e-3) }
        )
    }
}
