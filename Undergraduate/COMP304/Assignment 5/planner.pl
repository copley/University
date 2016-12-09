road(wellington, palmerstonNorth, 143).
road(palmerstonNorth, wanganui, 74).
road(palmerstonNorth, napier, 178).
road(palmerstonNorth, taupo, 259).
road(wanganui, taupo, 231).
road(wanganui, newPlymouth, 163).
road(wanganui, napier, 252).
road(napier, taupo, 147).
road(napier, gisborne, 215).
road(newPlymouth, hamilton, 289).
road(newPlymouth, taupo, 289).
road(taupo, hamilton, 153).
road(taupo, rotorua, 82).
road(taupo, gisborne, 334).
road(gisborne, rotorua, 291).
road(rotorua, hamilton, 109).
road(hamilton, auckland, 126).

% The joined predicate abstracts the road predicate, with the idea that the
% roads go both ways. joined(X, Y, C) scuseeds iff road(X, Y, C) or road(Y, X, C)
% scuseeds.
joined(X, Y, C) :- road(X, Y, C);
                road(Y, X, C).

% Let Visits = {start, v2, ..., finish}, The path predicate scuseeds iff start -> v1
% v1 -> ... -> finish is a valid path between the start and finish citys.
% We recursively procede through all the connected roads
% until we reach the base case (where start = finish). Along the way we only
% visit each city at most once to avoid cycles in the path.
path(Start, Finish, _, Visits) :-
  Start = Finish,
  Visits = [Finish].
path(Start, Finish, Used, Visits) :-
  \+(Start = Finish),
  joined(Start, Next, _),
  \+member(Next, Used),
  path(Next, Finish, [Start|Used], Visited),
  Visits = [Start | Visited].

% The route\3 predicate simply acts as an alias for path
route(Start, Finish, Visits) :-
  path(Start, Finish, [], Visits).

% The route\4 predicate builds apon the original route\3 by scuseeding iff
% route scuseeds and the sum of path lengths is equal to the required distance.s
route(Start, Finish, Visits, Distance) :-
  route(Start, Finish, Visits),
  pathcost(Visits, Distance).


% The choice predicate scuseeds iff RaD is the list all paths and distances
% (as tuples) from start to finish. Works by simply using the findall predicate.
choice(Start, Finish, RaD) :-
  findall((X, C), (
    route(Start, Finish, X),
    pathcost(X, C)
  ), RaD).


% The pathcost predicate scuseeds iff the sum of lengths in the path is equal
% to the given distance. Works by iterating through all the citys and adding
% the cost of the current road to the cost of the rest of the roads. Base case is
% when there is only one city left.
pathcost([_], 0).
pathcost([S1, S2 | Rest], Total) :-
  joined(S1, S2, Cost),
  pathcost([S2|Rest], Length),
  Total is Cost + Length.

% The via predicate scuseeds iff RaD is the complete list of paths and distances
% between start and finish that go via the citys in via. Uses the helper predicate
% checkvia which filters the list of paths to the ones that go via the required
% citys
via(Start, Finish, Via, RaD) :-
  choice(Start, Finish, All),
  checkvia(All, Via, RaD).

% We have two recurcive cases in the checkvia predicate, one if the current path
% goes via the required citys and one if it dosnt. In ether case we scuseeds iff
% RaD is the filtered version of Rest.
checkvia([], _, []).
checkvia([(Path, Cost)|Rest], Via, RaD) :-
  containsall(Path, Via),
  checkvia(Rest, Via, Checked),
  RaD = [(Path, Cost)|Checked].
checkvia([(Path, _)|Rest], Via, Checked) :-
  \+containsall(Path, Via),
  checkvia(Rest, Via, Checked).

% The avoiding predicate if RaD is the list of paths from start to finish which
% avoid the citys in the list Avoiding. Using the predicate check avoid to filter
% the list of paths.
avoiding(Start, Finish, Avoiding, RaD) :-
  choice(Start, Finish, All),
  checkavoid(All, Avoiding, RaD).

% Here we have two recursive cases, first if the first path avoids the required
% paths and the second for if the path does not avoid all the required paths.
% In ether case it scuseeds if RaD is the filterd version of Rest
checkavoid([], _, []).
checkavoid([(Path, Cost)|Rest], Avoiding, RaD) :-
  \+containsone(Path, Avoiding),
  checkavoid(Rest, Avoiding, Checked),
  RaD = [(Path, Cost)|Checked].
checkavoid([(Path, _)|Rest], Avoiding, Checked) :-
  containsone(Path, Avoiding),
  checkavoid(Rest, Avoiding, Checked).

% The containsone predicate scuseeds iff at least one element of the second
% list is contained in the first.
containsone(L, [Head|Rest]) :-
  member(Head, L);
  containsone(L, Rest).

% The containsall predicate scuseeds iff the first list contains all the elements
% of the second list.
containsall(_, []).
containsall(L, [Head|Rest]) :-
  member(Head, L),
  containsall(L, Rest).

%% Tests %%


% Joined should be symmetrical
:- joined(napier, taupo, 147).
:- joined(taupo, napier, 147).

% Paths should work and then if we reverse the start and finish then the reverse
% paths should work.
:- route(napier, auckland, [napier, taupo, hamilton, auckland]).
:- route(napier, auckland, [napier, taupo, rotorua, hamilton, auckland]).
:- route(auckland, napier, [auckland, hamilton, taupo, napier]).
:- route(auckland, napier, [auckland, hamilton, rotorua, taupo, napier]).

% Choice should give all the paths between two citys with distances.
:- choice(wellington, palmerstonNorth, [([wellington, palmerstonNorth], 143)]).

% Path cost should correctly find the path cost
:- pathcost([wellington, palmerstonNorth], 143).
:- pathcost([wanganui, taupo, hamilton], 384).

% Via
:- via(hamilton, auckland, [hamilton], [([hamilton, auckland], 126)]).

% Avoiding
:- avoiding(napier, taupo, [gisborne, wanganui],  [([napier, taupo], 147), ([napier, palmerstonNorth, taupo], 437)]).

% containsone
:- containsone([a,b,c], [a]).
:- containsone([a,b,c], [a, b]).
:- containsone([a,b,c], [c]).
:- containsone([a,b,c], [a, k, j]).

% containsall
:- containsall([a,b,c], [a]).
:- containsall([a,b,c], [a,b]).
:- containsall([a,b,c], [a,c]).
