package edu.kit.kastel.mcse.ardoco.metrics

data class AggregatedClassificationResult(
    val type: AggregationType,
    val precision: Double,
    val recall: Double,
    val f1: Double,
    val accuracy: Double?,
    val specificity: Double?,
    val phiCoefficient: Double?,
    val phiCoefficientMax: Double?,
    val phiOverPhiMax: Double?,
    val originalClassificationResults: List<ClassificationResult>,
    val weights: List<Int>?
) {
    fun prettyPrint() {
        println("Type: $type")
        println("Precision: $precision")
        println("Recall: $recall")
        println("F1-Score: $f1")
        if (accuracy != null) println("Accuracy: $accuracy")
        if (specificity != null) println("Specificity: $specificity")
        if (phiCoefficient != null) println("Phi Coefficient: $phiCoefficient")
        if (phiCoefficientMax != null) println("Phi Coefficient Max: $phiCoefficientMax")
        if (phiOverPhiMax != null) println("Phi over Phi Max: $phiOverPhiMax")
    }

    constructor(classificationResult: ClassificationResult, type: AggregationType, originalClassificationResults: List<ClassificationResult>, weights: List<Int>?) : this(
        type,
        classificationResult.precision,
        classificationResult.recall,
        classificationResult.f1,
        classificationResult.accuracy,
        classificationResult.specificity,
        classificationResult.phiCoefficient,
        classificationResult.phiCoefficientMax,
        classificationResult.phiOverPhiMax,
        originalClassificationResults,
        weights
    )
}

enum class AggregationType {
    MACRO_AVERAGE,
    WEIGHTED_AVERAGE,
    MICRO_AVERAGE
}
