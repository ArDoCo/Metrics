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
    override fun calculateMetrics(
        classification: Set<String>,
        groundTruth: Set<String>,
        confusionMatrixSum: Int?
    ): SingleClassificationResult {
        val tp = classification.intersect(groundTruth).size
        val fp = classification.size - tp
        val fn = groundTruth.size - tp
        val tn = confusionMatrixSum?.let { sum -> sum - (tp + fp + fn) }

        return calculateMetrics(tp, fp, fn, tn)
    }

    private fun calculateMetrics(
        tp: Int,
        fp: Int,
        fn: Int,
        tn: Int?
    ): SingleClassificationResult {
        val precision = calculatePrecision(tp, fp)
        val recall = calculateRecall(tp, fn)
        val f1 = calculateF1(precision, recall)

        if (tn == null) {
            return SingleClassificationResult(tp, fp, fn, null, precision, recall, f1, null, null, null, null, null)
        }

        val accuracy = calculateAccuracy(tp, fp, fn, tn)
        val specificity = calculateSpecificity(tn, fp)
        val phiCoefficient = calculatePhiCoefficient(tp, fp, fn, tn)
        val phiCoefficientMax = calculatePhiCoefficientMax(tp, fp, fn, tn)
        val phiOverPhiMax = calculatePhiOverPhiMax(tp, fp, fn, tn)

        return SingleClassificationResult(
            tp,
            fp,
            fn,
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
        singleClassificationResults: List<SingleClassificationResult>,
        weights: List<Int>?
    ): List<AggregatedClassificationResult> {
        val macroAverage = calculateMacroAverage(singleClassificationResults)

        val weightsForAverage = weights ?: singleClassificationResults.map { it.tp + it.fn }
        val weightedAverage = calculateWeightedAverage(singleClassificationResults, weightsForAverage, AggregationType.WEIGHTED_AVERAGE)

        val microAverage = calculateMicroAverage(singleClassificationResults)

        return listOf(macroAverage, weightedAverage, microAverage)
    }

    private fun calculateMicroAverage(singleClassificationResults: List<SingleClassificationResult>): AggregatedClassificationResult {
        if (singleClassificationResults.isEmpty()) {
            throw IllegalArgumentException("classificationResults must not be empty")
        }

        if (!singleClassificationResults.all { (singleClassificationResults[0].tn == null) == (it.tn == null) }) {
            throw IllegalArgumentException("All classificationResults must have either all or no tn")
        }

        var tp = 0
        var fp = 0
        var fn = 0
        var tn = 0

        for (classificationResult in singleClassificationResults) {
            tp += classificationResult.tp
            fp += classificationResult.fp
            fn += classificationResult.fn
            tn += classificationResult.tn ?: 0
        }

        val result = calculateMetrics(tp, fp, fn, tn)
        return AggregatedClassificationResult(result, AggregationType.MICRO_AVERAGE, singleClassificationResults, null)
    }

    private fun calculateMacroAverage(singleClassificationResults: List<SingleClassificationResult>): AggregatedClassificationResult {
        return calculateWeightedAverage(singleClassificationResults, singleClassificationResults.map { 1 }, AggregationType.MACRO_AVERAGE)
    }

    private fun calculateWeightedAverage(
        singleClassificationResults: List<SingleClassificationResult>,
        weights: List<Int>,
        type: AggregationType
    ): AggregatedClassificationResult {
        if (singleClassificationResults.isEmpty()) {
            throw IllegalArgumentException("classificationResults must not be empty")
        }

        if (!singleClassificationResults.all { (singleClassificationResults[0].tn == null) == (it.tn == null) }) {
            throw IllegalArgumentException("All classificationResults must have either all or no tn")
        }

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

        if (singleClassificationResults[0].tn == null) {
            return AggregatedClassificationResult(
                type,
                precision,
                recall,
                f1,
                null, null, null, null, null,
                singleClassificationResults, weights
            )
        } else {
            return AggregatedClassificationResult(
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
