package edu.kit.kastel.mcse.ardoco.metrics.cli.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationResult
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.SingleNullableOption
import kotlinx.cli.Subcommand
import kotlinx.cli.required
import java.io.File

@OptIn(ExperimentalCli::class)
class MultiClassificationCommand(
    private val outputFileOption: SingleNullableOption<String>
) : Subcommand("multi", "Aggregate results of multiple classifications. I.e., Average + WeightedAverage") {
    private val directoryWithResultsOption by option(ArgType.String, shortName = "d", description = "The directory with the classification results").required()

    override fun execute() {
        val directory = File(directoryWithResultsOption)
        if (!directory.isDirectory) {
            println("The provided path is not a directory")
            return
        }
        val oom = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerKotlinModule()
        val results: List<ClassificationResult> = directory.listFiles()?.filter { it.isFile }?.map { oom.readValue(it.inputStream()) } ?: emptyList()
        if (results.isEmpty()) {
            println("No classification results found")
            return
        }
        val classificationMetrics = ClassificationMetricsCalculator.Instance
        val average = classificationMetrics.calculateAverages(results)
        average.forEach { it.prettyPrint() }

        val output = outputFileOption.value
        if (output != null) {
            val outputFile = File(output)
            oom.writeValue(outputFile, average)
        }
    }
}
