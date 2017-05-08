# spark-digit-recognizer
Use Spark 2.x to recognize handwritten digits on a standard left-to-right golf scorecard

## Initial Steps
- [X] build a simple decision tree model which will predict handwritten digits from an image (67% accuracy) 
- [X] take a sample image and convert to greyscale 
- [X] take a score from your sample and use that as the dimensions you'll test for in the whole image (120 x 120)  
- [X] test model against sample score
- our 67% accurate model thought our 5 was an 8, maybe we should get a better model first?
- [] use neural net model to improve accuracy
- [X] figure out how to scale your sample's score to 28x28 so the model can interpret it correctly 
- [] iterate over a few rows of your image and see if we can use the probabilities from the model to do OCR   
- [] try to use probabilities to parse out rows of numbers

## Next
- [] hookup to [SwingStats](http://www.swingstats.com)

## Maybe
- [] try to handle top-bottom oriented scorecard

## Questions
- Why does the decision tree model keep predicting 8's?

## TODO
- [] easy way to convert probabilities into array double for writing to CSV

## Sources
- https://www.kaggle.com/c/digit-recognizer
- http://mike.seddon.ca/using-apache-spark-neural-networks-to-recognise-digits/
- https://spark.apache.org/docs/latest/ml-classification-regression.html#multilayer-perceptron-classifier
- http://blog.cloudera.com/blog/2015/10/how-to-index-scanned-pdfs-at-scale-using-fewer-than-50-lines-of-code/
- https://docs.cloud.databricks.com/docs/latest/featured_notebooks/DecisionTrees-Example.html