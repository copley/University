from SimPy.Simulation import * 
import random

class Person(Process):
	def visit(self, timeInMuseum, p):
		print now(), self.name, " This is new"

		print now(), self.name, " Look!, number 0"
		yield hold, self, timeInMuseum
		print now(), self.name, "mm"

		c = random.random()
		while c < p:
			print now(), self.name, " number 0"
			yield hold, self, timeInMuseum
			print now(), self.name, "mm"

			c = random.random()
			

random.seed(99999)
maxTime = 100.0
p = 0.4
timeInMuseum = 4.5

print "Kathy (d)"
initialize()
c = Person(name="Kathy")
activate(c, c.visit(timeInMuseum, p), at=0.0)
simulate(until=maxTime)