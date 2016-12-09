import numpy as np
import random
import scipy.stats as ss
import matplotlib.pyplot as plt

# This script finds the proportion of customers who arrive in different time intervals

# minutes before departure for all customers (generating using minutes_before_dep.py)
f = file('min_before_departure_list.txt', 'r+')
arrivalDepart = [float(x) for x in f.read().split(', ')]

# time intervals
t90_70 = 0.0
t70_60 = 0.0
t60_50 = 0.0
t50_40 = 0.0
t40_30 = 0.0
t30_20 = 0.0
t20_0 = 0.0
total = 0.0
for a in arrivalDepart:
    if a<=90.0 and a>70.0:
        t90_70+=1
    if a<=70.0 and a>60.0:
        t70_60+=1.0
    if a<=60.0 and a>50.0:
        t60_50+=1.0
    if a<=50.0 and a>40.0:
        t50_40+=1.0
    if a<=40.0 and a>30.0:
        t40_30+=1.0
    if a<=30.0 and a>20.0:
        t30_20+=1.0
    if a<=20.0 and a>0.0:
        t20_0+=1.0
    total+=1.0

prop = [t90_70/total, t70_60/total, t60_50/total, t50_40/total, t40_30/total, t30_20/total, t20_0/total]
print prop

# spreading 60 customers across time intervals based on proportions
cust = 60
custProp = []
for p in prop:
    custProp.append(int(cust*p))
print custProp

# the length of each time interval
timeInts = [20,10,10,10,10,10,20]

#generate arrivals by evenly distributing across intervals
t = 0.0
Times = []
for i in range(7):
    for c in range(custProp[i]):
        inc = float(timeInts[i])/float(custProp[i])
        t = t+inc
        Times.append(t)

print Times
