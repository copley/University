queens(0, []).
queens(N, [Column|Board]) :- N>0, N1 is N-1, queens(N1, Board),
                             member(Column, [1,2,3,4,5,6,7,8]),
                             safe(Column, Board).

safe(_, []).
safe(C, CL) :- not(south(C, CL)),
               not(southWest(C, CL)),
               not(southEast(C, CL)).

south(C, CL) :- member(C, CL).

southWest(C, [NC|_]) :- NC is C-1.
southWest(C, [_|L])
