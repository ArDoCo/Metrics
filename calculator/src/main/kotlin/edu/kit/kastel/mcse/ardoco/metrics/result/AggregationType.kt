package edu.kit.kastel.mcse.ardoco.metrics.result

/**
 * Represents the type of aggregation for classification results.
 */
enum class AggregationType {
    /** Macro average */
    MACRO_AVERAGE,

    /** Weighted average */
    WEIGHTED_AVERAGE,

    /** Micro average */
    MICRO_AVERAGE
}
