from scipy import stats  
import numpy as np  
import matplotlib.pylab as plt
import chisquare

#f = file('AllServiceTimes.txt', 'r+')
f = file('AllInterarrivalTimes.txt', 'r+')
data = [float(x) for x in f.read().split(', ')]#extract.extractData('data.txt')[0]

# plot normed histogram
plt.hist(data, normed=True)

# find minimum and maximum of xticks, so we know
# where we should compute theoretical distribution
xt = plt.xticks()[0]  
xmin, xmax = min(xt), max(xt)  
lnspc = np.linspace(xmin, xmax, len(data))

# Try the exponential disctubution
aexpon, muExp = stats.expon.fit(data)
pdf_exp = stats.expon.pdf(lnspc, aexpon, muExp)
plt.plot(lnspc, pdf_exp, label="Exponential")

# Try the erlang distrubution
ae,be,muErl = stats.erlang.fit(data)
pdf_erl = stats.erlang.pdf(lnspc, ae, be, muErl)
plt.plot(lnspc, pdf_erl, label="Erlang")

# Try the gamma distrubution
ag,bg,thetaGamma = stats.gamma.fit(data)  
pdf_gamma = stats.gamma.pdf(lnspc, ag, bg,thetaGamma)  
plt.plot(lnspc, pdf_gamma, label="Gamma")

plt.legend(loc='upper right')
plt.savefig("fitting.png")


print "[*] Fitting Paramaters"
print "[#] Exponential: ", aexpon, '???, mu = ', muExp, '\n',
print "[#] Erlang: ", ae, '???, k = ', be, ', mu = ', muErl
print "[#] Gamma: ", ag, '??? k = ', bg, ', Theta = ', thetaGamma, '\n'


print "[*] Computing Chi Square Test"
# Apply Chi-Square test to the three fittings
dof = 2
n = 30

rv = stats.expon(aexpon, muExp)
exponFit = chisquare.model(data, n, dof, rv)
print "[#] Exponential: The chi_sq test value is %10.6f and the p-value is %10.6f" % (exponFit[0], exponFit[1])

rv = stats.erlang(ae, be, muErl)
erlangFit = chisquare.model(data, n, dof, rv)
print "[#] Erlang: The chi_sq test value is %10.6f and the p-value is %10.6f" % (erlangFit[0], erlangFit[1])

rv = stats.gamma(ag, bg, thetaGamma)
gammaFit = chisquare.model(data, n, dof, rv)
print "[#] Gamma: The chi_sq test value is %10.6f and the p-value is %10.6f" % (gammaFit[0], gammaFit[1])