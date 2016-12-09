from SimPy.Simulation import * 
import random

class Person(Process):
	def visit(self, timeInMuseum, p):
		print now(), self.name, " This is new"

		print now(), self.name, " Look!, number 0"
		yield hold, self, timeInMuseum[0]

		c = random.random()
		i = 1
		if c > p:
			i = 2


		print now(), self.name, " Look!, number ", i
		yield hold, self, timeInMuseum[i]

		print now(), self.name, "mm"

random.seed(99999)
maxTime = 100.0
p = 0.4
timeInMuseum = [4.5, 5.5, 7.5];

print "Kathy (c)"
initialize()
c = Person(name="Kathy")
activate(c, c.visit(timeInMuseum, p), at=0.0)
simulate(until=maxTime)