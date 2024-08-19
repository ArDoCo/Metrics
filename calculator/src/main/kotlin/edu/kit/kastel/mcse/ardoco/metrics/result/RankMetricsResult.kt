package edu.kit.kastel.mcse.ardoco.metrics.result

interface RankMetricsResult {
    val map: Double
    val lag: Double

    fun prettyPrint()
}
