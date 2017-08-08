{-
    Author: yomishino

    Chapter 3, Exercise 6 (end of chap.)
    Create a function that sorts a list of lists based 
    on the length of each sublist.
-}

import Data.List

-- |Sorts a list of lists based on the length of each sublist
-- in ascending order.
sortBySublist :: Ord a => [[a]] -> [[a]]
sortBySublist xs = sortOn (length) xs
