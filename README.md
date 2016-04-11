### MultiLayerMatrix: Visualizing Large Taxonomic Datasets
*MultiLayerMatrix* is a new scalable technique to visualize very large matrices by breaking them into multiple layers: the top layer shows the relationships between different groups of clustered data while each sub-layer shows the relationships between nodes in each group as needed. This process can be applied iteratively to create multiple sub-layers for very large datasets. In the following figure, we illustrate the usefulness of *MultiLayerMatrix* by applying it to a network representing similarity measures between 2,048 characters in the Asteraceae taxonomy, a rich dataset that describes characteristics of species of flowering plants. We also discuss the scalability of our technique by investigating its effectiveness on a large synthetic dataset with 20,000 columns by 20,000 rows that is initially clustered into 50 distinct groups, and that can then be interactively investigated to examine a further level of detail within a selected cluster.
Here is *MultiLayerMatrix*'s [demo video](http://www.cs.uic.edu/~tdang/MultiLayerMatrix/video.mp4).

![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/teaser.png)

####Curation and Management of character clusters
Important visualization tasks supported in *MultiLayerMatrix* include allowing analysts to merge sets of characters that are determined to be identical in a taxonomy and to split a selected set of characters from a group that are determined to be irrelevant. This helps to improve the data quality of the matrix. The following figure shows an example of merging three clusters of characters into one. A leader is recomputed for the new combined cluster. The leader character is the one which has minimum distance (or most similar) to other characters in the cluster. Notice that we can still see the three distinct clusters in the merged matrix on the right. This indicates that the leader algorithm has done a good job in clustering similar characters.

![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/Figure4.png  | width=600)

The next figure shows an example of splitting a cluster of characters into three. The leader for each cluster is also recomputed. 
![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/Figure5.png | width=600)
