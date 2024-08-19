package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.RankMetricsCalculatorImpl
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedRankMetricsResult
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult

interface RankMetricsCalculator {
    companion object {
        @JvmStatic
        val Instance: RankMetricsCalculator = RankMetricsCalculatorImpl()
    }

    fun <T> calculateMetrics(
        rankedResults: List<List<T>>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String
    ): SingleRankMetricsResult {
        return calculateMetrics(
            rankedResults.map { id -> id.map { stringProvider(it) } },
            groundTruth.map { stringProvider(it) }.toSet()
        )
    }

    fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>
    ): SingleRankMetricsResult

    /**
     * Calculates the averages of the given rank results.
     * @param singleRankMetricsResults the rank results
     * @param weights the weights for the rank results. If not provided, the size of the gold standard is used as weight.
     */

    fun calculateAverages(
        singleRankMetricsResults: List<SingleRankMetricsResult>,
        weights: List<Int>? = null
    ): List<AggregatedRankMetricsResult>
}
