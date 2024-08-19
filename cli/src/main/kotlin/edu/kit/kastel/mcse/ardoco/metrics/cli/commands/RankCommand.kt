package edu.kit.kastel.mcse.ardoco.metrics.cli.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.SingleNullableOption
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

@OptIn(ExperimentalCli::class)
class RankCommand(private val outputFileOption: SingleNullableOption<String>) : Subcommand("rank", "Calculates rank metrics") {
    private val rankedListDirectoryOption by option(
        ArgType.String,
        shortName = "r",
        description = "The directory of the ranked list files",
        fullName = "ranked-list-directory"
    ).required()
    private val groundTruthFileOption by option(
        ArgType.String,
        shortName = "g",
        description = "The ground truth file",
        fullName = "ground-truth"
    ).required()
    private val fileHeaderOption by option(ArgType.Boolean, description = "Whether the files have a header", fullName = "header").default(false)

    override fun execute() {
        println("Calculating rank metrics")
        val rankedListDirectory = File(rankedListDirectoryOption)
        val groundTruthFile = File(groundTruthFileOption)

        if (!rankedListDirectory.exists() || !groundTruthFile.exists()) {
            println("The directory of the ranked list files or ground truth file does not exist")
            return
        }
        if (!rankedListDirectory.isDirectory) {
            println("The provided path is not a directory")
            return
        }
        val rankedResults: List<List<String>> =
            rankedListDirectory.listFiles()?.filter {
                    file ->
                file.isFile
            }?.map { file -> file.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0) } ?: emptyList()
        if (rankedResults.isEmpty()) {
            println("No classification results found")
            return
        }
        val groundTruth = groundTruthFile.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0).toSet()

        val rankMetrics = RankMetricsCalculator.Instance

        val result =
            rankMetrics.calculateMetrics(
                rankedResults,
                groundTruth
            )
        result.prettyPrint()

        val output = outputFileOption.value
        if (output != null) {
            val outputFile = File(output)
            val oom = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerKotlinModule()
            oom.writeValue(outputFile, result)
        }
    }
}
