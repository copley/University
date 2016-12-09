import random
random.seed(123)


def roll_die(n):
	"""Rolls n die and returns the results as array"""

	rolls = [];
	for i in range(0, n):
		rolls.append(random.randint(1, 6))

	return rolls;

if __name__ == '__main__':
	n = 1000000
	events = 0

	for i in range(0, n):
		o = False


		for j in range(0, 24):
			roll = roll_die(2)

			if roll[0] == 6 and roll[1] == 6:
				o = True

		if o:
			events += 1

	p = float(events)/float(n)

	print p
