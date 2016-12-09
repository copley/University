data MapElement a b = Pair a b deriving (Show)
type Map a b = [MapElement a b]

hasKey :: (Eq a) => a -> Map a b -> Bool
hasKey x m = any (\(Pair k v) -> x == k) m

setKey :: (Eq a) => a -> b -> Map a b -> Map a b
setKey k v m
  | hasKey k m = setKey k v (delKey k m)
  | otherwise = (Pair k v):m


getVal :: (Eq a) => a -> Map a b -> b
getVal k m
  | not (hasKey k m) = error "Map does not contain that key"
  | otherwise = find m--foldl (\acc (Pair x y) -> if x == k then y else acc)  m
  where find ((Pair x y):xs) = if k == x then y else find xs

delKey :: (Eq a) => a -> Map a b -> Map a b
delKey k m
  | not (hasKey k m) = error "Map does not contain that key"
  | otherwise = foldl (\acc e@(Pair x y) -> if x == k then acc else e:acc ) [] m


-- 4
buildMap :: (Eq a) => [a] -> Map a Int
buildMap xs = map (\g -> Pair (head g) (length g)) (group xs)
  where uniquic xs = foldl (\acc e -> if e `elem` acc then acc else e:acc) [] xs
        collect a xs = foldl (\acc e -> if e == a then e:acc else acc) [] xs
        group xs = map (\e -> collect e xs) (uniquic xs)
