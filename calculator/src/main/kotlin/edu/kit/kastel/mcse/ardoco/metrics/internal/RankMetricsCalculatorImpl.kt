package edu.kit.kastel.mcse.ardoco.metrics.internal

import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateLAG
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateMAP
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedRankResult
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregationType
import edu.kit.kastel.mcse.ardoco.metrics.result.RankMetricsResult

internal class RankMetricsCalculatorImpl : RankMetricsCalculator {
    override fun calculateMetrics(
        rankedResults: List<List<String>>,
        groundTruth: Set<String>
    ): RankMetricsResult {
        val map = calculateMAP(rankedResults, groundTruth)
        val lag = calculateLAG(rankedResults, groundTruth)
        return RankMetricsResult(map, lag, groundTruth.size)
    }

    override fun calculateAverages(
        rankMetricsResults: List<RankMetricsResult>,
        weights: List<Int>?
    ): List<AggregatedRankResult> {
        val macroAverage = calculateMacroAverage(rankMetricsResults)

        val weightsForAverage = weights ?: rankMetricsResults.map { it.groundTruthSize }
        val weightedAverage = calculateWeightedAverage(rankMetricsResults, weightsForAverage, AggregationType.WEIGHTED_AVERAGE)

        return listOf(macroAverage, weightedAverage)
    }

    private fun calculateMacroAverage(rankMetricsResults: List<RankMetricsResult>): AggregatedRankResult {
        return calculateWeightedAverage(rankMetricsResults, rankMetricsResults.map { 1 }, AggregationType.MACRO_AVERAGE)
    }

    private fun calculateWeightedAverage(
        rankMetricsResults: List<RankMetricsResult>,
        weights: List<Int>,
        type: AggregationType
    ): AggregatedRankResult {
        if (rankMetricsResults.isEmpty()) {
            throw IllegalArgumentException("rankMetricsResults must not be empty")
        }

        var map = 0.0
        var lag = 0.0

        var sumOfWeights = 0.0

        for ((i, rankMetricsResult) in rankMetricsResults.withIndex()) {
            map += rankMetricsResult.map * weights[i]
            lag += rankMetricsResult.lag * weights[i]
            sumOfWeights += weights[i]
        }

        map /= sumOfWeights
        lag /= sumOfWeights

        return AggregatedRankResult(type, map, lag, rankMetricsResults, weights)
    }
}
