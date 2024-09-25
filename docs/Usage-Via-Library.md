The project can be integrated into your own system as a library to calculate both classification and rank metrics. Follow the steps below to use the metrics calculator within your code.

## 1. Adding the Dependency

To use this project as a Maven dependency, you need to include the following dependency in your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.ardoco</groupId>
    <artifactId>metrics</artifactId>
    <version>${revision}</version>
</dependency>
```

Make sure to replace `${revision}` with the appropriate version number of the library. You can find the version from the repository or Maven Central.

### Optional: Snapshot Repository

If you are using a snapshot version of the library (like `0.1.1-SNAPSHOT`), you will need to include the **snapshot repository** configuration in your `pom.xml` file. This enables Maven to fetch the latest snapshot build:

```xml
<repositories>
    <repository>
        <id>mavenSnapshot</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

## 2. Importing the Metrics Calculator

Once the library is included, you can import and use the **ClassificationMetricsCalculator** and **RankMetricsCalculator** in your project.

### Example for Classification Metrics:

```kotlin
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult

fun main() {
    val classification = setOf("A", "B", "C")
    val groundTruth = setOf("A", "C", "D")

    // Use the ClassificationMetricsCalculator to calculate metrics
    val calculator = ClassificationMetricsCalculator.Instance
    val result: SingleClassificationResult<String> = calculator.calculateMetrics(
        classification = classification,
        groundTruth = groundTruth,
        confusionMatrixSum = null
    )

    result.prettyPrint()  // Logs precision, recall, F1 score, etc.
}
```

```java
import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator;
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleClassificationResult;

import java.util.Set;

public class ClassificationExample {
    public static void main(String[] args) {
        Set<String> classification = Set.of("A", "B", "C");
        Set<String> groundTruth = Set.of("A", "C", "D");

        // Use the ClassificationMetricsCalculator to calculate metrics
        ClassificationMetricsCalculator calculator = ClassificationMetricsCalculator.getInstance();
        SingleClassificationResult<String> result = calculator.calculateMetrics(
                classification,
                groundTruth,
                null  // Confusion matrix sum (optional)
        );

        // Print the result, which includes precision, recall, F1 score, etc.
        result.prettyPrint();
    }
}
```

### Example for Rank Metrics:

```kotlin
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult

fun main() {
    val rankedResults = listOf(
        listOf("A", "B", "C"),  // Ranked results for query 1
        listOf("B", "A", "D")   // Ranked results for query 2
    )
    val groundTruth = setOf("A", "B")

    // Use the RankMetricsCalculator to calculate metrics
    val calculator = RankMetricsCalculator.Instance
    val result: SingleRankMetricsResult = calculator.calculateMetrics(
        rankedResults = rankedResults,
        groundTruth = groundTruth,
        relevanceBasedInput = null
    )

    result.prettyPrint()  // Logs MAP, LAG, AUC, etc.
}
```

```java
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator;
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult;

import java.util.List;
import java.util.Set;

public class RankMetricsExample {
    public static void main(String[] args) {
        List<List<String>> rankedResults = List.of(
                List.of("A", "B", "C"),  // Ranked results for query 1
                List.of("B", "A", "D")   // Ranked results for query 2
        );
        Set<String> groundTruth = Set.of("A", "B");

        // Use the RankMetricsCalculator to calculate metrics
        RankMetricsCalculator calculator = RankMetricsCalculator.getInstance();
        SingleRankMetricsResult result = calculator.calculateMetrics(
                rankedResults,
                groundTruth,
                null  // Relevance-based input (optional)
        );

        // Print the result, which includes MAP, LAG, AUC, etc.
        result.prettyPrint();
    }
}
```

## 3. Customizing the Calculations

Both calculators (classification and rank metrics) provide customizable inputs like:
- **String Provider**: Allows you to specify how the elements in your classification or ranking are converted to strings.
- **Relevance-Based Input (optional for rank metrics)**: Allows you to input additional relevance scores if needed for calculating metrics like AUC.

### Example for Using `RelevanceBasedInput` with Rank Metrics

The **`RelevanceBasedInput`** class allows you to pass additional relevance scores for ranked results when calculating rank metrics like **AUC**. This relevance-based information gives more context to the ranking system, allowing it to factor in how relevant each item is.

#### Code Example:

```kotlin
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult
import edu.kit.kastel.mcse.ardoco.metrics.internal.RelevanceBasedInput

fun main() {
    // Example ranked results for two queries
    val rankedResults = listOf(
        listOf("A", "B", "C"),  // Ranked results for query 1
        listOf("D", "A", "B")   // Ranked results for query 2
    )

    // Ground truth for relevance (the most relevant results)
    val groundTruth = setOf("A", "B")

    // Relevance scores associated with the ranked results
    val rankedRelevances = listOf(
        listOf(0.9, 0.8, 0.4),  // Relevance scores for query 1
        listOf(0.7, 0.6, 0.5)   // Relevance scores for query 2
    )

    // Creating the RelevanceBasedInput object
    val relevanceInput = RelevanceBasedInput(
        rankedRelevances = rankedRelevances,   // Relevance scores for ranked results
        doubleProvider = { it },               // Function to provide the relevance value (identity function in this case)
        biggerIsMoreSimilar = true             // Whether higher values mean more relevance
    )

    // Use the RankMetricsCalculator to calculate metrics
    val calculator = RankMetricsCalculator.Instance
    val result: SingleRankMetricsResult = calculator.calculateMetrics(
        rankedResults = rankedResults,
        groundTruth = groundTruth,
        relevanceBasedInput = relevanceInput
    )

    // Print the calculated rank metrics (MAP, LAG, AUC, etc.)
    result.prettyPrint()
}
```

```
import edu.kit.kastel.mcse.ardoco.metrics.RankMetricsCalculator;
import edu.kit.kastel.mcse.ardoco.metrics.result.SingleRankMetricsResult;
import edu.kit.kastel.mcse.ardoco.metrics.internal.RelevanceBasedInput;

import java.util.List;
import java.util.Set;

public class RankMetricsWithRelevanceExample {
    public static void main(String[] args) {
        // Example ranked results for two queries
        List<List<String>> rankedResults = List.of(
            List.of("A", "B", "C"),  // Ranked results for query 1
            List.of("D", "A", "B")   // Ranked results for query 2
        );

        // Ground truth for relevance (the most relevant results)
        Set<String> groundTruth = Set.of("A", "B");

        // Relevance scores associated with the ranked results
        List<List<Double>> rankedRelevances = List.of(
            List.of(0.9, 0.8, 0.4),  // Relevance scores for query 1
            List.of(0.7, 0.6, 0.5)   // Relevance scores for query 2
        );

        // Creating the RelevanceBasedInput object
        RelevanceBasedInput relevanceInput = new RelevanceBasedInput(
            rankedRelevances,      // Relevance scores for ranked results
            Double::valueOf,       // Function to provide the relevance value (identity function in this case)
            true                   // Whether higher values mean more relevance
        );

        // Use the RankMetricsCalculator to calculate metrics
        RankMetricsCalculator calculator = RankMetricsCalculator.getInstance();
        SingleRankMetricsResult result = calculator.calculateMetrics(
            rankedResults,
            groundTruth,
            relevanceInput  // Provide relevance-based input
        );

        // Print the calculated rank metrics (MAP, LAG, AUC, etc.)
        result.prettyPrint();
    }
}
```

#### Explanation:
1. **Ranked Results**: A list of lists where each list represents ranked items for a query.
   - Query 1: $ ["A", "B", "C"] $
   - Query 2: $ ["D", "A", "B"] $

2. **Ground Truth**: The correct (most relevant) items, which are $ ["A", "B"] $.

3. **Relevance Scores**: The relevance values for the ranked results:
   - Query 1: $ [0.9, 0.8, 0.4] $ – Higher scores indicate more relevant items.
   - Query 2: $ [0.7, 0.6, 0.5] $.

4. **Relevance-Based Input**: This structure provides the calculator with the relevance scores and indicates that higher values represent more relevance (i.e., `biggerIsMoreSimilar = true`).

#### Customization:
- You can modify the `doubleProvider` function to convert any complex structure (such as custom objects) into a numeric relevance score.
- The `biggerIsMoreSimilar` flag can be set to `false` if lower values indicate more relevance (e.g., in a ranking where 1st place is more relevant than 10th place).

## 4. Aggregation of Results

To aggregate multiple classification or ranking results, you can utilize the respective aggregation methods provided by the library. For more details, refer to the [Aggregation](Aggregation-of-Metrics) section.
