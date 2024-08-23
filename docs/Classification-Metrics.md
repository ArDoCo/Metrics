The classification metrics calculator is responsible for computing various classification performance metrics based on input classifications and ground truth data.

## Input

1. **Classification**: A set of classified elements.
2. **Ground Truth**: A set representing the actual classification labels for comparison.
3. **String Provider Function (optional)**: A function that converts classification and ground truth elements into string representations for comparison purposes.
4. **Confusion Matrix Sum (optional)**: The sum of the confusion matrix values (true positives, false positives, etc.). Some metrics may not be calculated if this is not provided.

:warning: Classification result entries have to match entries in the ground truth (equals) 

## Supported Metrics

The system calculates a variety of standard classification metrics:

1. **Precision**: Measures the accuracy of the positive predictions.

   $$\text{Precision} = \frac{TP}{TP + FP}$$

   Where:
   - \( TP \) is the number of true positives.
   - \( FP \) is the number of false positives.

2. **Recall**: Also known as sensitivity, recall measures the ability to find all positive instances.

   $$\text{Recall} = \frac{TP}{TP + FN}$$

   Where:
   - \( FN \) is the number of false negatives.

3. **F1-Score**: A harmonic mean of precision and recall, providing a single score that balances both concerns.

   $$F1 = 2 \times \frac{\text{Precision} \times \text{Recall}}{\text{Precision} + \text{Recall}}$$

4. **Accuracy (optional)**: Measures the proportion of correctly predicted instances (if true negatives are provided).

   $$\text{Accuracy} = \frac{TP + TN}{TP + TN + FP + FN}$$

5. **Specificity (optional)**: Also called true negative rate, it measures the proportion of actual negatives that are correctly identified.

   $$\text{Specificity} = \frac{TN}{TN + FP}$$

6. **Phi Coefficient (optional)**: A measure of the degree of association between two binary variables.

   $$\Phi = \frac{TP \times TN - FP \times FN}{\sqrt{(TP + FP)(TP + FN)(TN + FP)(TN + FN)}}$$

7. **Phi Coefficient Max (optional)**: The maximum possible value for the phi coefficient.

8. **Phi Over Phi Max (optional)**: The ratio of the phi coefficient to its maximum possible value.

Each result includes a human-readable format that logs the computed metrics for ease of debugging and verification.


