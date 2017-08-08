{-
    Author: yomishino

    Chapter 3, Exercise 1 (halfway)
    Write a function that takes a List a and generates a [a].
-}

-- Definition of List a is given in the book.
data List a = Cons a (List a)
            | Nil
            deriving (Show)


-- | Generates a [a] from a List a.
toList :: List a -> [a]
toList Nil = []
toList (Cons a as) = a:(toList as)
