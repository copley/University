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
	min_num_throws = 24
	max_num_throws = 26
	events = {}

	for i in range(0, n):

		for j in range(min_num_throws, max_num_throws + 1):

			o = False
			events.setdefault(j, 0)

			for k in range(0, j):
				roll = roll_die(2)

				if roll[0] == 6 and roll[1] == 6:
					o = True

			if o:
				events[j] += 1

	for i in range(min_num_throws, max_num_throws+1):
		events[i] = float(events[i])/float(n)

	print events