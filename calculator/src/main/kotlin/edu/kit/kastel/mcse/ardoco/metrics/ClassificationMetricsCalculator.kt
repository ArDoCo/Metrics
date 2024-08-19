package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.ClassificationMetricsCalculatorImpl
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedClassificationResult
import edu.kit.kastel.mcse.ardoco.metrics.result.ClassificationResult

interface ClassificationMetricsCalculator {
    companion object {
        @JvmStatic
        val Instance: ClassificationMetricsCalculator = ClassificationMetricsCalculatorImpl()
    }

    fun <T> calculateMetrics(
        classification: Set<T>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String,
        confusionMatrixSum: Int?
    ): ClassificationResult {
        return calculateMetrics(
            classification.map { stringProvider(it) }.toSet(),
            groundTruth.map { stringProvider(it) }.toSet(),
            confusionMatrixSum
        )
    }

    fun calculateMetrics(
        classification: Set<String>,
        groundTruth: Set<String>,
        confusionMatrixSum: Int?
    ): ClassificationResult

    /**
     * Calculates the averages of the given classification results.
     * @param classificationResults the classification results
     * @param weights the weights for the classification results. If not provided, the size of the gold standard is used as weight.
     */
    fun calculateAverages(
        classificationResults: List<ClassificationResult>,
        weights: List<Int>? = null
    ): List<AggregatedClassificationResult>
}
