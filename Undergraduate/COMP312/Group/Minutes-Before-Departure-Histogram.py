import matplotlib.pyplot as plt
import numpy as np

def to_minutes_before_departure(times, d):
    return map(lambda x: (d - x)/60, times)

MORNING_DEPARTURE_TIME = 28800
AFTERNOON_DEPARTURE_TIME = 46800
#EVENING_DEPARTURE_TIME = ???

files = [('data/midday_29_03.txt', AFTERNOON_DEPARTURE_TIME),
         ('data/midday_02_04.txt', AFTERNOON_DEPARTURE_TIME),
         ('data/midday_01_04.txt', AFTERNOON_DEPARTURE_TIME),
         ('data/morning_01_04.txt', MORNING_DEPARTURE_TIME),
         ('data/morning_02_04.txt', MORNING_DEPARTURE_TIME),
         ('data/morning_30_03.txt', MORNING_DEPARTURE_TIME)]

min_before_departure = []

for loc in files:
    f = file(loc[0], 'r+')
    arrivals = filter(lambda x: 's' in x, f.read().split('\n')[3:])
    times = map(lambda x: int(float(x.split(' ')[0])), arrivals)

    min_before_departure.extend(to_minutes_before_departure(times, loc[1]))

# Save list as file
f = open('min_before_departure_list.txt', 'w+')
f.write(str(min_before_departure))
f.close()

plt.hist(min_before_departure, bins=10)
plt.title("Histogram of Arrival Times Before Boarding")
plt.ylabel("Number of Arrivals")
plt.xlabel("Time Before Arrival (in minutes)")
plt.savefig("min_before_departure.png")
plt.show()
