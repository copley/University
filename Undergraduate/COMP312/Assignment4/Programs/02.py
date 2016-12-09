from SimPy.Simulation import * 

class Person(Process):
	def visit(self, timeInMuseum):
		print now(), self.name, " This is new"

		for i in range(len(timeInMuseum)):
			print now(), self.name, " Look!, number", i
			yield hold, self, timeInMuseum[i]	

		print now(), self.name, "mm"

maxTime = 100.0
timeInMuseum = [4.5, 5.5];

print "Kathy (b)"
initialize()
c = Person(name="Kathy")
activate(c, c.visit(timeInMuseum), at=0.0)
simulate(until=maxTime)