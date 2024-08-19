package edu.kit.kastel.mcse.ardoco.metrics.internal

import edu.kit.kastel.mcse.ardoco.metrics.AggregatedClassificationResult
import edu.kit.kastel.mcse.ardoco.metrics.AggregationType
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationResult
import edu.kit.kastel.mcse.ardoco.metrics.calculateAccuracy
import edu.kit.kastel.mcse.ardoco.metrics.calculateF1
import edu.kit.kastel.mcse.ardoco.metrics.calculatePhiCoefficient
import edu.kit.kastel.mcse.ardoco.metrics.calculatePhiCoefficientMax
import edu.kit.kastel.mcse.ardoco.metrics.calculatePhiOverPhiMax
import edu.kit.kastel.mcse.ardoco.metrics.calculatePrecision
import edu.kit.kastel.mcse.ardoco.metrics.calculateRecall
import edu.kit.kastel.mcse.ardoco.metrics.calculateSpecificity

internal class ClassificationMetricsCalculatorImpl : ClassificationMetricsCalculator {
    override fun calculateMetrics(
        classification: Set<String>,
        groundTruth: Set<String>,
        confusionMatrixSum: Int?
    ): ClassificationResult {
        val tp = classification.intersect(groundTruth).size
        val fp = classification.size - tp
        val fn = groundTruth.size - tp

        val precision = calculatePrecision(tp, fp)
        val recall = calculateRecall(tp, fn)
        val f1 = calculateF1(precision, recall)

        if (confusionMatrixSum == null) {
            return ClassificationResult(tp, fp, fn, null, precision, recall, f1, null, null, null, null, null)
        }

        val tn = confusionMatrixSum - (tp + fp + fn)
        val accuracy = calculateAccuracy(tp, fp, fn, tn)
        val specificity = calculateSpecificity(tn, fp)
        val phiCoefficient = calculatePhiCoefficient(tp, fp, fn, tn)
        val phiCoefficientMax = calculatePhiCoefficientMax(tp, fp, fn, tn)
        val phiOverPhiMax = calculatePhiOverPhiMax(tp, fp, fn, tn)

        return ClassificationResult(
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
        classificationResults: List<ClassificationResult>,
        weights: List<Int>?
    ): List<AggregatedClassificationResult> {
        val average = calculateAverage(classificationResults)

        val weightsForAverage = weights ?: classificationResults.map { it.tp + it.fn }
        val weightedAverage = calculateWeightedAverage(classificationResults, weightsForAverage, AggregationType.WEIGHTED_AVERAGE)

        return listOf(average, weightedAverage)
    }

    private fun calculateAverage(classificationResults: List<ClassificationResult>): AggregatedClassificationResult {
        return calculateWeightedAverage(classificationResults, classificationResults.map { 1 }, AggregationType.AVERAGE)
    }

    private fun calculateWeightedAverage(
        classificationResults: List<ClassificationResult>,
        weights: List<Int>,
        type: AggregationType
    ): AggregatedClassificationResult {
        if (classificationResults.isEmpty()) {
            throw IllegalArgumentException("classificationResults must not be empty")
        }

        if (!classificationResults.all { (classificationResults[0].tn == null) == (it.tn == null) }) {
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

        for ((i, classificationResult) in classificationResults.withIndex()) {
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

        if (classificationResults[0].tn == null) {
            return AggregatedClassificationResult(
                type,
                precision,
                recall,
                f1,
                null, null, null, null, null,
                classificationResults, weights
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
                classificationResults, weights
            )
        }
    }
}
