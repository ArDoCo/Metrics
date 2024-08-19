package edu.kit.kastel.mcse.ardoco.metrics.result

data class Result(
    val singleClassificationResult: SingleClassificationResult?
) {
    fun prettyPrint() {
        if (singleClassificationResult != null) {
            singleClassificationResult.prettyPrint()
        } else {
            println("No classification result available")
        }
    }
}
