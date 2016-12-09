import numpy
import numpy
import math

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
        

# End functions *******************************************************

def extractData(filename):
    file = open(filename, 'r') 
    # list containing all activity, server starts, server empty, queue arrival
    all_activity = []

    for line in file:
        if not line.startswith('#') and 'begin' not in line and 'end' not in line: # ignore commented lines and begin and end lines
            # append all server activities (service start and server empty, queue arrival
            if ('s' in line or 'e' in line or 'q' in line): all_activity.append(line)

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

    for a in arrivals:
    # interarrival times
        interarrivals = []
        for i in range(len(arrivals)-1):
            interarrivals.append(arrivals[i+1]-arrivals[i])

    return (all_service_times, interarrivals)

d1 = extractData('data.txt')
#d2 = extractData('midday_01_04.txt')
#d3 = extractData('midday_29_03.txt')
#d4 = extractData('midday_d.txt')    
#d5 = extractData('morning_k.txt')
#d6 = extractData('morningCrew010416.txt')
#d7 = extractData('morningCrew300316.txt')
#data = [d1,d2,d3,d4,d5,d6,d7]
data = [d1]
interarrivals = []
service_times = []
for i in range(len(data)):
    service_times += data[i][0]
    interarrivals += data[i][1]

print numpy.histogram(service_times)
print numpy.histogram(interarrivals)
    