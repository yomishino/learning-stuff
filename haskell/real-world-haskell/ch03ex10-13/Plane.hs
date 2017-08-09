{-
    Author: yomishino

    Chatper 3, Ex 10--13 (end of chap.)
    - [x] Define a Direction data type representing the turn 
          from one line segment to another. (ex10)
    - [x] Calculates the turn made by three 2D points. (ex11)
    - [x] Computes the direction of each successive triple in a list
          of 2D points. (ex12)
    - [ ] Implements Graham's scan algorithm for the convex hull
          of a set of 2D points. (ex13)
-}

data Point = Point {
        ptX :: Double,
        ptY :: Double
    } deriving (Show, Eq)

data Direction = TurnLeft | TurnRight | StraightLine
    deriving Show


-- | Calculates the turn made by three two-dimensional points.
-- More specifically, the function determines, 
-- given points /a/, /b/, /c/,
-- whether it turns left, turns right, or forms a straight line
-- from the line segment /ab/ to the line segment /bc/.
whichDirection :: Point -> Point -> Point -> Direction
whichDirection a b c
    | cross > 0     = TurnLeft
    | cross < 0     = TurnRight
    | otherwise     = StraightLine
    where cross = (x2 - x1) * (y3 - y2) - ((y2 - y1) * (x3 - x2))
          x1 = ptX a
          x2 = ptX b
          x3 = ptX c
          y1 = ptY a
          y2 = ptY b
          y3 = ptY c
            -- make use of cross product


-- | Given a list of two-dimensional points, computes the direction
-- of each successive triple. 
-- The function returns a list of Direction, in which each element
-- indicates the direction of the corresponding triple.
-- If the input list contains less than three points,
-- then the empty list is returned.
directions :: [Point] -> [Direction]
directions (x:(y:(z:zs))) = (whichDirection x y z):(directions (y:(z:zs)))
directions _ = []
