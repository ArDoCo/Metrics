package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.RankMetricsCalculatorImpl

interface RankMetricsCalculator {
    companion object {
        @JvmStatic
        val Instance: RankMetricsCalculator = RankMetricsCalculatorImpl()
    }

    fun <T> calculateMetrics(
        rankedResults: List<List<T>>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String
    ): RankMetricsResult {
        return calculateMetrics(
            rankedResults.map { id -> id.map { stringProvider(it) } },
            groundTruth.map { stringProvider(it) }.toSet()
        )
    }

    fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>
    ): RankMetricsResult
}
