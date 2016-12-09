from SimPy.Simulation import * 

class Person(Process):
	def visit(self, timeInMuseum):
		print now(), self.name, " This is new"
		yield hold, self, timeInMuseum
		print now(), self.name, " Nice Place!"

maxTime = 100.0
timeInMuseum = 10.0;

print "Kathy (a)"
initialize()
c = Person(name="Kathy")
activate(c, c.visit(timeInMuseum), at=0.0)
simulate(until=maxTime)