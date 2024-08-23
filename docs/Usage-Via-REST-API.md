The metrics calculator provides a REST API that allows users to calculate classification and rank metrics by sending requests with their data. The API is built using Spring Boot and offers endpoints for both **classification** and **rank** metrics, as well as aggregation features.

## Base URL

By default, the API runs on port **8080**, and all endpoints are accessible under the base URL:

```
http://localhost:8080/api
```

## API Documentation via Swagger

The REST API provides a **Swagger UI** that allows you to easily explore and test the API endpoints. Swagger generates interactive API documentation and can be accessed from a web browser.

**Swagger URL:**
```
http://localhost:8080/swagger-ui/index.html
```

Through Swagger, you can:
- View all available endpoints.
- See detailed descriptions of the request and response formats.
- Test API calls directly from the browser.

## Endpoints

### 1. Classification Metrics

You can calculate classification metrics by sending data to the classification API. 

**Endpoint:**
```
POST /classification-metrics
```

**Request Body Example:**

```json
{
  "classification": [
    "string"
  ],
  "groundTruth": [
    "string"
  ],
  "confusionMatrixSum": 0
}
```

**Response Example:**

```json
{
  "truePositives": [
    "string"
  ],
  "falsePositives": [
    "string"
  ],
  "falseNegatives": [
    "string"
  ],
  "trueNegatives": 0,
  "precision": 0,
  "recall": 0,
  "f1": 0,
  "accuracy": 0,
  "specificity": 0,
  "phiCoefficient": 0,
  "phiCoefficientMax": 0,
  "phiOverPhiMax": 0
}
```

#### Aggregation of Classification Metrics

You can also aggregate multiple classification results into one by sending the following request:

**Endpoint:**
```
POST /classification-metrics/average
```

**Request Body Example:**

```json
{
  "classificationRequests": [
    {
      "classification": ["A", "B"],
      "groundTruth": ["A", "C"]
    },
    {
      "classification": ["B", "C"],
      "groundTruth": ["C", "D"]
    }
  ],
  "weights": [2, 1]
}
```

**Response Example:**

```json
{
  "classificationResults": [
    {
      "type": "MACRO_AVERAGE",
      "precision": 0,
      "recall": 0,
      "f1": 0,
      "accuracy": 0,
      "specificity": 0,
      "phiCoefficient": 0,
      "phiCoefficientMax": 0,
      "phiOverPhiMax": 0,
      "originalSingleClassificationResults": [
        {
          "truePositives": [
            "string"
          ],
          "falsePositives": [
            "string"
          ],
          "falseNegatives": [
            "string"
          ],
          "trueNegatives": 0,
          "precision": 0,
          "recall": 0,
          "f1": 0,
          "accuracy": 0,
          "specificity": 0,
          "phiCoefficient": 0,
          "phiCoefficientMax": 0,
          "phiOverPhiMax": 0
        }
      ],
      "weights": [
        2, 1
      ]
    }
  ]
}
```

### 2. Rank Metrics

To calculate rank metrics, send your ranked results and ground truth to the rank metrics API.

**Endpoint:**
```
POST /rank-metrics
```

**Request Body Example:**

```json
{
  "rankedResults": [
    ["A", "B", "C"],
    ["B", "A", "D"]
  ],
  "groundTruth": ["A", "B"],
  "rankedRelevances": [[0.9, 0.8, 0.4], [0.7, 0.6, 0.5]],
  "biggerIsMoreSimilar": true
}
```

**Response Example:**

```json
{
  "map": 0.85,
  "lag": 13,
  "auc": 0.92,
  "groundTruthSize": 2
}
```

#### Aggregation of Rank Metrics

You can also aggregate multiple rank metrics using the following endpoint:

**Endpoint:**
```
POST /rank-metrics/average
```

**Request Body Example:**

```json
{
  "rankMetricsRequests": [
    {
      "rankedResults": [["A", "B", "C"], ["B", "A", "D"]],
      "groundTruth": ["A", "B"],
      "rankedRelevances": [[0.9, 0.8, 0.4], [0.7, 0.6, 0.5]],
      "biggerIsMoreSimilar": true
    },
    {
      "rankedResults": [["D", "E", "F"], ["B", "A", "D"]],
      "groundTruth": ["D", "E"],
      "rankedRelevances": [[0.4, 0.8, 0.9], [0.5, 0.6, 0.7]],
      "biggerIsMoreSimilar": false
    }
  ],
  "weights": [2, 1]
}
```

**Response Example:**

```json
{
  "rankResults": [
    {
      "type": "WEIGHTED_AVERAGE",
      "map": 0.8,
      "lag": 12,
      "auc": 0.48,
      "originalRankResults": [
        {
          "map": 0.85,
          "lag": 13,
          "auc": 0.92,
          "groundTruthSize": 2
        },
        {
          "map": 0.55,
          "lag": 25,
          "auc": 0.52,
          "groundTruthSize": 2
        }
      ],
      "weights": [
        2, 1
      ]
    }
  ]
}
```