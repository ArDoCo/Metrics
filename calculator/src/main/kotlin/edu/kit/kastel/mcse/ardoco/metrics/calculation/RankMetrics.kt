package edu.kit.kastel.mcse.ardoco.metrics.calculation

/**
 * Calculates the average precision for a given ordered list of ranked results and ground truth.
 * // TODO: Information on actual order of list
 * @param similarityList The ranked results for a query.
 * @param groundTruth The ground truth for the query.
 * @return The average precision for the query.
 */
fun calculateAP(
    similarityList: List<String>,
    groundTruth: Set<String>
): Double {
    var relevantLinksAtK = 0
    var ap = 0.0

    // Calculate precision at each k
    for ((index, id) in similarityList.withIndex()) {
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
 * @return The mean average precision for the queries.
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
 * @return The lag for the queries.
 */
fun calculateLAG(
    rankedResults: List<List<String>>,
    groundTruth: Set<String>
): Double {
    var totalRelevantLinks = 0
    var lagSum = 0
    for (rankedResult in rankedResults) {
        var relevantLinksAtIndex = 0
        for ((index, id) in rankedResult.withIndex()) {
            if (groundTruth.contains(id)) {
                relevantLinksAtIndex += 1
                totalRelevantLinks += 1
                lagSum += (index + 1) - relevantLinksAtIndex
            }
        }
    }
    return lagSum.toDouble() / totalRelevantLinks
}
