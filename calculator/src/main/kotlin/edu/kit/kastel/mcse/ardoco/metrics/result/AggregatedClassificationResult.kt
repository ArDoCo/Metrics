package edu.kit.kastel.mcse.ardoco.metrics.result

data class AggregatedClassificationResult(
    val type: AggregationType,
    override val precision: Double,
    override val recall: Double,
    override val f1: Double,
    override val accuracy: Double?,
    override val specificity: Double?,
    override val phiCoefficient: Double?,
    override val phiCoefficientMax: Double?,
    override val phiOverPhiMax: Double?,
    val originalSingleClassificationResults: List<SingleClassificationResult<*>>,
    val weights: List<Int>?
) : ClassificationResult {
    override fun prettyPrint() {
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

    constructor(
        singleClassificationResult: SingleClassificationResult<*>,
        type: AggregationType,
        originalSingleClassificationResults: List<SingleClassificationResult<*>>,
        weights: List<Int>?
    ) : this(
        type,
        singleClassificationResult.precision,
        singleClassificationResult.recall,
        singleClassificationResult.f1,
        singleClassificationResult.accuracy,
        singleClassificationResult.specificity,
        singleClassificationResult.phiCoefficient,
        singleClassificationResult.phiCoefficientMax,
        singleClassificationResult.phiOverPhiMax,
        originalSingleClassificationResults,
        weights
    )
}
