{-
    Author: yomishino

    Chapter 3, Exercise 2
    Define a tree type that has only one constructor,
    using the Maybe type to refer to a node's children.
-}

data Tree a = Node (Maybe a)
    deriving (Show)
