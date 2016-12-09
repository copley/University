import matplotlib.pylab as plt
import re
import numpy as np

x = []
y = []
f = file('regression.txt', 'r+')
lines = f.read().strip().split('\n')

for line in lines:
	t = re.compile("\s+").split(line.strip())
	x.append(float(t[0]))
	y.append(float(t[1]))


plt.scatter(x, y, s=30, facecolors='none', edgecolors='r')

q = np.linspace(-2, 3, num=10000)
plt.plot(q, 1 + q**2 - 2*(q**3) + q**4)

plt.savefig("plot.png")