package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.ClassificationMetricsCalculatorImpl

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

    fun calculateAverage(classificationResults: Collection<ClassificationResult>): ClassificationResult

    fun calculateWeightedAverage(
        classificationResults: List<ClassificationResult>,
        weights: List<Int>
    ): ClassificationResult
}
