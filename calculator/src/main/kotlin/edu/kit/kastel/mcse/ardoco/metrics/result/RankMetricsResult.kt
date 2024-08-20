package edu.kit.kastel.mcse.ardoco.metrics.result

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Represents the result of metrics for a rank task.
 */
interface RankMetricsResult {
    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(RankMetricsResult::class.java)
    }

    /** Mean average precision */
    val map: Double

    /** LAG Metric */
    val lag: Double

    /** Prints the result in a human-readable format to the logger. */
    fun prettyPrint() {
        logger.info("Mean Average Precision: $map")
        logger.info("Lag: $lag")
    }
}
