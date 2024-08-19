package edu.kit.kastel.mcse.ardoco.metrics.result

interface ClassificationResult {
    val precision: Double
    val recall: Double
    val f1: Double
    val accuracy: Double?
    val specificity: Double?
    val phiCoefficient: Double?
    val phiCoefficientMax: Double?
    val phiOverPhiMax: Double?

    fun prettyPrint()
}
