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
     * @param rankedResults the ranked results as a list of sorted lists (most relevant artifacts first). Each list represents one query of a source artifact.
     * @param groundTruth the ground truth
     * @param stringProvider a function to convert the ranked results and ground truth to strings
     * @param relevanceBasedInput the input for relevance based calculations
     *
     * @return the rank metrics result
     */
    fun <T> calculateMetrics(
        rankedResults: List<List<T>>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String,
        relevanceBasedInput: RelevanceBasedInput<T>?
    ): SingleRankMetricsResult {
        return calculateMetrics(
            rankedResults.map { id -> id.map { stringProvider(it) } },
            groundTruth.map { stringProvider(it) }.toSet(),
            relevanceBasedInput?.let { rbi ->
                RelevanceBasedInput(
                    rbi.rankedRelevances.map { id -> id.map { rbi.doubleProvider(it) } },
                    { it },
                    rbi.biggerIsMoreSimilar
                )
            }
        )
    }

    /**
     * @param rankedRelevances An optional list of lists representing the relevance scores associated with each ranked result.
     *                         If provided, this list must correspond to `rankedResults` in structure. If `null`, the relevance scores are ignored.
     * @param doubleProvider A function that converts the ranked relevances into their double representations.
     * @param biggerIsMoreSimilar Whether the relevance scores are more similar if bigger
     */
    data class RelevanceBasedInput<T>(
        val rankedRelevances: List<List<T>>,
        val doubleProvider: ((T) -> Double),
        val biggerIsMoreSimilar: Boolean
    )

    /**
     * Calculates the metrics for the given ranked results.
     * @param rankedResults the ranked results as a list of sorted lists (most relevant artifacts first). Each list represents one query of a source artifact.
     * @param groundTruth the ground truth
     * @param relevanceBasedInput the input for relevance based calculation
     * @return the rank metrics result
     */
    fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>,
        relevanceBasedInput: RelevanceBasedInput<Double>?
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
