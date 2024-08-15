package edu.kit.kastel.mcse.ardoco.metrics

interface ClassificationMetricsCalculator {
    companion object {
        @JvmStatic
        val Instance: ClassificationMetricsCalculator = ClassificationMetricsCalculatorImpl()
    }

    fun <T> calculateMetrics(
        classification: Set<T>,
        groundTruth: Set<T>,
        stringProvider: (T) -> String,
        confusionMatrixSum: Int?
    ): ClassificationResult {
        return calculateMetrics(
            classification.map { stringProvider(it) }.toSet(),
            groundTruth.map { stringProvider(it) }.toSet(),
            confusionMatrixSum
        )
    }

    fun calculateMetrics(
        classification: Set<String>,
        groundTruth: Set<String>,
        confusionMatrixSum: Int?
    ): ClassificationResult
}

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
}
