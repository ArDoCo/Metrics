The metrics calculator provides a command-line interface (CLI) that allows users to calculate both classification and rank metrics, as well as aggregate the results from multiple inputs.

## Running the CLI

To run the CLI, use the following command:

```bash
java -jar metrics-cli.jar <command> [options]
```

Each command has specific options and input files required to perform the desired calculation.

## Commands

### 1. **Classification Metrics Command**

This command calculates classification metrics for a single classification task.

**Command:**
```
classification
```

**Options:**
- `-c, --classification <file>`: The file containing the classified items.
- `-g, --ground-truth <file>`: The file containing the ground truth items.
- `--header`: (Optional) Indicates that the input files have a header.
- `-s, --sum <Int>`: (Optional) The sum of the confusion matrix.
- `-o, --output <file>`: (Optional) The output file to store the results.


**Example Usage:**
```bash
java -jar metrics-cli.jar classification -c classified.txt -g ground_truth.txt --header -s 5 -o result.json
```

This command reads the classification and ground truth data, computes metrics like precision, recall, F1-score, and saves the result to `result.json` if specified.

### 2. **Rank Metrics Command**

This command calculates rank metrics such as Mean Average Precision (MAP), LAG, and AUC for ranking tasks.

**Command:**
```
rank
```

**Options:**
- `-r, --ranked-list-directory <directory>`: The directory containing ranked list result files for each query.
- `-g, --ground-truth <file>`: The file containing the ground truth items.
- `--header`: (Optional) Indicates that the input files have a header.
- `-rrl, --ranked-relevance-list-Directory <directory>`: (Optional) The directory with ranked relevance score lists.
- `-b, --bigger-is-more-similar <boolean>`: (Optional) Specifies if higher relevance scores indicate more relevance.
- `-o, --output <file>`: (Optional) The output file to store the results.


**Example Usage:**
```bash
java -jar metrics-cli.jar rank -r ranked_results/ -g ground_truth.txt -rrl relevance_scores/ -b true --header -o result.json
```

This command computes the rank metrics based on the provided ranked results, ground truth, and optional relevance scores, and saves the output if specified.

### 3. **Aggregation of Classification Metrics**

This command aggregates multiple classification results into one, calculating an overall precision, recall, and F1-score.

**Command:**
```
aggCl
```

**Options:**
- `-d <directory>`: The directory with the result json files.
- `-o, --output <file>`: (Optional) The output file to store the results.

**Example Usage:**
```bash
java -jar metrics-cli.jar aggCl -d classifiedDir/ -o aggregated_result.json
```

This command aggregates classification results from multiple files and calculates the average performance.

### 4. **Aggregation of Rank Metrics**

This command aggregates multiple rank metrics, calculating the average performance across multiple ranking tasks.

**Command:**
```
aggRnk
```

**Options:**
- `-d <directory>`: The directory with the result json files.
- `-o, --output <file>`: (Optional) The output file to store the results.

**Example Usage:**
```bash
java -jar metrics-cli.jar aggRnk -d classifiedDir/ -o aggregated_result.json
```

This command aggregates rank metrics from multiple directories and computes an overall performance score.
