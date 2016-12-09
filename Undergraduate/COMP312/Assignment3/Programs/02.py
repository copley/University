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

if __name__ == "__main__":
	k = 9
	n = 10000

	random.seed(123)

	for i in range(1, k):
		res = estimate(i, n)
		print "K = ", i, ": ", res
