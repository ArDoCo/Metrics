package edu.kit.kastel.mcse.ardoco.metrics.rest.controller

import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/classification-metrics")
class ClassificationMetricsController {
    @GetMapping
    fun running(): String {
        return "ClassificationMetricsController is running"
    }

    @PostMapping
    fun calculateClassificationMetrics(
        @RequestBody body: ClassificationMetricsRequest
    ): ClassificationResult {
        val classificationMetricsCalculator = ClassificationMetricsCalculator.Instance
        val result = classificationMetricsCalculator.calculateMetrics(body.classification.toSet(), body.groundTruth.toSet(), body.confusionMatrixSum)
        return result
    }

    data class ClassificationMetricsRequest(
        val classification: List<String>,
        val groundTruth: List<String>,
        val confusionMatrixSum: Int? = null
    )
}
