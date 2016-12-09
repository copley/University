data = {'Iris-setosa': [], 'Iris-versicolor': [], 'Iris-virginica': []}

f = file("part1/iris-training.txt")
lines = f.read().split('\n')
f.close()

maxvals = [Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE]
minvals = [Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE]

for line in lines:
	point = line.split(' ')

	v = (float(point[0]), float(point[1]), float(point[2]))

	for i in range(3):
		if point[i] > maxvals[i]:
			maxvals[i] = point[i]

		if point[i] < minvals[i]:
			minvals[i] = point[i]

	c = point[3]

	data[c].append([c, v])


# Compute the ranges of the values
r = [0, 0, 0]
for i in range(3):
	r[i] = maxvals[i] - minvals[i]


# Normalise the data
i = 0
for d in data:
	for pattern in d:
		ov = pattern[1]
		nv = (ov[0]/r[0], ov[1]/r[1], ov[2]/r[2])

		pattern[1] = nv


total_patterns = 0
for d in data:
	total_patterns += len(d)

#output = file('part1/iris-training.pat')

#output.write('SNNS pattern definition file V3.2\n')
#output.write('generated at Mon Apr 25 17:47:12 1994\n')
#output.write('\n\n')

#output.write('No. of patterns : ' + total_patterns)
#output.write('No. of input units : ' + 3)
#output.write('No. of output units : ' + 3)

#output.write('\n')

#i = 0
#for d in data:
#	for pattern in d:
#		i += 1
#		output.write('# Input pattern ' + i + ':\n')


#output.write('# Input pattern ')

#output.close()