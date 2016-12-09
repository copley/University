mergeSort :: (Ord a) => [a] -> [a]
mergeSort [] = []
mergeSort (x:[]) = [x]
mergeSort (x:y:[]) = if x > y then [y,x] else [x,y]
mergeSort a =
  let mid = (length a) `div` 2
      l = take mid a
      r = drop mid a
  in merge (mergeSort l) (mergeSort r)
  where merge [] [] = []
        merge [] x = x
        merge x [] = x
        merge allX@(x:xs) allY@(y:ys)
          | x > y = y:(merge allX ys)
          | otherwise = x:(merge xs allY)
