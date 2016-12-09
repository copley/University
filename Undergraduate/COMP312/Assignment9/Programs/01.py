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

def tablelookup(P):
    u = random.random()
    sumP = 0.0
    for i in range(len(P)):
        sumP += P[i]
        if u < sumP:
            return i

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

        currentStation = 0
        while (currentStation != 3):

            station = G.stations[currentStation]#getattr(G, 'station' + str(currentStation))
            #print station[1]
            yield request, self, station[0]
            t = random.expovariate(mu)
            yield hold, self, t
            yield release, self, station[0]

            currentStation = tablelookup(station[1])

        Arrival.n -= 1
        G.nummon.observe(Arrival.n)

        delay = now()-arrivetime
        G.delaymon.observe(delay)



class G:
    stations = [['dummy', [0, 0.1, 0.9, 0]],
                ['dummy', [0.2, 0, 0.5, 0.3]],
                ['dummy', [0, 0.1, 0, 0.9]]]
    delaymon = 'Monitor'
    nummon = 'Monitor'

def model(c, N, lamb, mu, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    Arrival.n = 0
    G.stations[1][0] = Resource(c)
    G.stations[0][0] = Resource(c)
    G.stations[2][0] = Resource(c)

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

lambs = [1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2]

for lam in lambs:
    allW = []
    allL = []
    for k in range(50):
        seed = 123*k
        result = model(c=2, N=10000, lamb=lam, mu=1.0,
                  maxtime=2000000, rvseed=seed)
        allW.append(result[0])
        allL.append(result[1])

    print "\nLambda: ", lam
    print "\tEstimate of W:",numpy.mean(allW)
    print "\tConf int of W:",conf(allW)
    print "\tEstimate of L:",numpy.mean(allL)
    print "\tConf int of L:",conf(allL)
