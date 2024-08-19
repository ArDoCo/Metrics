package edu.kit.kastel.mcse.ardoco.metrics.result

data class AggregatedRankMetricsResult(
    val type: AggregationType,
    override val map: Double,
    override val lag: Double,
    val originalRankResults: List<SingleRankMetricsResult>,
    val weights: List<Int>
) : RankMetricsResult {
    override fun prettyPrint() {
        println("Type: $type")
        println("Mean Average Precision: $map")
        println("Lag: $lag")
    }
}
