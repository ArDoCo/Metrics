package edu.kit.kastel.mcse.ardoco.metrics.result

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Represents the result of metrics for a classification task.
 */
interface ClassificationResult {
    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(ClassificationResult::class.java)
    }

    /** Precision of the classification. */
    val precision: Double

    /** Recall of the classification. */
    val recall: Double

    /** F1-Score of the classification. */
    val f1: Double

    /** Accuracy of the classification. */
    val accuracy: Double?

    /** Specificity of the classification. */
    val specificity: Double?

    /** Phi coefficient of the classification. */
    val phiCoefficient: Double?

    /** Maximum phi coefficient of the classification. */
    val phiCoefficientMax: Double?

    /** Phi coefficient over maximum phi coefficient of the classification. */
    val phiOverPhiMax: Double?

    /** Prints the result in a human-readable format to the logger. */
    fun prettyPrint() {
        logger.info("Precision: $precision")
        logger.info("Recall: $recall")
        logger.info("F1-Score: $f1")
        if (accuracy != null) logger.info("Accuracy: $accuracy")
        if (specificity != null) logger.info("Specificity: $specificity")
        if (phiCoefficient != null) logger.info("Phi Coefficient: $phiCoefficient")
        if (phiCoefficientMax != null) logger.info("Phi Coefficient Max: $phiCoefficientMax")
        if (phiOverPhiMax != null) logger.info("Phi over Phi Max: $phiOverPhiMax")
    }
}
