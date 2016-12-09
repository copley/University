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
    def run(self, N, lamb):
        for i in range(N):
            a = Arrival(str(i))
            activate(a, a.run())
            t = random.expovariate(lamb)
            yield hold, self, t

class Arrival(Process):
    """an arrival"""
    def run(self):
        arrivetime = now()
        # Before we can move into the river we need
        # to be assigned a dock
        yield request, self, G.dock

        # Then we must wait for a tug boat to take us to the dock
        yield request, self, G.tug_boat

        # Once given a tug boat it takes 0.2 days to get there
        yield hold, self, 0.2

        # Now we can release the tug boat
        yield release, self, G.tug_boat

        # Now it takes a day to unload the boat
        yield hold, self, 1.0

        # Now we can release the dock
        yield release, self, G.dock
        #t = random.expovariate(mu)
        #yield hold, self, t
        #yield release, self, G.server
        delay = now()-arrivetime
        G.delaymon.observe(delay)



class G:
    dock = 'dummy'
    tug_boat = 'dummy'
    delaymon = 'Monitor'

def model(c, N, lamb, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.dock = Resource(2)
    G.tug_boat = Resource(c, monitored=True)
    G.delaymon = Monitor()

    # simulate
    s = Source('Source')
    activate(s, s.run(N, lamb))
    simulate(until=maxtime)

    # gather performance measures
    U = 1 - G.tug_boat.waitMon.timeAverage()
    W = G.delaymon.mean()
    return(W, U)

## Experiment ----------

for i in range(1, 3):
    print "\n" + str(i) + " Tug Boats"
    allW = []
    allU = []
    for k in range(50):
        seed = 123*k
        result = model(c=i, N=10000, lamb=1, maxtime=2000000, rvseed=seed)
        allW.append(result[0])
        allU.append(result[1])
    #print allW
    print ""
    print "Estimate of W:",numpy.mean(allW)
    print "Conf int of W:",conf(allW)
    print ""
    print "Estimate of U:",numpy.mean(allU)
    print "Conf int of U:",conf(allU)
