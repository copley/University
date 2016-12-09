# Bluebridge data collection - Group 4

import numpy
import math
import Tkinter
import tkFileDialog

#Functions ************************************************************

def split_data_into_individual_servers(services):
    """ Takes the full activity list and populates the server lists 
    """
    for i in services:  
        if ('s1' in i or 'e1' in i): server1.append(i) # appends to appropriate list, any service start/stop activity
        elif ('s2' in i or 'e2' in i): server2.append(i)
        elif ('s3' in i or 'e3' in i): server3.append(i)

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

def get_queue_times(services):
    """ build and return a list of times spent in the queue
    """
    queue_times = [] # list for all queue times
    queue_service_starts = [] # this list holds the indices of start times from queue to service
                              # If the index number of a start time is in the list, then that start
                              # time has already been used, so the next queue exit will start at the next
                              # start time not in this list.

    for i in range(len(services)):  
        if ('q' in services[i]): # if a q entry found in list of all activities
            start_queue_time = float(services[i].split()[0]) # get the time, assign to start queue
            # This inner loop checks from the q start time found, and searches until it finds the next service start time
            # that has not been used already as a queue exit time
            for j in range(len(services)):
                if('s' in services[i+j] and i+j not in queue_service_starts): # "unused" service start time is found
                    end_queue_time = float(services[i+j].split()[0]) # set queue exit time
                    queue_service_starts.append(i+j) # important step - add the index of the service start time to the list of used queue exit times 
                    break                         # exit inner loop
            queue_time = end_queue_time - start_queue_time  #
            queue_times.append(queue_time)
            
    return queue_times
    
def print_service_times(service_times, server_number):
    """prints out the list of service times from a given server
    """
    print ("\nServer {}\n".format(server_number))
    for i in range(len(service_times)):
        print ("Service {}: {} seconds".format(i, round(service_times[i],2)))
    print ("\nMean service time for server: {}".format(round(numpy.mean(service_times),2))) # also prints mean service time

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
                queuing -= 1
            else:
                arrivals.append(float(e.split()[0]))
    return arrivals
        

# End functions *******************************************************

# Program start *******************************************************

# open data file using GUI
Tkinter.Tk().withdraw()
file_path = tkFileDialog.askopenfilename()
file = open(file_path, 'r') 
# list containing all activity, server starts, server empty, queue arrival
all_activity = []
# Lists to contain activity for each server
server1 = []
server2 = []
server3 = []

for line in file:
    if not line.startswith('#') and 'begin' not in line and 'end' not in line: # ignore commented lines and begin and end lines
        # append all server activities (service start and server empty, queue arrival
        if ('s' in line or 'e' in line or 'q' in line): all_activity.append(line)

split_data_into_individual_servers(all_activity) # populate each server's list of service start/end 
     
# the following lines get a count for each activity ---------------------------------   
s1_services = 0
s2_services = 0
s3_services = 0
total_services = 0
num_in_queue = 0      
# get total time of data collection session in minutes: last activity time - first activity time / 60 to 2dp
observation_time = round((float(all_activity[len(all_activity)-1].split()[0]) - float(all_activity[0].split()[0])) / 60, 2)

for i in all_activity:
    if ('s1' in i): s1_services += 1
    elif ('s2' in i): s2_services += 1
    elif ('s3' in i): s3_services += 1
    elif ('q' in i): num_in_queue += 1        
total_services = s1_services + s2_services + s3_services

print ("Number of services by s1: {0}\nNumber of services by s2: {1}\nNumber of services by s3: {2}\nTotal services = {3},\nTotal number queued = {4}\nDuration of session: {5} minutes".format(s1_services,s2_services,s3_services,total_services,num_in_queue, observation_time))
 
# use functions to popluate server lists and print information
server1_service_times = get_service_times(server1)
server2_service_times = get_service_times(server2)
server3_service_times = get_service_times(server3)

print_service_times(server1_service_times, 1)
print_service_times(server2_service_times, 2)
print_service_times(server3_service_times, 3)

# get list of times spent in queue from function
queue_times = get_queue_times(all_activity)
#print queue info
print ("\nTimes spent in queue\n")
for i in range(len(queue_times)):
    print ("entry {}: {} seconds".format(i, round(queue_times[i],2)))
print ("\nmean time spent in queue: {} seconds\n".format(round(numpy.mean(queue_times),2)))

# list of customer arrival times
arrivals = arrival_times(all_activity)
print "\nCustomer arrival times:\n"
for a in arrivals:
    print a
# interarrival times
interarrivals = []
for i in range(len(arrivals)-1):
    interarrivals.append(arrivals[i+1]-arrivals[i])
mean_interarrival = numpy.mean(interarrivals)   
print "\nMean interarrival time: " + str(mean_interarrival)

