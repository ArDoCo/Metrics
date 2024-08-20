package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.ClassificationMetricsCalculatorImpl
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedClassificationResult
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult

/**
 * Interface for calculating classification metrics.
 */
interface ClassificationMetricsCalculator {
    companion object {
        /** A default instance of the classification metrics calculator. */
        @JvmStatic
        val Instance: ClassificationMetricsCalculator = ClassificationMetricsCalculatorImpl()
    }

    /**
     * Calculates the metrics for the given classification.
     * @param classification the classification
     * @param groundTruth the ground truth
     * @param stringProvider a function to convert the classification and ground truth to strings
     * @param confusionMatrixSum the sum of the confusion matrix. If not provided, some metrics can't be calculated.
     * @return the classification result
     */
    fun <T> calculateMetrics(
        classification: Set<T>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String,
        confusionMatrixSum: Int?
    ): SingleClassificationResult<String> {
        return calculateMetrics(
            classification.map { stringProvider(it) }.toSet(),
            groundTruth.map { stringProvider(it) }.toSet(),
            confusionMatrixSum
        )
    }

    /**
     * Calculates the metrics for the given classification.
     * @param T the type of classified elements
     * @param classification the classification
     * @param groundTruth the ground truth
     * @param confusionMatrixSum the sum of the confusion matrix. If not provided, some metrics can't be calculated.
     * @return the classification result
     */
    fun <T> calculateMetrics(
        classification: Set<T>,
        groundTruth: Set<T>,
        confusionMatrixSum: Int?
    ): SingleClassificationResult<T>

    /**
     * Calculates the averages of the given classification results.
     * @param singleClassificationResults the classification results
     * @param weights the weights for the classification results. If not provided, the size of the gold standard is used as weight.
     * @return the aggregated classification results
     */
    fun calculateAverages(
        singleClassificationResults: List<SingleClassificationResult<*>>,
        weights: List<Int>? = null
    ): List<AggregatedClassificationResult>
}
