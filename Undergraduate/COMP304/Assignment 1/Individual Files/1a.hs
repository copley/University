count :: (Eq a) => a -> [a] -> Int
count e x = foldl (\acc a -> if a == e then acc + 1 else acc) 0 x
