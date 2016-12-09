f = file('AllInterarrivalTimes.txt', 'r+')
irt_data = [float(x) for x in f.read().split(', ')]
f.close()

f = file('AllServiceTimes.txt', 'r+')
st_data = [float(x) for x in f.read().split(', ')]
f.close()

print "Average Interarrival Time", reduce(lambda x, y: x+y, irt_data) / len(irt_data)
print "Average Service Time", reduce(lambda x, y: x+y, st_data) / len(st_data)
