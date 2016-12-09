import math

class Ellipse(object):
	"""The Ellipse class"""

	def __init__(self, a, b):
		self.a = a;
		self.b = b;

	def area(self):
		return math.pi * self.a * self.b

	def eccentricity(self):
		return math.sqrt(1 - math.pow(self.b/self.a, 2))


if __name__ == "__main__":
	ellipse = Ellipse(10, 5)

	print "Area: " + str(ellipse.area())
	print "Eccentricity: " + str(ellipse.eccentricity())