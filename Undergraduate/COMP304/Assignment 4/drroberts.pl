% printSentence takes a list and writes all the elements of the list to the output
% if we encouter the element qm we write '?' insted of just writing qm.
printSentence([]).
printSentence([qm|Tail]) :- write(?), write(" "), printSentence(Tail).
printSentence([Head|Tail]) :- \+(Head = qm), write(Head), write(" "), printSentence(Tail).

% Used for testing, takes a question list, finds the reply and prints it.
printReply(Question) :- answer(Question, Reply), printSentence(Reply).

% Answer is simply an alias for match, wasnt sure if automatic testing was going
% to be used.
answer(Question, Reply) :- match(Question, Reply).

% match is a relation between a question string and a reply string. It matches
% keywords in the beginning of the question string to the beginning of the Reply
% string. The Rest varaible contains the part of the question we want to transform
% and give back to the user, so we use the build predicate to give us a translated
% version and this version is assigned to the Transformed variable. The build
% predicate also allows us to append to the end of the string, e.g. to add a
% question mark.
match([i, feel | Rest], [what, makes, you, feel | Transformed]) :- build(Rest, [qm], Transformed).
match([i, fantasised, about | Rest], [have, you, fantasised, about | Transformed]) :- build(Rest, [before, qm], Transformed).
match([i, know | Rest], [are, you, sure, you, know, that | Transformed]) :- build(Rest, [qm], Transformed).
match([i, dont, like | Rest], [why, dont, you, like | Transformed]) :- build(Rest, [qm], Transformed).
match([i, dreamt, about | Rest], [have, you, dreamt, about | Transformed]) :- build(Rest, [before, qm], Transformed).

% the build predicate has three variables, the first two Head and End are two
% lists of words which we append together, We then use the translate predicate
% on the resultant list to get the Complete variable.
build(Head, End, Complete) :-
  append(Head, End, Pre),
  transform(Pre, Complete).

% transform is a relaton between two lists where the two lists are related iff
% you can transform the first word of one to the first word of the other or the
% two first words cant be transformed to one and other but they are equal, and the
% two lists with first letters removed are related.
transform([], []).
transform([X|O], [Y|T]) :- \+transformWord(X, Y), X = Y, transform(O, T).
transform([X|O], [Y|T]) :- transformWord(X, Y), transform(O, T).

% transformWord takes a word from a question and transforms it to a word in the
% reply.
transformWord(i, you).
transformWord(my, your).
transformWord(you, me).
transformWord(am, are).


% Tests %
%%%%%%%%%

% Tests for build
:- build([i, am], [cool], [you, are, cool]).

% Tests for transform
:- transform([non, transformable, words], [non, transformable, words]).
:- transform([i, you, my, am], [you, me, your, are]).

% Tests for answer (and match)
:- answer([i, feel, good], [what, makes, you, feel, good, qm]).
:- answer([i, fantasised, about, fast, cars], [have, you, fantasised, about, fast, cars, before, qm]).
:- answer([i, know, i, am, cool], [are, you, sure, you, know, you, are, cool, qm]).
:- answer([i, dont, like, you], [why, dont, you, like, me, qm]).
