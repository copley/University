likes(jim, music).
likes(jim, fishing).
likes(jim, reading).
likes(liz, reading).
likes(liz, dancing).
likes(ann, swimming).
likes(ann, dancing).


merge(R, [], R).
merge([], R, R).
marge([X|L1], [Y|L2], [Z|R]) :- leq(X,Y), Z = X, merge(L1, [Y|L2], R).
marge([X|L1], [Y|L2], [Z|R]) :- leq(Y,X), Z = Y, merge([X|L1], L2, R).


pre([], _).
pre([X|Y], [X|Z]) :- pre(Y, Z).
