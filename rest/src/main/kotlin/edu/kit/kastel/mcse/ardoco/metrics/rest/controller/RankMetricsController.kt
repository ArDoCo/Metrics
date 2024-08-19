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
    fun calculateClassificationMetrics(
        @RequestBody body: RankMetricsRequest
    ): SingleRankMetricsResult {
        val rankMetricsCalculator = RankMetricsCalculator.Instance
        val result = rankMetricsCalculator.calculateMetrics(body.rankedResults, body.groundTruth)
        return result
    }

    @Operation(summary = "Calculate rank metrics for multiple projects. Calculate the average and optionally a weighted average.")
    @PostMapping("/average")
    fun calculateMultipleRankMetrics(
        @RequestBody body: AverageRankMetricsRequest
    ): AverageRankMetricsResponse {
        val rankMetricsCalculator = RankMetricsCalculator.Instance

        val requests = body.classificationMetricsRequests
        val results =
            requests.map {
                rankMetricsCalculator.calculateMetrics(it.rankedResults, it.groundTruth)
            }

        val averages = rankMetricsCalculator.calculateAverages(results, body.weights)

        return AverageRankMetricsResponse(averages)
    }

    data class AverageRankMetricsRequest(
        val classificationMetricsRequests: List<RankMetricsRequest>,
        val weights: List<Int>? = null
    )

    data class AverageRankMetricsResponse(
        val classificationResults: List<AggregatedRankMetricsResult>
    )

    data class RankMetricsRequest(
        val rankedResults: List<List<String>>,
        val groundTruth: Set<String>
    )
}
