minList([E], E).
minList([E1,E2|T], E) :- E1>E2, minList([E2|T], E).
minList([E1,E2|T], E) :- E1=<E2, minList([E1|T], E).
