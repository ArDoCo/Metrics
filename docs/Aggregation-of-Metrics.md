In addition to calculating individual metrics for classification and ranking tasks, the system supports the **aggregation** of results across multiple classifications or rank-based results. Aggregation methods allow users to compute overall metrics that represent the combined performance of several tasks.

## Aggregation Types

The following **Aggregation Types** are supported for both classification and rank metrics:

1. **Macro Average**: This type of aggregation computes the average of the metrics for each class or query, giving equal weight to each.
   - **Use Case**: Useful when all classes or queries are equally important, regardless of how many instances belong to each class.

2. **Micro Average**: This method aggregates by counting the total true positives, false positives, and false negatives across all classes or queries, then computes the metrics globally.
   - **Use Case**: Useful when classes or queries have an uneven number of instances, and you want to prioritize overall accuracy over individual class performance.

3. **Weighted Average**: In this method, the average is computed with weights, typically proportional to the number of instances in each class or query.
   - **Use Case**: Useful when certain classes or queries are more important and should contribute more to the overall metrics.

## Aggregation for Classification Metrics

The **AggregatedClassificationResult** class aggregates results from multiple classification tasks. It combines metrics like precision, recall, and F1-score across multiple classification results and calculates an overall score using one of the aggregation methods mentioned above.

Key Metrics Aggregated:
- **Precision**
- **Recall**
- **F1-Score**
- **Accuracy (if available)**
- **Specificity (if available)**
- **Phi Coefficient (if available)**
- **Phi Coefficient Max (if available)**
- **Phi Over Phi Max (if available)**

**Example:**
If you perform multiple classification tasks and want a single precision or recall score, the **macro average** would treat each classification equally, while the **weighted average** would account for the number of instances in each task.

## Aggregation for Rank Metrics

The **AggregatedRankMetricsResult** class aggregates results from multiple ranking tasks. It computes an overall **Mean Average Precision (MAP)**, **LAG**, and **AUC** by combining the results of each individual rank task.

Key Metrics Aggregated:
- **Mean Average Precision (MAP)**
- **LAG**
- **AUC (if available)**

**Example:**
For search or ranking tasks, you might aggregate the **MAP** scores of multiple queries to get a single performance measure for the ranking system across all queries.