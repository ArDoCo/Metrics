package edu.kit.kastel.mcse.ardoco.metrics.result

/**
 * Represents the result of the rank metrics calculation.
 *
 * @param map The mean average precision.
 * @param lag The lag.
 * @param groundTruthSize The size of the ground truth.
 */
data class RankMetricsResult(
    val map: Double,
    val lag: Double,
    val groundTruthSize: Int
) {
    fun prettyPrint() {
        println("Mean Average Precision: $map")
        println("Lag: $lag")
    }
}
