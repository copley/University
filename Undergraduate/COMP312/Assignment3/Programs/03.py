import random
import math
import numpy

def chisquarevariate(k):
	"""Generates a variate from chi square distrubuton with paramater k"""

	s = 0
	for i in range(k):
		s += math.pow(random.normalvariate(0, 1), 2)

	return s


def estimate(k, n):
	"""Estimate expected value and variance over n runs"""
	variates = []
	for i in range(n):
		variates.append(chisquarevariate(k))

	return (numpy.mean(variates), numpy.var(variates))


def conf(L):
	"""Compute a 95 percent confidence interval"""
	lower = numpy.mean(L) - 1.96 * numpy.std(L)/math.sqrt(len(L))
	upper = numpy.mean(L) + 1.96 * numpy.std(L)/math.sqrt(len(L))

	return (lower, upper)

if __name__ == "__main__":
	k = 9
	n = 10000
	m = 50

	random.seed(123)

	for i in range(1, k):

		Lvar = []
		Lmean = []
		for j in range(m):
			res = estimate(i, n)
			
			Lmean.append(res[0])
			Lvar.append(res[1])

		confMean = conf(Lmean)
		confVar = conf(Lvar)

		print "K = ", i
		print "  > Conf Mean: ", confMean
		print "  > Conf Var: ", confVar
		print "\n"
