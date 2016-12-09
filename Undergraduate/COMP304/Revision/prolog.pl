% Problem 1
my_last(X, [X|[]]).
my_last(X, [_|Y]) :- my_last(X, Y).

% Problem 2
my_but_last(X, [X|[_|[]]]).
my_but_last(X, [_|Y]) :- my_but_last(X, Y).

% Problem 3
element_at(X, [X|_], 1).
element_at(X, [_|Y], N) :- M is (N-1), element_at(X, Y, M).

% Problem 4
my_length(1, [_|[]]).
my_length(N, [_|Y]) :- my_length(M, Y), N is M + 1.

% Problem 5
my_reverse(L1, L2) :- my_rev(L1, L2, []).
my_rev([], L2, L2).
my_rev([X|Xs], L2, Acc) :- my_rev(Xs, L2, [X|Acc]).

% Problem 6
is_palindrome(L1) :- my_reverse(L1, L1).

% Problem 7
my_flatten([], []).
my_flatten([X|Y], [X|F]) :-
  \+is_list(X),
  my_flatten(Y, F).
my_flatten([X|Y], F) :-
  is_list(X),
  my_flatten(X, A),
  my_flatten(Y, Z),
  append(A, Z, F).

% Problem 8
compress([], []).
compress([X], [X]).
compress([X,X|Xs], Zs) :- compress([X|Xs], Zs).
compress([X,Y|Ys], [X|Zs]) :- X \= Y, compress([Y|Ys], Zs).

% Problem 9
pack([], []).
pack([X|Xs], [Z|Zs]) :- strip(X, Xs, Ys, Z), pack(Ys, Zs).

strip(X, [], [], [X]).
strip(X, [Y|Ys], [Y|Ys], [X]) :- X \= Y.
strip(X, [X|Ys], R, [X|P]) :- strip(X, Ys, R, P).

% Problem 10
encode(X, E) :- pack(X, P), enc(P, E).

first([X|_], X).

enc([], []).
enc([X|Xs], [[L, Elm]|Es]) :- length(X, L), first(X, Elm), enc(Xs, Es).

% Problem 11
encode_modified(X, Em) :- encode(X, E), encm(E, Em).
encm([], []).
encm([[N, X]|Xs], [X|Es]) :- N is 1, !, encm(Xs, Es).
encm([[N, X]|Xs], [[N, X]|Es]) :- \+(N is 1), encm(Xs, Es).

% Problem 12
decode([], []).
decode([X|Xs], D) :- is_list(X), !, replicate(X, R), decode(Xs, Ds), append(R, Ds, D).
decode([X|Xs], [X|D]) :- decode(Xs, D).

replicate([N, _], []) :- N is 0, !.
replicate([N, X], [X|Xs]) :- M is N-1, replicate([M, X], Xs).

% Problem 13
encode_direct([], []).
encode_direct([X|Xs], [E|Es]) :- count(X, Xs, 1, Ys, E), encode_direct(Ys, Es).

count(X, [], 1, [], X) :- !.
count(X, [], N, [], [N, X]) :- !.
count(X, [X|Xs], N, Ys, E) :- M is N+1, count(X, Xs, M, Ys, E).
count(X, [Y|Xs], 1, [Y|Xs], X) :- X \= Y, !.
count(X, [Y|Xs], N, [Y|Xs], [N, X]) :- X \= Y, !.
