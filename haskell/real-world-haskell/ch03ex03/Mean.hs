{-
    Author: yomishino

    Chapter 3, Exercise 3 (end of chap.)
    Write a function that computes the mean of a list.
-}

-- |Computes the mean of the list.
mean :: Fractional a => [a] -> a
mean [] = error "empty list"
mean xs = sum xs / fromIntegral (length xs)
