The rank metrics calculator computes performance metrics for systems that provide ranked results, such as search engines or recommendation systems. These metrics are based on the comparison between the provided ranked results and the ground truth data.

## Input

1. **Ranked Results**: A list of sorted lists, where each list represents the ranked results for one query or item (with the most relevant items first).
2. **Ground Truth**: A set of items representing the correct or ideal results for the given queries or items.
3. **String Provider Function**: A function that converts the ranked results and ground truth elements into string representations for comparison purposes.
4. **Relevance-Based Input (optional)**: Contains relevance scores associated with each ranked result. This input is used for relevance-based calculations, allowing the ranking system to incorporate degrees of relevance.

## Supported Metrics

The rank metrics calculator computes the following key metrics:

1. **Mean Average Precision (MAP)**: This metric computes the average precision for each query and then averages those precision values over all queries. It provides a single score that summarizes the quality of the ranked results.

   $$\text{MAP} = \frac{1}{N} \sum_{i=1}^{N} \text{AveragePrecision}(i)$$

   Where:
   - $N$ is the number of queries.
   - $\text{AveragePrecision}(i)$ is the average of the precision scores at each relevant document for query $i$. It is calculated by considering only the positions where relevant items are retrieved and averaging the precision at those points.

   $$\text{AveragePrecision}(i) = \frac{\sum_{r=1}^{|retrieved_i|} (precision_i(r)\times relevant_i(r))}{|relevantLinks_i|}$$

   Where:
   - $|retrieved|$ is the number of retrieved links for a query
   - $r$ is the rank in the produced list
   - $precision(r)$ is the *precision* of the list if truncated after rank $r$
   - $relevant(r)$ is a binary function that determines whether the link at rank $r$ is valid (1) or not (0)
   - $|relevantLinks|$ is the total number of links that are relevant for this query according to the gold standard

2. **LAG**: LAG measures the distance (lag) between the position of relevant items in the ranked results and their ideal positions (i.e., as close to the top as possible). It helps assess how well the system ranks relevant items near the top.

   $$\text{LAG} = \frac{1}{N} \sum_{i=1}^{N} \text{Lag}(i)$$

   Where:
   - $\text{Lag}(i)$ is the average lag for query $i$. 
   
   Lag measures how many incorrect links are retrieved above each correct link. For example, if the relevant item should ideally be at position 1 but is ranked at position 3, the lag for that item is 2. The lag is averaged over all relevant documents for query $i$ to compute $\text{Lag}(i)$.

3. **ROC (Receiver Operating Characteristic) Curve (optional)**

    The **ROC curve** is a graphical representation of a classification model’s performance across different decision thresholds. It plots:

    - **True Positive Rate (TPR)**, or **Recall**, on the **y-axis**: $\text{TPR} = \frac{TP}{TP + FN}$
    where $TP$ is the number of true positives, and $ FN $ is the number of false negatives.

    - **False Positive Rate (FPR)** on the **x-axis**: $\text{FPR} = \frac{FP}{FP + TN}$
    where $FP$ is the number of false positives, and $ TN $ is the number of true negatives.

    Each point on the ROC curve corresponds to a different threshold used by the classifier to distinguish between positive and negative predictions. By adjusting the threshold, the TPR and FPR values change, and the ROC curve shows how well the classifier separates the positive from the negative class.


3. **Area Under Curve (AUC) of the Receiver Operating Characteristic (ROC) (optional)**: AUC measures the ability of the system to discriminate between relevant and non-relevant items. The AUC value ranges from 0 to 1, where 1 indicates perfect discrimination.

   $$\text{AUC} = \int_0^1 \text{TPR}(FPR)\ dFPR$$

   Where $TPR$ is the true positive rate and $ FPR $ is the false positive rate.

