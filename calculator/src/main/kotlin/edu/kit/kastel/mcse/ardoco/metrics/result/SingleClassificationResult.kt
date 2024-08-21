package edu.kit.kastel.mcse.ardoco.metrics.result

import edu.kit.kastel.mcse.ardoco.metrics.result.ClassificationResult.Companion.logger

/**
 * Represents the result of metrics for one classification task.
 * @param T the type of classified elements
 */
data class SingleClassificationResult<T>(
    /** The true positives */
    val tp: Set<T>,
    /** The false positives */
    val fp: Set<T>,
    /** The false negatives */
    val fn: Set<T>,
    /** The true negatives. If not available, this is null. */
    val tn: Int?,
    override val precision: Double,
    override val recall: Double,
    override val f1: Double,
    // Only if tn is available
    override val accuracy: Double?,
    override val specificity: Double?,
    override val phiCoefficient: Double?,
    override val phiCoefficientMax: Double?,
    override val phiOverPhiMax: Double?
) : ClassificationResult {
    override fun prettyPrint() {
        logger.info("True Positives: ${tp.size}")
        logger.info("False Positives: ${fp.size}")
        logger.info("False Negatives: ${fn.size}")
        logger.info("True Negatives: ${tn ?: "N/A"}")
        super.prettyPrint()
    }
}
