package edu.kit.kastel.mcse.ardoco.metrics.calculation

/**
 * Calculates the rank metrics for a given set of ranked results and ground truth.
 *
 * @param similarityList The ranked results for each query.
 * @param groundTruth The ground truth for the queries.
 */
fun calculateAP(
    similarityList: List<String>,
    groundTruth: Set<String>
): Double {
    var relevantLinksAtK = 0
    var ap = 0.0

    // Calculate precision at each k
    similarityList.forEachIndexed { index, id ->
        val relevant = if (groundTruth.contains(id)) 1 else 0
        relevantLinksAtK += relevant
        val precisionAtK: Double = relevantLinksAtK.toDouble() / (index + 1)
        ap += precisionAtK * relevant
    }

    ap = if (relevantLinksAtK == 0) 1.0 else ap / relevantLinksAtK

    return ap
}

/**
 * Calculates the mean average precision for a given set of ranked results and ground truth.
 *
 * @param rankedResults The ranked results for each query.
 * @param groundTruth The ground truth for the queries.
 */
fun calculateMAP(
    rankedResults: Collection<List<String>>,
    groundTruth: Set<String>
): Double {
    var map = 0.0
    for (rankedResult in rankedResults) {
        map += calculateAP(rankedResult, groundTruth)
    }

    return map / groundTruth.size
}

/**
 * Calculates the lag for a given set of ranked results and ground truth.
 *
 * @param rankedResults The ranked results for each query.
 * @param groundTruth The ground truth for the queries.
 */
fun calculateLAG(
    rankedResults: List<List<String>>,
    groundTruth: Set<String>
): Double {
    var totalRelevantLinks = 0
    var lagSum = 0
    for (rankedResult in rankedResults) {
        var relevantLinksAtIndex = 0
        rankedResult.forEachIndexed { index, id ->
            if (groundTruth.contains(id)) {
                relevantLinksAtIndex += 1
                totalRelevantLinks += 1
                lagSum += (index + 1) - relevantLinksAtIndex
            }
        }
    }
    return lagSum.toDouble() / totalRelevantLinks
}
