f = (100.0/321.0, 10.0/107.0, 100.0/321.0, 91.0/321.0)
es = (3, 6, 15, 20)

def lam(m, f, w):
    s = 0
    for i in range(len(f)):
        s += f[i] * w[i]

    return float(m)/float(s)


def w(l, es):
    return (1 + l) * es

def l(lam, f, w):
    return lam * f * w

res = [[3, 6, 15, 20]]
lmb = lam(1, f, es)
res[0].append(lmb)
res[0].append(l(lmb, f[0], es[0]))
res[0].append(l(lmb, f[1], es[1]))
res[0].append(l(lmb, f[2], es[2]))
res[0].append(l(lmb, f[3], es[3]))

for i in range(1, 5):
    m = i+1
    prev = res[i-1]

    c = []
    for j in range(len(es)-1):
        # print str(6 + j)
        c.append(w(prev[5 + j], es[j]))
    c.append(es[3])

    lmb = lam(m, f, c)
    c.append(lmb)

    for j in range(len(es)):
        c.append(l(lmb, f[j], c[j]))

    res.append(c)

for row in res:
    print row
