## EDIT THIS SECTION ##
f = (100.0/321.0, 10.0/107.0, 100.0/321.0, 91.0/321.0)
es = (3, 6, 15, 20)
infinite = (False, False, False, True)
M = 5
#######################

def lam(m, f, w):
    """Compute lambda"""
    s = 0
    for i in range(len(f)):
        s += f[i] * w[i]

    return float(m)/float(s)


def w(l, es):
    """Compute w"""
    return (1 + l) * es

def l(lam, f, w):
    """Compute l"""
    return lam * f * w

# Compute the first row
res = [[]]
for i in range(len(es)):
    res[0].append(es[i])

lmb = lam(1, f, es)
res[0].append(lmb)

for i in range(len(es)):
    res[0].append(l(lmb, f[i], es[i]))


# Compute the rest of the table
for i in range(1, M):
    m = i+1
    prev = res[i-1]

    c = []
    for j in range(len(es)):
        # print str(6 + j)
        if not infinite[j]:
            c.append(w(prev[(len(es) + 1) + j], es[j]))
        else:
            c.append(es[j])

    lmb = lam(m, f, c)
    c.append(lmb)

    for j in range(len(es)):
        c.append(l(lmb, f[j], c[j]))

    res.append(c)

for row in res:
    print row
