package edu.kit.kastel.mcse.ardoco.metrics.result

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
}
