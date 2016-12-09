allPos :: (Eq a) => a -> [a] -> [Int]
allPos e x = collectIndexs x 0
    where collectIndexs [] _ = []
          collectIndexs (y:yx) i
              | e == y = i:(collectIndexs yx (i+1))
              | otherwise  = collectIndexs yx (i+1)
