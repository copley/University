import numpy as np
import random
import scipy.stats as ss
import matplotlib.pyplot as plt
import chiSquare

f = file('AllInterarrivalTimes2.txt', 'r+')
service_times = [float(x) for x in f.read().split(', ')]

fit_alpha,fit_beta=ss.expon.fit(service_times, floc=0)
rv1 = ss.expon(fit_alpha,fit_beta)
print 'Exponential parameters: loc, mu: ',fit_alpha,fit_beta

fit_alpha,fit_loc,fit_beta=ss.erlang.fit(service_times, floc=0)
fit_alpha = int(round(fit_alpha))
rv2 = ss.erlang(fit_alpha,fit_loc,fit_beta)
print 'Erlang parameters: f, loc, mu: ',fit_alpha,fit_loc,fit_beta

fit_alpha,fit_loc,fit_beta=ss.gamma.fit(service_times, floc=0)
rv3 = ss.gamma(fit_alpha,fit_loc,fit_beta)
print 'Gamma parameters: alpha, loc, beta: ',fit_alpha,fit_loc,fit_beta

fig = plt.figure()
myHist = plt.hist(service_times, 60, normed=True)
x = np.linspace(0.001,500)
ex = plt.plot(x, rv1.pdf(x), lw=2, label="Exponential")
e = plt.plot(x, rv2.pdf(x), lw=2, label="Erlang")
g = plt.plot(x, rv3.pdf(x), lw=2, label="Gamma")
plt.legend(loc='upper right')
fig.suptitle('Interarrival times')

dof = 1
n = 30
exp = chiSquare.model(service_times, n, dof, rv= rv1)
print "The chi_sq test value for exponential is %10.6f and the p-value is %10.20f" % (exp[0], exp[1])
dof = 2
n = 30
print "The chi_sq test value for erlang is %10.6f and the p-value is %10.20f" % (chiSquare.model(service_times, n, dof, rv=rv2)[0], chiSquare.model(service_times,n,dof,rv= rv2)[1])
print "The chi_sq test value for gamma is %10.6f and the p-value is %10.20f" % (chiSquare.model(service_times, n, dof, rv=rv3)[0], chiSquare.model(service_times,n,dof,rv= rv3)[1])
plt.ylim([0,0.018])
plt.show()
