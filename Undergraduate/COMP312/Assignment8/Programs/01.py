"""(q3.py) M/M/c queueing system with monitor
   and multiple replications"""

from SimPy.Simulation import *
import random
import numpy
import math

## Useful extras ----------
def conf(L):
    """confidence interval"""
    lower = numpy.mean(L) - 1.96*numpy.std(L)/math.sqrt(len(L))
    upper = numpy.mean(L) + 1.96*numpy.std(L)/math.sqrt(len(L))
    return (lower,upper)

## Model ----------
class Source(Process):
    """generate random arrivals"""
    def run(self, N, lamb, mu):
        for i in range(N):
            a = Arrival(str(i))
            activate(a, a.run(mu))
            t = random.expovariate(lamb)
            yield hold, self, t

class Arrival(Process):
    n = 0

    """an arrival"""
    def run(self, mu):
        arrivetime = now()

        Arrival.n += 1
        G.nummon.observe(Arrival.n)

        currentStation = 1
        while not currentStation == -1:

            station = 'dummy'
            if currentStation == 1:
                station = G.station1
            elif currentStation == 2:
                station = G.station2
            elif currentStation == 3:
                station = G.station3

            yield request, self, station
            t = random.expovariate(mu)
            yield hold, self, t
            yield release, self, station

            r = random.random()
            if currentStation == 1:
                if r <= 0.1:
                    currentStation = 2
                else:
                    currentStation = 3
            elif currentStation == 2:
                if r <= 0.2:
                    currentStation = 1
                else:
                    currentStation = 3
            elif currentStation == 3:
                if r <= 0.1:
                    currentStation = 2
                else:
                    currentStation = -1


        Arrival.n -= 1
        G.nummon.observe(Arrival.n)

        delay = now()-arrivetime
        G.delaymon.observe(delay)



class G:
    station1 = 'dummy'
    station2 = 'dummy'
    station3 = 'dummy'
    delaymon = 'Monitor'
    nummon = 'Monitor'

def model(c, N, lamb, mu, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.station1 = Resource(c)
    G.station2 = Resource(c)
    G.station3 = Resource(c)

    G.delaymon = Monitor()
    G.nummon = Monitor()

    # simulate
    s = Source('Source')
    activate(s, s.run(N, lamb, mu))
    simulate(until=maxtime)

    # gather performance measures
    W = G.delaymon.mean()
    L = G.nummon.timeAverage()
    return(W, L)

## Experiment ----------
allW = []
allL = []
for k in range(50):
    print k
    seed = 123*k
    result = model(c=2, N=10000, lamb=1.4, mu=1.00,
                  maxtime=2000000, rvseed=seed)
    allW.append(result[0])
    allL.append(result[1])

print ""
print "Estimate of W:",numpy.mean(allW)
print "Conf int of W:",conf(allW)

print ""
print "Estimate of L:",numpy.mean(allL)
print "Conf int of L:",conf(allL)
