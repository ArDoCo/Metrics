package edu.kit.kastel.mcse.ardoco.metrics.cli

import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.AggregationClassificationCommand
import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.AggregationRankCommand
import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.ClassificationCommand
import edu.kit.kastel.mcse.ardoco.metrics.cli.commands.RankCommand
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    val parser = ArgParser("ArDoCo Metrics")

    val outputFileOption = parser.option(ArgType.String, shortName = "o", description = "The output file", fullName = "output")
    val classificationCommand = ClassificationCommand(outputFileOption)
    val aggregationClassificationCommand = AggregationClassificationCommand(outputFileOption)
    val rankCommand = RankCommand(outputFileOption)
    val aggregationRankCommand = AggregationRankCommand(outputFileOption)
    parser.subcommands(classificationCommand, aggregationClassificationCommand, rankCommand, aggregationRankCommand)
    parser.parse(args)
}
