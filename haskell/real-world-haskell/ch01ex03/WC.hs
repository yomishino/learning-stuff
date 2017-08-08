{-
    Author: yomishino

    Chapter 1, Exercise 1
    Counting the number of words in a file.
    
    The contents of quux.txt are from the book.
-}

main = interact count 
    where count input = show (length (words input)) ++ "\n"
