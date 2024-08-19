package edu.kit.kastel.mcse.ardoco.metrics.internal

import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsResult
import edu.kit.kastel.mcse.ardoco.metrics.calculateLAG
import edu.kit.kastel.mcse.ardoco.metrics.calculateMAP

internal class RankMetricsCalculatorImpl : RankMetricsCalculator {
    override fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>
    ): RankMetricsResult {
        val map = calculateMAP(rankedResults, groundTruth)
        val lag = calculateLAG(rankedResults, groundTruth)
        return RankMetricsResult(map, lag)
    }
}
