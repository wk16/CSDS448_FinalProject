import numpy as np
from sklearn import tree


benignTrainFile = open("/Users/will/Desktop/csvs/benignTrain.csv")
malwareTrainFile = open("/Users/will/Desktop/csvs/MalwareTrain.csv")
ransomTrainFile = open("/Users/will/Desktop/csvs/ransomwareTrain.csv")
benignTrainList = np.loadtxt(benignTrainFile, delimiter=",")
malwareTrainList = np.loadtxt(malwareTrainFile, delimiter=",")
ransomTrainList = np.loadtxt(ransomTrainFile, delimiter=",")
permissionList = np.vstack([benignTrainList, malwareTrainList, ransomTrainList])

benignClassifications = [0]*len(benignTrainList)
malwareClassifications = [1]*len(malwareTrainList)
ransomClassifications = [2]*len(ransomTrainList)
classifications = benignClassifications + malwareClassifications + ransomClassifications

tool = tree.DecisionTreeClassifier()
tool = tool.fit(permissionList, classifications)
# print(classifications)

# benignTestFile = open("/Users/will/Desktop/csvs/benignTest.csv")
malwareTestFile = open("/Users/will/Desktop/SMSmalware.csv")
# ransomTestFile = open("/Users/will/Desktop/SMSmalware.csv")
# benignTestList = np.loadtxt(benignTestFile, delimiter=",")
malwareTestList = np.loadtxt(malwareTestFile, delimiter=",")
# ransomTestList = np.loadtxt(ransomTestFile, delimiter=",")

# benignTestResults = tool.predict(benignTestList)
malwareTestResults = tool.predict(malwareTestList)
# ransomTestResults = tool.predict(ransomTestList)

correct = 0
falseMalPos = 0
falseRanPos = 0
falseMalNeg = 0
falseRanNeg = 0
misclass = 0
# for i in benignTestResults:
#     if i == 0:
#         correct += 1
#     if i == 1:
#         falseMalPos += 1
#     if i == 2:
#         falseRanPos += 1
#
for i in malwareTestResults:
    if i == 0:
        falseMalNeg += 1
    if i == 1:
        correct += 1
    if i == 2:
        misclass += 1
#
# for i in ransomTestResults:
#     if i == 0:
#         falseRanNeg += 1
#     if i == 1:
#         misclass += 1
#     if i == 2:
#         correct += 1
#
# print("\nDATASETS")
# print("Total training set size: " + str(len(permissionList)))
# print("Benign training set size: " + str(len(benignTrainList)))
# print("Malware training set size: " + str(len(malwareTrainList)))
# print("Ransomware training set size: " + str(len(ransomTrainList)))
# print("Total testing set size: " + str(len(benignTestList)+len(malwareTestList)+len(ransomTestList)))
# print("Benign testing set size: " + str(len(benignTestList)))
# print("Malware testing set size: " + str(len(malwareTestList)))
# print("Ransomware testing set size: " + str(len(ransomTestList)))
# print("\nRESULTS")
# print(benignTestResults)
# print("\n")
print(malwareTestResults)
# print("\n")
# print(ransomTestResults)
# print("\nRESULT ANALYSIS")
# print("Malware false positive: " + str(falseMalPos/len(benignTestList)))
# print("Ransomware false positive: " + str(falseRanPos/len(benignTestList)))
# print("Total false positive: " + str((falseMalPos + falseRanPos)/len(benignTestList)))
print("Malware false negative: " + str(falseMalNeg/len(malwareTestList)))
# print("Ransomware false negative: " + str(falseRanNeg/len(ransomTestList)))
# print("Total false negative: " + str((falseMalNeg + falseRanNeg)/(len(malwareTestList) +len(ransomTestList))))
# print("Malware/Ransomware misclassifications: " + str(misclass/(len(ransomTestList)+len(malwareTestList))))
# print("Total accuracy: " + str(correct/(len(benignTestList)+len(malwareTestList)+len(ransomTestList))))