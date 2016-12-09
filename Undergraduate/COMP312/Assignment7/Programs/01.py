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
        # Check to see if arival is rejected
        if len(G.server.waitQ) >= 5:
            G.rejectmon.observe(1)
            return
        G.rejectmon.observe(0)

        # See if the arrival will balk
        pBalk = 0.2 * len(G.server.waitQ)
        if random.random() <= pBalk:
            G.balkmon.observe(1)
            return

        G.balkmon.observe(0)

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
    balkmon = 'Monitor'
    rejectmon = 'Monitor'

def model(c, N, lamb, mu, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.server = Resource(c)
    G.delaymon = Monitor()
    G.balkmon = Monitor()
    G.rejectmon = Monitor()

    # simulate
    s = Source('Source')
    activate(s, s.run(N, lamb, mu))
    simulate(until=maxtime)

    # gather performance measures
    W = G.delaymon.mean()
    B = G.balkmon.mean()
    R = G.rejectmon.mean()
    return(W, B, R)

## Experiment ----------
allW = []
allBalk = []
allReject = []
for k in range(50):
    print k
    seed = 123*k
    result = model(c=1, N=10000, lamb=2.0, mu=(1.0/0.5), maxtime=2000000, rvseed=seed)
    allW.append(result[0])
    allBalk.append(result[1])
    allReject.append(result[2])
#print allW
print ""
print "Estimate of W:",numpy.mean(allW)
print "Conf int of W:",conf(allW)
print ""
print "Estimate of Balk:",numpy.mean(allBalk)
print "Conf int of Balk:",conf(allBalk)
print ""
print "Estimate of Reject:",numpy.mean(allReject)
print "Conf int of Reject:",conf(allReject)
