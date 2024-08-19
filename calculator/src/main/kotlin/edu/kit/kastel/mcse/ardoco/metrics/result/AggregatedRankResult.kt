package edu.kit.kastel.mcse.ardoco.metrics.result

data class AggregatedRankResult(
    val type: AggregationType,
    val map: Double,
    val lag: Double,
    val originalRankResults: List<RankMetricsResult>,
    val weights: List<Int>
) {
    fun prettyPrint() {
        println("Type: $type")
        println("Mean Average Precision: $map")
        println("Lag: $lag")
    }
}
