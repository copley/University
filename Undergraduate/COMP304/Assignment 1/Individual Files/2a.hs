sort1 :: (Ord a) => [a] -> [a]
sort1 = selectionSort

selectionSort :: (Ord a) => [a] -> [a]
selectionSort [] = []
selectionSort x =
  let m = minimum x
      l = takeWhile (/=m) x
      r = drop ((length l) + 1) x
      remaining = l ++ r
  in m:(selectionSort remaining)
