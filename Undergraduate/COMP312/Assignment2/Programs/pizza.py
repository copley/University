coeffs = {}

# Compute the coefficients
l = 1
u = 1

for i in range(1, 32 + 1):
	price = max(0, 16 - 0.5 * (i-1))

	l = l * price
	u = u * (i * 1.0/(10 + price))

	coeffs[i] = float(l)/float(u)


# Compute pi 0
pi0 = 0
s = 1

for i in range(1, 32 + 1):
	s = s + coeffs[i]

pi0 = 1/s

pies = {}
pies[0] = pi0


# Compute the rest of the pis
for i in range(1, 32 + 1):
	pies[i] = coeffs[i] * pi0


# Ensure that the sum of pies is 1
piSum = 0
for i in range(32 + 1):
	piSum += pies[i]

if not piSum == 1.0:
	raise Exception("Sum of pies should be 1")


## Solve Assignment Problems ##
###############################

# Compute average number of pizza restrants
avg = 0
for i in range(32 + 1):
	avg += i * pies[i]

print "Average Num Pizza Shops: " + str(avg)

# Compute fraction of time more than 20 restrants
timeM20 = 0.0
for i in range(20 + 1, 32 + 1):
	timeM20 += pies[i]

print "Fraction of time with more than 20 restrants: " + str(timeM20)