package edu.kit.kastel.mcse.ardoco.metrics

/**
 * Represents the result of the rank metrics calculation.
 *
 * @param map The mean average precision.
 * @param lag The lag.
 */
data class RankMetricsResult(
    val map: Double,
    val lag: Double
) {
    fun prettyPrint() {
        println("Mean Average Precision: $map")
        println("Lag: $lag")
    }
}
