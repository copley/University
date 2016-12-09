import random
import numpy
import math

## Useful extras ----------
def conf(x):
    """95% confidence interval"""
    lower = numpy.mean(x) - 1.96*numpy.std(x)/math.sqrt(len(x))
    upper = numpy.mean(x) + 1.96*numpy.std(x)/math.sqrt(len(x))
    return (lower,upper)

def coxian():
	"""Generates a coxian random variate"""
	t = random.expovariate(2)
	if random.random() <= 0.2:
		return t

	t += random.expovariate(3)
	return t


if __name__ == "__main__":
	random.seed(123)
	a = []
	b = []
	for i in range(10000):
		variate = coxian()

		a.append(variate)
		b.append(math.pow(variate, 2))

	print "E[t]:", conf(a)
	print "E[t^2]:", conf(b)