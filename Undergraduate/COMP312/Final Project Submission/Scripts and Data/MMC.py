# M/M/3

from SimPy.Simulation import *
import random
import numpy
import math
import matplotlib.pyplot as pl


## Useful extras ----------
def conf(L):
    """confidence interval"""
    lower = numpy.mean(L) - 1.96*numpy.std(L)/math.sqrt(len(L))
    upper = numpy.mean(L) + 1.96*numpy.std(L)/math.sqrt(len(L))
    return (lower,upper)

## Model ----------
class Source(Process):
    """generate random arrivals"""
    def run(self, N):
        for i in range(N):
            a = Arrival(str(i))
            activate(a, a.run())
            t = random.expovariate(1.0/54.7985931569)
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
        t = random.expovariate(1.0/81.4455727819)
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
    W = G.delaymon.mean()
    L = G.numbermon.timeAverage()
    LQ = G.server.waitMon.timeAverage()
    lambEff = L/W
    WQ = LQ/lambEff
    U = G.server.actMon.timeAverage()/c
    cust = G.numbermon.yseries()
    time = G.numbermon.tseries()
    return(W, L, WQ, LQ, U, cust, time)

## Experiment ----------


allW = []
allL = []
allWQ = []
allLQ = []
allU = []

for k in range(1000):
    seed = 127*k
    result = model(c=3, N=60,
                  maxtime=7200, rvseed=seed)
    allW.append(result[0])
    allL.append(result[1])
    allWQ.append(result[2])
    allLQ.append(result[3])
    allU.append(result[4])

print ""
print "Estimate of W:",numpy.mean(allW)
print "Conf int of W:",conf(allW)

print ""
print "Estimate of L:",numpy.mean(allL)
print "Conf int of L:",conf(allL)

print ""
print "Estimate of WQ:",numpy.mean(allWQ)
print "Conf int of WQ:",conf(allWQ)

print ""
print "Estimate of LQ:",numpy.mean(allLQ)
print "Conf int of LQ:",conf(allLQ)

print ""
print "Estimate of U:",numpy.mean(allU)
print "Conf int of U:",conf(allU)

# generates a plot of customers over time
fig = pl.figure()
pl.plot(result[6],result[5],lw=2,label = "Customers in system")
fig.suptitle('Numbers in system over session- mmc')
pl.legend(loc='upper right')
pl.xlabel("Time(s)")
pl.ylabel("Customers over time")
pl.show()
