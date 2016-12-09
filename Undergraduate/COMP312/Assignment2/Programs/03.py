import random

def tablelookup(y,p):
	"""Sample from y[i] with probabilities p[i]"""
	u = random.random()
	sumP = 0.0
	b = 0

	for i in range(len(p)):
		sumP += p[i]
		b += 1
		if u < sumP:
			return {'val': y[i], 'count': b}

def run_lookup(y, p):
	"""Runs a lookup for given probabilities"""

	m = 1000000
	b = 0.0
	valuetotal = 0.0

	for k in range(m):
		d = tablelookup(y,p)

		b += d['count']
		valuetotal += d['val']

	print valuetotal/m
	print b/m
	print "\n"

random.seed(123)

y1 = [0,1,2,3,4,5]
p1 = [1.0/1024, 15.0/1024, 90.0/1024, 270.0/1024, 405.0/1024, 243.0/1024]

y2 = [5,4,3,2,1,0]
p2 = [243.0/1024, 405.0/1024, 270.0/1024, 90.0/1024, 15.0/1024, 1.0/1024]

y3 = [4,3,5,2,1,0]
p3 = [405.0/1024, 270.0/1024, 243.0/1024, 90.0/1024, 15.0/1024, 1.0/1024]


print "Part I"
run_lookup(y1, p1)

print "Part II"
run_lookup(y2, p2)

print "Part III"
run_lookup(y3, p3)