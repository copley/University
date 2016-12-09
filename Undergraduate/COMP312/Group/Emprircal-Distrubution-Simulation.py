"""Simulation of bluebridge ferry terminal using collected data for distribution"""

from SimPy.Simulation import *
import random
import numpy
import math
import matplotlib.pyplot as pl

def get_uniquic(l):
    """Takes a list and returns a new list with only uniquic elements"""
    return list(set(l))

class Distrubution(object):
    """Handles sampleing from a empirial distribution"""
    def __init__(self, samples):
        self.observations = samples
        self.ws = sorted(samples)

    def random_variate(self):
        """Generates a new random variate from the distrubution"""
        r = random.random()
        k = 0

        while self.__f(k+1) <= r:
            k += 1

        return self.__w(k) + ((r - self.__f(k))/(self.__f(k+1) - self.__f(k))) * (self.__w(k+1) - self.__w(k))


    def __f(self, i):
        """Computes the CDF"""
        if i == 0:
            return 0

        wi = self.ws[i]
        count = sum(s <= wi for s in self.observations)
        return float(count)/float(len(self.observations))

    def __w(self, i):
        if i == 0:
            return 0

        return self.ws[i-1]


## Useful extras ----------
def conf(L):
    """confidence interval"""
    lower = numpy.mean(L) - 1.96*numpy.std(L)/math.sqrt(len(L))
    upper = numpy.mean(L) + 1.96*numpy.std(L)/math.sqrt(len(L))
    return (lower,upper)

## Model ----------
class Source(Process):
    """generate random arrivals"""
    def run(self, N, irt, st):
        for i in range(N):
            a = Arrival(str(i))
            activate(a, a.run(st))
            t = irt.random_variate()
            yield hold, self, t

class Arrival(Process):
    n = 0

    """an arrival"""
    def run(self, st):
        arrivetime = now()

        Arrival.n += 1
        G.numbermon.observe(Arrival.n)

        yield request, self, G.server
        t = st.random_variate()
        yield hold, self, t
        yield release, self, G.server
        delay = now()-arrivetime
        G.delaymon.observe(delay)
        G.total_W += delay

        Arrival.n -= 1
        G.numbermon.observe(Arrival.n)

        G.num_departures += 1
        G.trackW.observe(G.total_W/G.num_departures)



class G:
    server = 'dummy'
    delaymon = 'Monitor'
    numbermon = 'Monitor'
    trackW = 'for plotting'
    num_departures = 0
    total_W = 0

def model(c, N, irt, st, maxtime, rvseed):
    # setup
    initialize()
    random.seed(rvseed)
    G.server = Resource(c, monitored=True)
    G.delaymon = Monitor()
    G.numbermon = Monitor()
    G.trackW = Monitor()
    G.num_departures = 0
    G.total_W = 0

    # simulate
    s = Source('Source')
    activate(s, s.run(N, irt, st))
    simulate(until=maxtime)

    # gather performance measures
    W = G.delaymon.mean()
    LQ = G.server.waitMon.timeAverage()
    L = G.numbermon.timeAverage()
    lambEff = L/W
    WQ = LQ/lambEff
    U = G.server.actMon.timeAverage()
    return(W, L, WQ, LQ, U)

## Experiment ----------

# Create the distrubutions
f = file('AllInterarrivalTimes.txt', 'r+')
irt_data = [float(x) for x in f.read().split(', ')]
f.close()
interrival_dist = Distrubution(irt_data)

f = file('AllServiceTimes.txt', 'r+')
st_data = [float(x) for x in f.read().split(', ')]
f.close()
service_dist = Distrubution(st_data)

allW = []
allL = []
allU = []
allWQ = []
allLQ = []

pl.clf()
pl.hold(True)
for k in range(50):
    print k
    seed = 123*k
    result = model(c=3, N=10000, irt=interrival_dist, st=service_dist,
                  maxtime=2000000, rvseed=seed)
    allW.append(result[0])
    allL.append(result[1])
    allWQ.append(result[2])
    allLQ.append(result[3])
    allU.append(result[4])

    pl.plot(G.trackW.tseries(), G.trackW.yseries())

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


pl.xlabel("t")
pl.ylabel("W")
pl.axis("tight")
pl.savefig("Empirical-Warm-Up.png")
pl.show()
