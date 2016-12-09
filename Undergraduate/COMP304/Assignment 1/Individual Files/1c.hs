firstLastPos :: (Eq a) => a -> [a] -> (Int, Int)
firstLastPos e a = (firstPos a 0, lastPos a  ((length a) - 1))
    where firstPos [] _ = 0
          firstPos (x:xs) i
            | e == x = i + 1
            | otherwise = firstPos xs (i+1)
          lastPos [] _ = 0
          lastPos (x:xs) i
            | e == x = i + 1
            | otherwise = firstPos xs (i-1)
