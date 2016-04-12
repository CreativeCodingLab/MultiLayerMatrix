### MultiLayerMatrix: Visualizing Large Taxonomic Datasets

Here is *MultiLayerMatrix*'s [demo video](http://www.cs.uic.edu/~tdang/MultiLayerMatrix/video.mp4).

*MultiLayerMatrix* is a new scalable technique to visualize very large matrices by breaking them into multiple layers: the top layer shows the relationships between different groups of clustered data while each sub-layer shows the relationships between nodes in each group as needed. This process can be applied iteratively to create multiple sub-layers for very large datasets. In the following figure, we illustrate the usefulness of *MultiLayerMatrix* by applying it to a network representing similarity measures between 2,048 characters in the Asteraceae taxonomy, a rich dataset that describes characteristics of species of flowering plants. Visualizing 2048 nodes in a regular adjacency matrix (left) and in a *MultiLayerMatrix* of two layers: The middle panel shows the first layer, and the right panel shows an example of the second layer, which is shown when users select a cluster in the first layer. Green indicates similar characters while red highlights dissimilarity.

![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/teaser.png)

####Curation and Management of character clusters
Important visualization tasks supported in *MultiLayerMatrix* include allowing analysts to merge sets of characters that are determined to be identical in a taxonomy and to split a selected set of characters from a group that are determined to be irrelevant. This helps to improve the data quality of the matrix. The following figure shows an example of merging three clusters of characters into one. A leader is recomputed for the new combined cluster. The leader character is the one which has minimum distance (or most similar) to other characters in the cluster. Notice that we can still see the three distinct clusters in the merged matrix on the right. This indicates that the leader algorithm has done a good job in clustering similar characters.

![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/Figure4.png)

The next figure shows an example of splitting a cluster of characters into three. The leader for each cluster is also recomputed. 
![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/Figure5.png)

####Pattern discovery and hypothesis generation
Given one taxonomy with associated characters, analysts would like to zoom into or highlight the branches with certain characters. This feature is particularly interesting to educators and can be used in museums or classrooms as a teaching tool. 

*MultiLayerMatrix* allows users to select a particular branch in the taxonomy and display related characters. The related characters are defined as the characters which contain some data in the taxon-by-character table within the selected branch, such as a tribe, a genus, and a species. The next figure(a) shows the structure of our input taxonomy, the Asteraceae family. This family contains 10 tribes (in the first column), 137 genera (in the second column), and 537 species (in the third column). The links in this taxonomy are color-encoded by tribe. The thickness of the links are relative to the number of taxa belonging to these branches. Genera (second column) and species (last column) are ordered based on the tribes that they belong to. 
The figure(b) shows an example of selecting a particular species, Californica. As depicted, the Californica species belongs to 4 different genera (Artemisia, Malacothrix, Rafinesquia, and Trixis) which come from 3 different tribes (Anthemideae, Cichorieae, and Mutisieae). 
Taxonomic names in biology can be complex. At some rank, for example, family, one word name is enough. 
At sub-ranks, such as tribe or species (sub-species, variety etc.), a binomial naming system is used. For example, a species name has two parts: its genus and its specific epithet. It is not unusual for a specific epithet to be shared by many genera. 

![ScreenShot](https://github.com/CreativeCodingLab/MultiLayerMatrix/blob/master/figures/Figure6ab.png)
