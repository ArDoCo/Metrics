package edu.kit.kastel.mcse.ardoco.metrics.internal

import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateAccuracy
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateF1
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculatePhiCoefficient
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculatePhiCoefficientMax
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculatePhiOverPhiMax
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculatePrecision
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateRecall
import edu.kit.kastel.mcse.ardoco.metrics.calculation.calculateSpecificity
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregatedClassificationResult
import edu.kit.kastel.mcse.ardoco.metrics.result.AggregationType
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult

internal class ClassificationMetricsCalculatorImpl : ClassificationMetricsCalculator {
    override fun <T> calculateMetrics(
        classification: Set<T>,
        groundTruth: Set<T>,
        confusionMatrixSum: Int?
    ): SingleClassificationResult<T> {
        val tp = classification.intersect(groundTruth)
        val fp = classification.filter { !groundTruth.contains(it) }.toSet()
        val fn = groundTruth.filter { !classification.contains(it) }.toSet()
        val tn = confusionMatrixSum?.let { sum -> sum - (tp.size + fp.size + fn.size) }

        val intermediateMetrics = calculateMetrics(tp.size, fp.size, fn.size, tn)
        return SingleClassificationResult(
            tp,
            fp,
            fn,
            tn,
            intermediateMetrics.precision,
            intermediateMetrics.recall,
            intermediateMetrics.f1,
            intermediateMetrics.accuracy,
            intermediateMetrics.specificity,
            intermediateMetrics.phiCoefficient,
            intermediateMetrics.phiCoefficientMax,
            intermediateMetrics.phiOverPhiMax
        )
    }

    private fun calculateMetrics(
        tp: Int,
        fp: Int,
        fn: Int,
        tn: Int?
    ): SingleClassificationResult<Nothing> {
        val precision = calculatePrecision(tp, fp)
        val recall = calculateRecall(tp, fn)
        val f1 = calculateF1(precision, recall)

        if (tn == null) {
            return SingleClassificationResult(setOf(), setOf(), setOf(), null, precision, recall, f1, null, null, null, null, null)
        }

        val accuracy = calculateAccuracy(tp, fp, fn, tn)
        val specificity = calculateSpecificity(tn, fp)
        val phiCoefficient = calculatePhiCoefficient(tp, fp, fn, tn)
        val phiCoefficientMax = calculatePhiCoefficientMax(tp, fp, fn, tn)
        val phiOverPhiMax = calculatePhiOverPhiMax(tp, fp, fn, tn)

        return SingleClassificationResult(
            setOf(),
            setOf(),
            setOf(),
            tn,
            precision,
            recall,
            f1,
            accuracy,
            specificity,
            phiCoefficient,
            phiCoefficientMax,
            phiOverPhiMax
        )
    }

    override fun calculateAverages(
        singleClassificationResults: List<SingleClassificationResult<*>>,
        weights: List<Int>?
    ): List<AggregatedClassificationResult> {
        val macroAverage = calculateMacroAverage(singleClassificationResults)

        val weightsForAverage = weights ?: singleClassificationResults.map { it.tp.size + it.fn.size }
        val weightedAverage = calculateWeightedAverage(singleClassificationResults, weightsForAverage, AggregationType.WEIGHTED_AVERAGE)

        val microAverage = calculateMicroAverage(singleClassificationResults)

        return listOf(macroAverage, weightedAverage, microAverage)
    }

    private fun calculateMicroAverage(singleClassificationResults: List<SingleClassificationResult<*>>): AggregatedClassificationResult {
        require(singleClassificationResults.isNotEmpty()) { "classificationResults must not be empty" }

        require(
            singleClassificationResults.all {
                (singleClassificationResults[0].tn == null) == (it.tn == null)
            }
        ) { "All classificationResults must have either all or no tn" }

        var tp = 0
        var fp = 0
        var fn = 0
        var tn: Int? = if (singleClassificationResults[0].tn != null) 0 else null

        for (classificationResult in singleClassificationResults) {
            tp += classificationResult.tp.size
            fp += classificationResult.fp.size
            fn += classificationResult.fn.size
            if (tn != null) {
                tn += classificationResult.tn!!
            }
        }

        val result = calculateMetrics(tp, fp, fn, tn)
        return AggregatedClassificationResult(result, AggregationType.MICRO_AVERAGE, singleClassificationResults, null)
    }

    private fun calculateMacroAverage(singleClassificationResults: List<SingleClassificationResult<*>>): AggregatedClassificationResult {
        return calculateWeightedAverage(singleClassificationResults, singleClassificationResults.map { 1 }, AggregationType.MACRO_AVERAGE)
    }

    private fun calculateWeightedAverage(
        singleClassificationResults: List<SingleClassificationResult<*>>,
        weights: List<Int>,
        type: AggregationType
    ): AggregatedClassificationResult {
        require(singleClassificationResults.isNotEmpty()) { "classificationResults must not be empty" }

        require(
            singleClassificationResults.all {
                (singleClassificationResults[0].tn == null) == (it.tn == null)
            }
        ) { "All classificationResults must have either all or no tn" }

        var precision = 0.0
        var recall = 0.0
        var f1 = 0.0
        var accuracy = 0.0
        var specificity = 0.0
        var phiCoefficient = 0.0
        var phiCoefficientMax = 0.0
        var phiOverPhiMax = 0.0

        var sumOfWeights = 0.0

        for ((i, classificationResult) in singleClassificationResults.withIndex()) {
            precision += classificationResult.precision * weights[i]
            recall += classificationResult.recall * weights[i]
            f1 += classificationResult.f1 * weights[i]
            accuracy += (classificationResult.accuracy ?: 0.0) * weights[i]
            specificity += (classificationResult.specificity ?: 0.0) * weights[i]
            phiCoefficient += (classificationResult.phiCoefficient ?: 0.0) * weights[i]
            phiCoefficientMax += (classificationResult.phiCoefficientMax ?: 0.0) * weights[i]
            phiOverPhiMax += (classificationResult.phiOverPhiMax ?: 0.0) * weights[i]

            sumOfWeights += weights[i]
        }

        precision /= sumOfWeights
        recall /= sumOfWeights
        f1 /= sumOfWeights
        accuracy /= sumOfWeights
        specificity /= sumOfWeights
        phiCoefficient /= sumOfWeights
        phiCoefficientMax /= sumOfWeights
        phiOverPhiMax /= sumOfWeights

        return if (singleClassificationResults[0].tn == null) {
            AggregatedClassificationResult(
                type,
                precision,
                recall,
                f1,
                null, null, null, null, null,
                singleClassificationResults, weights
            )
        } else {
            AggregatedClassificationResult(
                type,
                precision,
                recall,
                f1,
                accuracy,
                specificity,
                phiCoefficient,
                phiCoefficientMax,
                phiOverPhiMax,
                singleClassificationResults, weights
            )
        }
    }
}
