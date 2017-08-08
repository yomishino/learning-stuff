{-
    Author: yomishino

    Chapter 1, Exercise 4
    Count the number of characters in a file.
    
    The contents of quux.txt are from the book.
-}

main = interact count
    where count input = show (length input) ++ "\n"
