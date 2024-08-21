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
        ArgType.String, shortName = "r", description = "The directory of the ranked list files", fullName = "ranked-list-directory"
    ).required()
    private val groundTruthFileOption by option(
        ArgType.String, shortName = "g", description = "The ground truth file", fullName = "ground-truth"
    ).required()
    private val fileHeaderOption by option(ArgType.Boolean, description = "Whether the files have a header", fullName = "header").default(false)
    private val rankedRelevanceListDirectoryOption by option(
        ArgType.String,
        shortName = "rrl",
        description = "The directory of the ranked relevance list files",
        fullName = "ranked-relevance-list-directory"
    )
    private val biggerIsMoreSimilar by option(
        ArgType.String, shortName = "b", description = "Whether the relevance scores are more similar if bigger", fullName = "bigger-is-more-similar"
    )


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
        val rankedResults: List<List<String>> = rankedListDirectory.listFiles()?.filter { file ->
            file.isFile
        }?.map { file -> file.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0) } ?: emptyList()
        if (rankedResults.isEmpty()) {
            println("No classification results found")
            return
        }
        val groundTruth = groundTruthFile.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0).toSet()

        var relevanceBasedInput: RankMetricsCalculator.RelevanceBasedInput<Double>? = null
        if (rankedRelevanceListDirectoryOption != null) {
            val rankedRelevanceListDirectory = File(rankedRelevanceListDirectoryOption!!)
            if (!rankedRelevanceListDirectory.exists() || !rankedRelevanceListDirectory.isDirectory) {
                println("The directory of the ranked relevance list files does not exist or is not a directory")
                return
            }
            val rankedRelevances = rankedRelevanceListDirectory.listFiles()?.filter { file ->
                file.isFile
            }?.map { file -> file.readLines().filter { it.isNotBlank() }.map { it.toDouble() }.drop(if (fileHeaderOption) 1 else 0) } ?: emptyList()
            if (rankedRelevances.isEmpty()) {
                println("No relevance scores found")
                return
            }
            if (biggerIsMoreSimilar == null) {
                throw IllegalArgumentException("ranked relevances and bigger is more similar can only occur together")
            }
            relevanceBasedInput = if (biggerIsMoreSimilar != null) RankMetricsCalculator.RelevanceBasedInput(
                rankedRelevances, { it }, biggerIsMoreSimilar.toBoolean()
            ) else null
        }
        val rankMetrics = RankMetricsCalculator.Instance

        val result = rankMetrics.calculateMetrics(
            rankedResults, groundTruth, relevanceBasedInput
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
