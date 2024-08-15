package edu.kit.kastel.mcse.ardoco.metrics.internal

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

    override fun calculateAverage(classificationResults: Collection<ClassificationResult>): ClassificationResult {
        val sum = classificationResults.reduce { acc, classificationResult -> acc.addToAll(classificationResult) }
        val size = classificationResults.size
        return sum.normalizeOnlyMetrics(size)
    }

    override fun calculateWeightedAverage(
        classificationResults: List<ClassificationResult>,
        weights: List<Int>
    ): ClassificationResult {
        if (classificationResults.size != weights.size) {
            throw IllegalArgumentException("The number of classification results and weights must be equal")
        }

        val sum =
            classificationResults.zip(weights) //
                .map { (classificationResult, weight) -> classificationResult.timesOnlyMetrics(weight) }
                .reduce { acc, classificationResult -> acc.addToAll(classificationResult) }
        val allWeights = weights.sum()
        return sum.normalizeOnlyMetrics(allWeights)
    }
}
