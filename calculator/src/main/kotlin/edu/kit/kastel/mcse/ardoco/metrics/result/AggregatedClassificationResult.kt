package edu.kit.kastel.mcse.ardoco.metrics.result

import edu.kit.kastel.mcse.ardoco.metrics.result.ClassificationResult.Companion.logger

/** Represents the aggregation of multiple classification results. */
data class AggregatedClassificationResult(
    /** The type of aggregation */
    val type: AggregationType,
    override val precision: Double,
    override val recall: Double,
    override val f1: Double,
    override val accuracy: Double?,
    override val specificity: Double?,
    override val phiCoefficient: Double?,
    override val phiCoefficientMax: Double?,
    override val phiOverPhiMax: Double?,
    /** The original single classification results that were aggregated */
    val originalSingleClassificationResults: List<SingleClassificationResult<*>>,
    /** The weights of the original single classification results (only if applicable) */
    val weights: List<Int>?
) : ClassificationResult {
    override fun prettyPrint() {
        logger.info("Type: $type")
        super.prettyPrint()
    }

    /**
     * Creates an aggregated classification result.
     *
     * @param metrics The metrics of the aggregation (this information will be copied to this result).
     * @param type The type of aggregation.
     * @param originalSingleClassificationResults The original single classification results that were aggregated.
     * @param weights The weights of the original single classification results (only if applicable).
     */
    constructor(
        metrics: SingleClassificationResult<*>,
        type: AggregationType,
        originalSingleClassificationResults: List<SingleClassificationResult<*>>,
        weights: List<Int>?
    ) : this(
        type,
        metrics.precision,
        metrics.recall,
        metrics.f1,
        metrics.accuracy,
        metrics.specificity,
        metrics.phiCoefficient,
        metrics.phiCoefficientMax,
        metrics.phiOverPhiMax,
        originalSingleClassificationResults,
        weights
    )
}
