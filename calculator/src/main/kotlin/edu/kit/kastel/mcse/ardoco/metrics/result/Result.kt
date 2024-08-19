package edu.kit.kastel.mcse.ardoco.metrics.result

data class Result(
    val classificationResult: ClassificationResult?
) {
    fun prettyPrint() {
        if (classificationResult != null) {
            classificationResult.prettyPrint()
        } else {
            println("No classification result available")
        }
    }
}
