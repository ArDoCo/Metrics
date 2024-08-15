package edu.kit.kastel.mcse.ardoco.metrics.cli

import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.ClassificationCommand
import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.MultiClassificationCommand
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    val parser = ArgParser("ArDoCo Metrics")

    val outputFileOption = parser.option(ArgType.String, shortName = "o", description = "The output file", fullName = "output")
    val classification = ClassificationCommand(outputFileOption)
    val multiClassification = MultiClassificationCommand(outputFileOption)
    parser.subcommands(classification, multiClassification)
    parser.parse(args)
}
