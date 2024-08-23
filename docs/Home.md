Welcome to the **ArDoCo Metrics Calculator** project! This tool provides functionality to calculate and aggregate **classification** and **rank metrics** for various machine learning and ranking tasks.

This Wiki contains all the necessary information to use the **ArDoCo Metrics Calculator** via multiple interfaces, including a library, REST API, and command-line interface (CLI).


## 1. Classification Metrics

This section provides detailed information about how to calculate **classification metrics** such as precision, recall, F1-score, and more. The classification metrics are essential for evaluating the performance of classification models by comparing the predicted results with the ground truth.

[Read more about Classification Metrics](Classification-Metrics)



## 2. Rank Metrics

The rank metrics module helps you calculate metrics for ranked results, such as **Mean Average Precision (MAP)**, **LAG**, and **AUC**. These metrics are useful for evaluating ranking systems, search engines, or recommendation systems.

[Read more about Rank Metrics](Rank-Metrics)



## 3. Aggregation of Metrics

Aggregation allows you to compute an overall metric from multiple classification or ranking tasks. This can be useful when you want to combine results from several tasks to get a single evaluation score.

[Read more about Aggregation of Metrics](Aggregation-of-Metrics)


## 4. Usage

### 4.1 Usage via Library

The **ArDoCo Metrics Calculator** can be integrated into your project as a library. This section provides instructions for adding the project as a Maven dependency and examples of how to calculate metrics programmatically.

[Read more about Usage via Library](Usage-Via-Library)



### 4.2 Usage via REST API

The project offers a REST API for calculating metrics. You can send HTTP requests to the API to compute both classification and rank metrics, as well as aggregate results across tasks. Swagger documentation is provided for easy testing and interaction.

[Read more about Usage via REST API](Usage-Via-REST-API)



### 4.3 Usage via CLI

For users who prefer using a command-line interface, the project offers CLI commands for calculating and aggregating metrics. This section provides detailed instructions and examples on how to use the CLI for different tasks.

[Read more about Usage via CLI](Usage-Via-CLI)
