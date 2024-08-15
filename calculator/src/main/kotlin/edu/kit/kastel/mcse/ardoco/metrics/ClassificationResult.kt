package edu.kit.kastel.mcse.ardoco.metrics

data class ClassificationResult(
    val tp: Int,
    val fp: Int,
    val fn: Int,
    val tn: Int?,
    val precision: Double,
    val recall: Double,
    val f1: Double,
    // Only if tn is available
    val accuracy: Double?,
    val specificity: Double?,
    val phiCoefficient: Double?,
    val phiCoefficientMax: Double?,
    val phiOverPhiMax: Double?
) {
    fun prettyPrint() {
        println("True Positives: $tp")
        println("False Positives: $fp")
        println("False Negatives: $fn")
        println("True Negatives: ${tn ?: "N/A"}")
        println("Precision: $precision")
        println("Recall: $recall")
        println("F1-Score: $f1")
        if (accuracy != null) println("Accuracy: $accuracy")
        if (specificity != null) println("Specificity: $specificity")
        if (phiCoefficient != null) println("Phi Coefficient: $phiCoefficient")
        if (phiCoefficientMax != null) println("Phi Coefficient Max: $phiCoefficientMax")
        if (phiOverPhiMax != null) println("Phi over Phi Max: $phiOverPhiMax")
    }

    internal fun addToAll(other: ClassificationResult): ClassificationResult {
        if ((tn != null && other.tn == null) || (tn == null && other.tn != null)) {
            throw IllegalArgumentException("Cannot add ClassificationResults with different availability of True Negatives")
        }

        val newTP = tp + other.tp
        val newFP = fp + other.fp
        val newFN = fn + other.fn
        val newTN = tn?.plus(other.tn!!)
        val newPrecision = precision + other.precision
        val newRecall = recall + other.recall
        val newF1 = f1 + other.f1
        val newAccuracy = accuracy?.plus(other.accuracy!!)
        val newSpecificity = specificity?.plus(other.specificity!!)
        val newPhiCoefficient = phiCoefficient?.plus(other.phiCoefficient!!)
        val newPhiCoefficientMax = phiCoefficientMax?.plus(other.phiCoefficientMax!!)
        val newPhiOverPhiMax = phiOverPhiMax?.plus(other.phiOverPhiMax!!)
        return ClassificationResult(newTP, newFP, newFN, newTN, newPrecision, newRecall, newF1, newAccuracy, newSpecificity, newPhiCoefficient, newPhiCoefficientMax, newPhiOverPhiMax)
    }

    internal fun normalizeOnlyMetrics(size: Int): ClassificationResult {
        val newTP = tp
        val newFP = fp
        val newFN = fn
        val newTN = tn
        val newPrecision = precision / size
        val newRecall = recall / size
        val newF1 = f1 / size
        val newAccuracy = accuracy?.div(size)
        val newSpecificity = specificity?.div(size)
        val newPhiCoefficient = phiCoefficient?.div(size)
        val newPhiCoefficientMax = phiCoefficientMax?.div(size)
        val newPhiOverPhiMax = phiOverPhiMax?.div(size)
        return ClassificationResult(newTP, newFP, newFN, newTN, newPrecision, newRecall, newF1, newAccuracy, newSpecificity, newPhiCoefficient, newPhiCoefficientMax, newPhiOverPhiMax)
    }

    internal fun timesOnlyMetrics(weight: Int): ClassificationResult {
        val newTP = tp
        val newFP = fp
        val newFN = fn
        val newTN = tn
        val newPrecision = precision * weight
        val newRecall = recall * weight
        val newF1 = f1 * weight
        val newAccuracy = accuracy?.times(weight)
        val newSpecificity = specificity?.times(weight)
        val newPhiCoefficient = phiCoefficient?.times(weight)
        val newPhiCoefficientMax = phiCoefficientMax?.times(weight)
        val newPhiOverPhiMax = phiOverPhiMax?.times(weight)
        return ClassificationResult(newTP, newFP, newFN, newTN, newPrecision, newRecall, newF1, newAccuracy, newSpecificity, newPhiCoefficient, newPhiCoefficientMax, newPhiOverPhiMax)
    }
}
