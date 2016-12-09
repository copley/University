-- Problem 1
myLast :: [a] -> a
myLast (x:[]) = x
myLast (x:xs) = myLast xs

-- Problem 2
myButLast :: [a] -> a
myButLast (x:[y]) = x
myButLast (x:xs) = myButLast xs

-- Problem 3
elementAt :: [a] -> Int -> a
elementAt (x:xs) 1 = x
elementAt (x:xs) n = elementAt xs (n-1)

-- Problem 4
myLength :: [a] -> Int
myLength [] = 0
myLength (x:xs) = 1 + (myLength xs)

-- Problem 5
myReverse :: [a] -> [a]
myReverse [] = []
myReverse (x:xs) = (myReverse xs) ++ [x]

-- Problem 6
isPalindrome ::(Eq a) => [a] -> Bool
isPalindrome x = x == (myReverse x)

-- Problem 7
data NestedList a = Elem a | List [NestedList a]

flatten :: NestedList a -> [a]
flatten (Elem x) = [x]
flatten (List xs) = f xs
  where f [] = []
        f (e:elems) = (flatten e) ++ (f elems)

-- Problem 8
compress :: (Eq a) => [a] -> [a]
compress (x:xs) = c x xs
  where c e [] = [e]
        c e (y:ys)
          | e == y = c e ys
          | otherwise = e:(c y ys)

-- Problem 9
pack :: (Eq a) => [a] -> [[a]]
pack [] = []
pack (x:xs) = p xs x [x]
  where p [] _ c = [c]
        p (y:ys) elm c
          | y == elm = p ys elm (elm:c)
          | otherwise = c:(p ys y [y])

-- Problem 10
encode :: (Eq a) => [a] -> [(Int, a)]
encode x = map (\s@(y:ys) -> (length s, y)) (pack x)

-- Problem 11
data ListItem a = Single a | Multiple Int a deriving(Show)

encodeModified :: (Eq a) => [a] -> [ListItem a]
encodeModified x = map listItem encoded
  where encoded = encode x
        listItem (1, x) = (Single x)
        listItem (n, x) = (Multiple n x)

-- Problem 12
decode :: [ListItem a] -> [a]
decode [] = []
decode (x:xs) = (r x) ++ (decode xs)
  where r (Single x) = [x]
        r (Multiple 1 x) = [x]
        r (Multiple n x) = x:(r (Multiple (n-1) x))

-- Problem 13
encodeDirect :: (Eq a) => [a] -> [ListItem a]
encodeDirect (x:xs) = encodeDirect' xs x 1 []

encodeDirect' :: (Eq a) => [a] -> a -> Int -> [ListItem a] -> [ListItem a]
encodeDirect' [] x n e = (li n x):e
encodeDirect' (x:xs) y n e
  | x == y = encodeDirect' xs y (n+1) e
  | x /= y = encodeDirect' xs x 1 ((li n y):e)

li 1 x = (Single x)
li n x = (Multiple n x)

-- Problem 14
dupli :: [a] -> [a]
dupli (x:xs) = x:x:(dupli xs)

-- Problem 15
repli :: [a] -> Int -> [a]
repli [] _ = []
repli (x:xs) n = (r x n) ++ (repli xs n)
  where r x 0 = []
        r x n = x:(r x (n-1))
