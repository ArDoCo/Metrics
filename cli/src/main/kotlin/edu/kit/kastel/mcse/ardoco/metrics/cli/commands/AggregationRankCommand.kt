package edu.kit.kastel.mcse.ardoco.metrics.cli.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.result.RankMetricsResult
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.SingleNullableOption
import kotlinx.cli.Subcommand
import kotlinx.cli.required
import java.io.File

@OptIn(ExperimentalCli::class)
class AggregationRankCommand(
    private val outputFileOption: SingleNullableOption<String>
) : Subcommand("aggRnk", "Aggregate results of multiple rank metrics runs. I.e., Macro Average + WeightedAverage") {
    private val directoryWithResultsOption by option(
        ArgType.String,
        shortName = "d",
        description = "The directory with the rank results"
    ).required()

    override fun execute() {
        val directory = File(directoryWithResultsOption)
        if (!directory.isDirectory) {
            println("The provided path is not a directory")
            return
        }
        val oom = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerKotlinModule()
        val results: List<RankMetricsResult> = directory.listFiles()?.filter { it.isFile }?.map { oom.readValue(it.inputStream()) } ?: emptyList()
        if (results.isEmpty()) {
            println("No classification results found")
            return
        }
        val rankMetrics = RankMetricsCalculator.Instance
        val average = rankMetrics.calculateAverages(results)
        average.forEach { it.prettyPrint() }

        val output = outputFileOption.value
        if (output != null) {
            val outputFile = File(output)
            oom.writeValue(outputFile, average)
        }
    }
}
