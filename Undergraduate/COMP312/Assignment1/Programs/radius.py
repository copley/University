import random
import math

N = 1000000

random.seed(123)

circleRadi = []
for i in range(0, N):
	r = random.random();
	A = math.pi * math.pow(r, 2)

	circleRadi.append((r, A))

sumR = 0
sumRSquare = 0
sumA = 0
for circle in circleRadi:
	r, a = circle

	sumR += r
	sumRSquare += math.pow(r, 2)
	sumA += a

meanR = (1.0/N) * sumR
meanRSquare = (1.0/N) * sumRSquare
meanA = (1.0/N) * sumA

print "Mean R: " + str(meanR)
print "Mean Squared R: " + str(meanRSquare)
print "pi(meanR)^2: " + str(math.pi * math.pow(meanR, 2))
print "Mean A: " + str(meanA)