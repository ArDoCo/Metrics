package edu.kit.kastel.mcse.ardoco.metrics.result

/**
 * Represents the result of the rank metrics calculation.
 *
 * @param map The mean average precision.
 * @param lag The lag.
 * @param auc The AUC ROC
 * @param groundTruthSize The size of the ground truth.
 */
data class SingleRankMetricsResult(
    override val map: Double,
    override val lag: Double,
    override val auc: Double?,
    val groundTruthSize: Int
) : RankMetricsResult
