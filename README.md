
1. Java was used for this problem. I have created a jar file (AnomalyDetection.jar) in the root directory, which can be called by run.sh. I also modified the run.sh in the root directory and run_tests.sh in the insight_testsuite folder, so they are now compatible with my java code.

2. I used an external libray for JSON parsing. The library was downloaded from here: http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip

3. The source code is consisted of 7 java code files:
	Anomaly.java: Anomaly is a class to detect if a purchase is anomaly
	DegreeBFS.java: degree-depended breath-first search (DegreeBFS) algorithm to find all friends' purchase
	JsonOutAnomaly.java: Convert Jsonobject to String for output
	Purchase.java: Purchase is a class to represent one purchase record
	ReadStream.java: Read the batch_log and stream_log, and output the purchase record in stream log if it is an anomaly
	TrackedPurchase.java: to find the defined number of purchases from latest to oldest from all friends' purchases
	User.java: User is a class to represent users of the social network at Market-ter Inc.
