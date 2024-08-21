package edu.kit.kastel.mcse.ardoco.metrics.result

import edu.kit.kastel.mcse.ardoco.metrics.result.RankMetricsResult.Companion.logger

/**
 * Represents the aggregation of multiple rank results.
 */
data class AggregatedRankMetricsResult(
    /** The type of aggregation */
    val type: AggregationType,
    override val map: Double,
    override val lag: Double,
    override val auc: Double?,
    /** The original single rank results that were aggregated */
    val originalRankResults: List<SingleRankMetricsResult>,
    /** The weights of the original single rank results */
    val weights: List<Int>
) : RankMetricsResult {
    override fun prettyPrint() {
        logger.info("Type: $type")
        super.prettyPrint()
    }
}
