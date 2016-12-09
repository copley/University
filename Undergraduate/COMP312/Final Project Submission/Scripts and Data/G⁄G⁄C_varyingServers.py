
# G/G/C

from SimPy.Simulation import *
import random
import numpy
import math
import matplotlib.pyplot as pl


# This script computes the time taken to process a given number of customers with different numbers of servers

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
            t = random.gammavariate(0.643186078892,85.1986853693)
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

for c in [2,3]:
    for k in range(1000):
        seed = 123*k
        result = model(c, N=60,
                          maxtime=7200, rvseed=seed)
        finTime.append(max(result[1]))

    print ""
    print "severs = ", str(c)
    print "Estimate of finish:",numpy.mean(finTime)
    print "Conf int of finish time:",conf(finTime)

