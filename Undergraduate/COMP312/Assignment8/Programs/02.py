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

class Job(Process):

    """an arrival"""
    def run(self, p):
        currentNode = 0

        while True:
            server = getattr(G, 'node' + str(currentNode))

            t = now()

            yield request, self, server[0]
            t = random.expovariate(server[1])
            yield hold, self, t
            yield release, self, server[0]

            server[2].observe(now() - t)

            if currentNode == 0:
                r = random.random()
                if r <= p:
                    currentNode = 1
                else:
                    currentNode = 2
            elif currentNode == 1:
                currentNode = 0
            elif currentNode == 2:
                currentNode = 1



class G:
    node0 = ['server', 1.0/20.0, 'Monitor']
    node1 = ['server', 1.0/10.0, 'Monitor']
    node2 = ['server', 1.0/30.0, 'Monitor']
    delaymon = 'Monitor'

def model(N, p, maxtime, rvseed):
    # setup
    initialize()

    # Create the customers in the sysyem
    for i in range(N):
        j = Job(name="Job" + str(i))
        activate(j, j.run(p))

    random.seed(rvseed)
    G.node0[0] = Resource(N, monitored=True) # Has enough servers to handler all jobs therefor same as IS
    G.node0[2] = Monitor()

    G.node1[0] = Resource(1, monitored=True)
    G.node1[2] = Monitor()

    G.node2[0] = Resource(1, monitored=True)
    G.node2[2] = Monitor()
    G.delaymon = Monitor()

    # simulate
    simulate(until=maxtime)

    L = []
    for i in range(3):
        server = getattr(G, 'node' + str(i))
        h = server[0].waitMon.timeAverage() + server[0].actMon.timeAverage()
        L.append(h)

    # gather performance measures
    W0 = G.node0[2].mean()
    W1 = G.node1[2].mean()
    W2 = G.node2[2].mean()
    return ([W0, W1, W2], L)

## Experiment ----------
allW0 = []
allW1 = []
allW2 = []
allL0 = []
allL1 = []
allL2 = []
for k in range(50):
    print k
    seed = 123*k
    result = model(N=5, p=0.5, maxtime=10000, rvseed=seed)

    W = result[0]
    L = result[1]

    allW0.append(W[0])
    allW1.append(W[1])
    allW2.append(W[2])
    allL0.append(L[0])
    allL1.append(L[1])
    allL2.append(L[2])

print ""
print "Estimate of W0:",numpy.mean(allW0)
print "Conf int of W0:",conf(allW0)

print ""
print "Estimate of W1:",numpy.mean(allW1)
print "Conf int of W1:",conf(allW1)

print ""
print "Estimate of W2:",numpy.mean(allW2)
print "Conf int of W2:",conf(allW2)

print ""
print "Estimate of L0:",numpy.mean(allL0)
print "Conf int of L0:",conf(allL0)

print ""
print "Estimate of L1:",numpy.mean(allL1)
print "Conf int of L1:",conf(allL1)

print ""
print "Estimate of L2:",numpy.mean(allL2)
print "Conf int of L2:",conf(allL2)
