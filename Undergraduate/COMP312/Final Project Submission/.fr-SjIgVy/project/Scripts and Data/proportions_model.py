# D/G/3- proportions model

from SimPy.Simulation import *
import random
import numpy
import math
import matplotlib.pyplot as pl

# this script simulates a D/G/3 model based on proportions

f = file('min_before_departure_list.txt', 'r+')
arrivalDepart = [float(x) for x in f.read().split(', ')]

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


cust = 65
custProp = []
for p in prop:
    custProp.append(int(cust*p))

timeInts = [20,10,10,10,10,10,20]

#generate interarrivals

Times = []
for i in range(7):
    for c in range(custProp[i]):
        inc = float(timeInts[i])/float(custProp[i])*60.0
        Times.append(inc)


## Useful extras ----------
def conf(L):
    """confidence interval"""
    lower = numpy.mean(L) - 1.96*numpy.std(L)/math.sqrt(len(L))
    upper = numpy.mean(L) + 1.96*numpy.std(L)/math.sqrt(len(L))
    return (lower,upper)

## Model ----------
class Source(Process):
    """generate random arrivals"""
    n = 0
    def run(self, N):
        for i in range(N):
            a = Arrival(str(i))
            activate(a, a.run())
            t = Times[Source.n]
            Source.n+=1
            yield hold, self, t

class Arrival(Process):
    n = 0

    """an arrival"""
    def run(self):
        arrivetime = now()
        G.numbermon.observe(Arrival.n)
        Arrival.n += 1
        G.numbermon.observe(Arrival.n)
        yield request, self, G.server
        t = random.gammavariate(1.37508883625,59.2293360366)
        yield hold, self, t
        yield release, self, G.server
        delay = now()-arrivetime
        G.delaymon.observe(delay)
        G.numbermon.observe(Arrival.n)
        Arrival.n -= 1
        G.numbermon.observe(Arrival.n)



class G:
    server = 'dummy'
    delaymon = 'Monitor'
    numbermon = 'Monitor'

def model(c, N, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.server = Resource(c,monitored = True)
    G.delaymon = Monitor()
    G.numbermon = Monitor()

    # simulate
    s = Source('Source')
    activate(s, s.run(N))
    simulate(until=maxtime)

    # gather performance measures
    cust = G.numbermon.yseries()
    time = G.numbermon.tseries()
    
    return(cust, time)

## Experiment ----------

# Create the distrubutions
finTime = []

for c in [3]:
    for k in range(1):
        seed =222
        result = model(c, N=58,
                          maxtime=7200, rvseed=seed)
        finTime.append(max(result[1]))

    print ""
    print "severs = ", str(c)
    print "Estimate of finish:",numpy.mean(finTime)
    print "Conf int of finish time:",conf(finTime)

fig = pl.figure()
pl.plot(result[1],result[0],lw=2,label = "Customers over time")
fig.suptitle('Numbers in system over session- ggc')
pl.legend(loc='upper right')
pl.xlabel("Time(s)")
pl.ylabel("Customers over time")
pl.show()

