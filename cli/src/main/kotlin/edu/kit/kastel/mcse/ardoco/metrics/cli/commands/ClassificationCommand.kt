package edu.kit.kastel.mcse.ardoco.metrics.cli.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.SingleNullableOption
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

@OptIn(ExperimentalCli::class)
class ClassificationCommand(private val outputFileOption: SingleNullableOption<String>) : Subcommand("classification", "Calculates classification metrics") {
    private val classificationFileOption by option(ArgType.String, shortName = "c", description = "The classification file", fullName = "classification").required()
    private val groundTruthFileOption by option(ArgType.String, shortName = "g", description = "The ground truth file", fullName = "ground-truth").required()
    private val fileHeaderOption by option(ArgType.Boolean, description = "Whether the files have a header", fullName = "header").default(false)
    private val confusionMatrixSumOption by option(ArgType.Int, shortName = "s", description = "The sum of the confusion matrix", fullName = "sum").default(-1)

    override fun execute() {
        println("Calculating classification metrics")
        val classificationFile = File(classificationFileOption)
        val groundTruthFile = File(groundTruthFileOption)

        if (!classificationFile.exists() || !groundTruthFile.exists()) {
            println("Classification file or ground truth file does not exist")
            return
        }

        val classification = classificationFile.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0).toSet()
        val groundTruth = groundTruthFile.readLines().filter { it.isNotBlank() }.drop(if (fileHeaderOption) 1 else 0).toSet()

        val classificationMetrics = ClassificationMetricsCalculator.Instance

        val result =
            classificationMetrics.calculateMetrics(
                classification,
                groundTruth,
                if (confusionMatrixSumOption < 0) null else confusionMatrixSumOption
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
