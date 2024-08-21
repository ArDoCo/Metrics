package edu.kit.kastel.mcse.ardoco.metrics.rest.controller

import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedRankMetricsResult
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rank-metrics")
class RankMetricsController {
    @Operation(summary = "Check if the service is running")
    @GetMapping
    fun running(): String {
        return "RankMetricsController is running"
    }

    @Operation(summary = "Calculate rank metrics for one project")
    @PostMapping
    fun calculateRankMetrics(
        @RequestBody body: RankMetricsRequest
    ): SingleRankMetricsResult {
        val rankMetricsCalculator = RankMetricsCalculator.Instance
        val relevanceBasedInput = relevanceBasedInput(body)
        val result = rankMetricsCalculator.calculateMetrics(body.rankedResults, body.groundTruth, relevanceBasedInput)
        return result
    }

    private fun relevanceBasedInput(body: RankMetricsRequest): RankMetricsCalculator.RelevanceBasedInput<Double>? {
        if ((body.rankedRelevances == null) != (body.biggerIsMoreSimilar == null)) {
            throw IllegalArgumentException("ranked relevances and bigger is more similar can only occur together")
        }
        val relevanceBasedInput =
            if (body.rankedRelevances != null && body.biggerIsMoreSimilar != null) RankMetricsCalculator.RelevanceBasedInput(
                body.rankedRelevances, { it }, body.biggerIsMoreSimilar
            ) else null
        return relevanceBasedInput
    }

    @Operation(summary = "Calculate rank metrics for multiple projects. Calculate the average and optionally a weighted average.")
    @PostMapping("/average")
    fun calculateMultipleRankMetrics(
        @RequestBody body: AverageRankMetricsRequest
    ): AverageRankMetricsResponse {
        val rankMetricsCalculator = RankMetricsCalculator.Instance

        val requests = body.rankMetricsRequests
        val results = requests.map {
            val relevanceBasedInput = relevanceBasedInput(it)
            rankMetricsCalculator.calculateMetrics(it.rankedResults, it.groundTruth, relevanceBasedInput)
        }

        val averages = rankMetricsCalculator.calculateAverages(results, body.weights)

        return AverageRankMetricsResponse(averages)
    }

    data class AverageRankMetricsRequest(
        val rankMetricsRequests: List<RankMetricsRequest>, val weights: List<Int>? = null
    )

    data class AverageRankMetricsResponse(
        val rankResults: List<AggregatedRankMetricsResult>
    )

    data class RankMetricsRequest(
        val rankedResults: List<List<String>>,
        val groundTruth: Set<String>,
        val rankedRelevances: List<List<Double>>?,
        val biggerIsMoreSimilar: Boolean?
    )
}
