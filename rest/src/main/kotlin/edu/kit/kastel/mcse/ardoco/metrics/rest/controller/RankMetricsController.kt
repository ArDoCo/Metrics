package edu.kit.kastel.mcse.ardoco.metrics.rest.controller

import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsResult
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
    ): RankMetricsResult {
        val rankMetricsCalculator = RankMetricsCalculator.Instance
        val result = rankMetricsCalculator.calculateMetrics(body.rankedResults, body.groundTruth)
        return result
    }

    data class RankMetricsRequest(
        val rankedResults: List<List<String>>,
        val groundTruth: Set<String>
    )
}
