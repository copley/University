import numpy
import math
import Tkinter
import tkFileDialog
import matplotlib.pyplot as pl

# This script generates a plot of customers in the session over time

#Function ************************************************************

def split_data_into_individual_servers(services):
    """ Takes the full activity lest and splits populates the server lists 
    """
    server1 = []
    server2 = []
    server3 = []
    
    for i in services:  
        if ('s1' in i or 'e1' in i): server1.append(i) # appends to appropriate list, any service start/stop activity
        elif ('s2' in i or 'e2' in i): server2.append(i)
        elif ('s3' in i or 'e3' in i): server3.append(i)
    return (server1, server2, server3)

def get_service_times(services):
    """takes a list of services from an individual server returns a list of service times
    """
    service_times = []
    for s in range(len(services)-1):
        if ('e' not in services[s]): # does not count e (empty) as a start time
            start_time = float(services[s].split()[0]) # every s is a start time  **cast first element of split as float
            end_time = float(services[s+1].split()[0]) # next entry is finish time - e or s
            service_time = (end_time - start_time) # calculate service time and add to list
            service_times.append(service_time)
    
    return service_times   # returns the list of service times      


def arrival_times(all_events):
    """creates a list of customer arrival times"""
    arrivals = []
    queuing = 0
    for e in all_events:
        if 'q' in e:
            arrivals.append(float(e.split()[0]))
            queuing += 1
        elif 's' in e:
            if queuing>0:
                queuing += -1
            else:
                arrivals.append(float(e.split()[0]))
    return arrivals

def departure_times(all_events):
    departures = []
    servers = 3
    for e in all_events:
        if 'e' in e:
            departures.append(float(e.split()[0]))
            servers += 1
        elif 's' in e:
            if servers>0:
                servers += -1
            else:
                departures.append(float(e.split()[0]))
    return departures

def servers_open(all_events):
    events = []
    times = []
    servers = 3
    times.append(float(all_events[0].split()[0]))
    events.append(servers)
    for e in all_events:
        t = float(e.split()[0])
        if 'c' in e:
            times.append(t)
            events.append(servers)
            servers += -1
            times.append(t)
            events.append(servers)
        elif 'o' in e:
            times.append(t)
            events.append(servers)
            servers += 1
            times.append(t)
            events.append(servers)
    times.append(float(all_events[len(all_events)-1].split()[0]))
    events.append(servers)
    return (events,times)
# End functions *******************************************************

def extractData(filename):
    file = open(filename, 'r') 
    # list containing all activity, server starts, server empty, queue arrival
    all_activity = []

    for line in file:
        if not line.startswith('#') and 'begin' not in line and 'end' not in line: # ignore commented lines and begin and end lines
            # append all server activities (service start and server empty, queue arrival
            if ('s' in line or 'e' in line or 'q' in line or 'o' in line or 'c' in line): all_activity.append(line)

    server_split = split_data_into_individual_servers(all_activity) # populate each server's list of service start/end
    server1 = server_split[0]
    server2 = server_split[1]
    server3 = server_split[2]
          
     
    # use functions to popluate server lists and print information
    server1_service_times = get_service_times(server1)
    server2_service_times = get_service_times(server2)
    server3_service_times = get_service_times(server3)

    all_service_times = server1_service_times+server2_service_times+server3_service_times

    arrivals = arrival_times(all_activity)
    departures = departure_times(all_activity)
    servers = servers_open(all_activity)

    # interarrival times
    interarrivals = []
    for i in range(len(arrivals)-1):
        interarrivals.append(arrivals[i+1]-arrivals[i])

    return (all_service_times, interarrivals,departures,arrivals,servers)

d = extractData('morning_k.txt')
departures = d[2]
arrivals = d[3]
start_time = arrivals[0]
cust = 0
f = 0.0
x = []
y = []
for i in range(4000):
    for a in arrivals:
        t = a-start_time
        if t>=f and t<float(i):
            cust += 1
    for dp in departures:
        t = dp - start_time
        if t>=f and t<float(i):
            cust += -1
    f = float(i)
    x.append(i)
    y.append(cust)
print arrivals
x2 = []
for i in d[4][1]:
    x2.append(i-start_time)
y2 = d[4][0]

x3 = []
y3 = []
for a in arrivals:
    x3.append(a-start_time)
    y3.append(6.0)

fig1 = pl.figure()
pl.plot(arrivals[:-1], d[1])
pl.xlabel("Time(s)")
pl.ylabel("Interarrival time")
fig1.suptitle('Interarrival times over session- morning_k')

fig = pl.figure()
#pl.plot(x3,y3,'ro',lw=2,label = 'Arrivals')
pl.plot(x2,y2,lw=2,label = "Servers open")
pl.plot(x,y,lw=2,label = "Customers in system")
fig.suptitle('Numbers in system over session- morning_k')
pl.legend(loc='upper right')
pl.xlabel("Time(s)")
pl.ylabel("Customers in system")
pl.show()


