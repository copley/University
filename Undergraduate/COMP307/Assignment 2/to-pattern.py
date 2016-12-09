import sys
import re

s = "data/part1/iris-test"
# s = "data/part1/iris-training"

data = {'Iris-setosa': ([], '1 0 0'), 'Iris-versicolor': ([], '0 1 0'), 'Iris-virginica': ([], '0 0 1')}

f = file(s + ".txt")
lines = f.read().split('\n')
f.close()

maxvals = [sys.float_info.min, sys.float_info.min, sys.float_info.min, sys.float_info.min]
minvals = [sys.float_info.max, sys.float_info.max, sys.float_info.max, sys.float_info.max]

for line in lines:
	point = re.compile("\s*").split(line)#line.split('\s*')

	if not len(point) == 5:
		continue

	v = (float(point[0]), float(point[1]), float(point[2]), float(point[3]))

	for i in range(4):
		if float(point[i]) > maxvals[i]:
			maxvals[i] = float(point[i])

		if float(point[i]) < minvals[i]:
			minvals[i] = float(point[i])

	c = point[4]

	data[c][0].append([c, v])


# Compute the ranges of the values
r = [0, 0, 0, 0]
for i in range(4):
	r[i] = maxvals[i] - minvals[i]


# Normalise the data
i = 0
for d in data:
	for pattern in data[d][0]:
		ov = pattern[1]
		nv = ((ov[0] - minvals[0])/r[0], (ov[1] - minvals[1])/r[1], (ov[2] - minvals[2])/r[2], (ov[3] - minvals[3])/r[3])

		pattern[1] = nv


# Sum up the total number of patters
total_patterns = 0
for d in data:
	total_patterns += len(data[d][0])

print "Total Patterns: ", total_patterns

output = file(s + '.pat', 'w+')

output.write('SNNS pattern definition file V3.2\n')
output.write('generated at Mon Apr 25 17:47:12 1994\n')
output.write('\n\n')

output.write('No. of patterns : ' + str(total_patterns) + '\n')
output.write('No. of input units : 4\n')
output.write('No. of output units : 3\n')

output.write('\n')

i = 0
for d in data:
	for pattern in data[d][0]:
		i += 1
		output.write('# Input pattern ' + str(i) + ':\n')
		output.write(str(pattern[1][0]) + ' ' + str(pattern[1][1]) + ' ' + str(pattern[1][2]) + ' ' + str(pattern[1][3]) + '\n')
		output.write('# Output pattern ' + str(i) + ':\n')
		output.write(data[d][1] + '\n')

output.close()
