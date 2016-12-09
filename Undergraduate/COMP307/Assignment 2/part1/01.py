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
    """an arrival"""
    def run(self, mu):
        arrivetime = now()
        yield request, self, G.server
        t = random.expovariate(mu)
        yield hold, self, t
        yield release, self, G.server
        delay = now()-arrivetime
        G.delaymon.observe(delay)



class G:
    server = 'dummy'
    delaymon = 'Monitor'

def model(c, N, lamb, mu, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.server = Resource(c)
    G.delaymon = Monitor()
   
    # simulate
    s = Source('Source')
    activate(s, s.run(N, lamb, mu))
    simulate(until=maxtime)
   
    # gather performance measures
    W = G.delaymon.mean()
    return(W)

## Experiment ----------
allW = []
for k in range(50):
   seed = 123*k
   result = model(c=1, N=10000, lamb=0.96, mu=1.00,
                  maxtime=2000000, rvseed=seed)
   allW.append(result)
print allW
print ""
print "Estimate of W:",numpy.mean(allW)
print "Conf int of W:",conf(allW)


