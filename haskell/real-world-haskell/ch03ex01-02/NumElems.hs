{-
    Author: yomishino

    Chapter 3, Exercise 1 & 2 (end of chap.)
    Write a function that computes the number of elements in a list.
-}

-- | Computes the number of elements in the input list.
numElems :: [a] -> Int
numElems [] = 0
numElems (x:xs) = 1 + numElems xs
