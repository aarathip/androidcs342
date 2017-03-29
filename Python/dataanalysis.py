import pandas as pd
import numpy as np
import csv
from datetime import datetime 
from sklearn.ensemble import RandomForestClassifier
import itertools
from sklearn.cross_validation import cross_val_score
from sklearn import svm

df=pd.read_csv('motion.txt')
df.columns = ["time", "x", "y", "z", "label"]


# Method to convert string to python timestamp 
# def str_to_timestamp(s):
#     d = datetime.strptime(s.strip(), '%m/%d/%Y %H:%M:%S')
#     return pd.Timestamp(d)

# Another way to read into a dataframe, so you can exclude the time column
# vals={}
# xvals=[]
# yvals=[]
# zvals=[]
# labels=[]
# times=[]
# with open('motion.txt', 'rt') as csvfile:
#     vals = csv.reader(csvfile, delimiter=',')
#     for row in vals:
# #         print(row)
#         xvals.append(row[1])
#         yvals.append(row[2])
#         zvals.append(row[3])
#         labels.append(row[4])
# dict={}
# dict["x"] = xvals
# dict["y"] = yvals
# dict["z"] = zvals
# dict["label"]=labels
# df=pd.DataFrame.from_dict(dict, orient="columns")

# Use a Random Forest classifier
print("****************** Random Forest *************\n")
frames = [df["x"], df["y"], df["z"]]
X = pd.concat(frames, axis=1)
Y = df["label"]

smean=[]
sstd=[]

clf = RandomForestClassifier(n_estimators=10)
clf.fit(X,Y)

np.random.seed(0)
clf = RandomForestClassifier(n_estimators=10, max_depth=None, min_samples_split=1, random_state=0)
scores = cross_val_score(clf, X, Y, cv=10)
print(scores)
smean.append(scores.mean())
sstd.append(scores.std())
print(str(scores.mean())+","+str(scores.std())+"\n")

# Use a SVM
scores=[]
print("****************** SVM *************\n")
for num in range(1,10):
    X_test = X[num*100:num*100+4000]
    Y_test = Y[num*100:num*100+4000]
    #print(len(X_test), len(Y_test))
    
    X_train = [X[:num*100], X[num*100+4000:]]
    X_train = pd.concat(X_train)
    
    Y_train = [Y[:num*100],Y[num*100+4000:]]
    Y_train = pd.concat(Y_train)
    #print(len(X_train), len(Y_train))
    
    #print(len(X_test) + len(X_train))
      
    clf = svm.SVC(kernel='linear', C=1).fit(X_train, Y_train)
    score=clf.score(X_test, Y_test)
    print(score)
    scores.append(score)

scores=np.array(scores)
print(str(scores.mean()), str(scores.std()) + "\n")
