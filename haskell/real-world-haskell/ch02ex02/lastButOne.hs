{-
    Author: yomishino

    Chapter 2, Exercise 2
    Write a function that returns the element before the last.
-}

lastButOne :: [a] -> a
lastButOne [] = error "list too short"

lastButOne (x:xs)
    | len == 2  = x
    | otherwise = lastButOne xs
    where len = length (x:xs)
