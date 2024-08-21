package edu.kit.kastel.mcse.ardoco.metrics

import edu.kit.kastel.mcse.ardoco.metrics.internal.RankMetricsCalculatorImpl
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedRankMetricsResult
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult

/**
 * The interface to calculate rank metrics.
 */
interface RankMetricsCalculator {
    companion object {
        /**
         * A default instance of the rank metrics calculator.
         */
        @JvmStatic
        val Instance: RankMetricsCalculator = RankMetricsCalculatorImpl()
    }

    /**
     * Calculates the metrics for the given ranked results.
     * @param rankedResults the ranked results //TODO info about the format
     * @param groundTruth the ground truth
     * @param stringProvider a function to convert the ranked results and ground truth to strings
     * @return the rank metrics result
     */
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

    /**
     * Calculates the metrics for the given ranked results.
     * @param rankedResults the ranked results
     * @param groundTruth the ground truth
     * @return the rank metrics result
     */
    fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>
    ): SingleRankMetricsResult

    /**
     * Calculates the averages of the given rank results.
     * @param singleRankMetricsResults the rank results
     * @param weights the weights for the rank results. If not provided, the size of the gold standard is used as weight.
     * @return the aggregated rank results
     */
    fun calculateAverages(
        singleRankMetricsResults: List<SingleRankMetricsResult>,
        weights: List<Int>? = null
    ): List<AggregatedRankMetricsResult>
}
