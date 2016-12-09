import numpy
import matplotlib.pyplot as pl

def graph(func, l, m, n):
    x = numpy.linspace(l, m, n, endpoint=True)
    y = [func(z) for z in x]

    pl.plot(x, y)
    pl.savefig('graph.png')

def f(x):
    if x == 0:
        return 1

    return numpy.sin(numpy.pi * x) / numpy.pi * x

graph(f, -numpy.pi, numpy.pi, 256)
