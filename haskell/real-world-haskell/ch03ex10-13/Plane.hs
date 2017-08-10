{-
    Author: yomishino

    Chatper 3, Ex 10--13 (end of chap.)
    - [x] Define a Direction data type representing the turn 
          from one line segment to another. (ex10)
    - [x] Calculates the turn made by three 2D points. (ex11)
    - [x] Computes the direction of each successive triple in a list
          of 2D points. (ex12)
    - [x] Implements Graham's scan algorithm for the convex hull
          of a set of 2D points. (ex13)
-}

import Data.List


data Point = Point {
        ptX :: Double,
        ptY :: Double
    } deriving (Show, Eq)

data Direction = LeftTurn | RightTurn | StraightLine
    deriving (Show, Eq)


-- | Calculates the turn made by three two-dimensional points.
-- More specifically, the function determines, 
-- given points /a/, /b/, /c/,
-- whether it turns left, turns right, or forms a straight line
-- from the line segment /ab/ to the line segment /bc/.
whichDirection :: Point -> Point -> Point -> Direction
whichDirection a b c
    | cross > 0     = LeftTurn
    | cross < 0     = RightTurn
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


-- |Finds the convex hull of the given list of points.
-- The returned list contains points that lie on the boundary of 
-- the convex hull.
-- If three or more points that are on the boundary also lie
-- on the same line, then the point(s) inbetween will not be included
-- in the returned list.
-- If the given points cannot make a convex hull, 
-- the empty list is returned.
-- 
-- This function implements the Graham's Scan.
-- See, for example, <https://en.wikipedia.org/wiki/Graham_scan>.
convexHull :: [Point] -> [Point]
convexHull ps =
    let 
        gScan qs = 
            case qs of
                [x,y,z] -> 
                    if dList == [LeftTurn]
                    then [x,y]  -- Exclude the last point since it is
                                -- a duplicate of the bottommost point.
                    else []
                x:(y:ys) ->
                    if head dList == LeftTurn
                    then x:(gScan (y:ys))
                    else gScan (x:ys)
                _ -> []
            where dList = directions qs
    in case ps of
        x:(y:(z:zs)) -> gScan (sorted ++ [bottommost ps])
                        -- It may not work properly for some set of points
                        -- if we do not append a duplicate at the end?
        _            -> [] 
    where sorted = sortByAngleFromBottommost ps


-- | Finds the bottommost point in the given list of points,
-- that is, the point with the lowest y-coordinate.
-- If more than one points have the lowest y-coordinate,
-- then the one with the lowest x-coordinate is picked.
bottommost :: [Point] -> Point
bottommost [] = error "no points given"
bottommost ps = minimumBy compareYX ps
    where compareYX p q = compare (ptY p, ptX p) (ptY q, ptX q)


-- | Sorts the given points in increasing order of the angle made by 
-- the line from the bottommost point to each point in the set 
-- (excluding the bottomost point itself) and the x-axis.
-- 
-- The bottommost point is the one that would be picked by 
-- the function 'bottommost', and it is always the first point
-- in the returned list.
sortByAngleFromBottommost :: [Point] -> [Point] 
sortByAngleFromBottommost [] = [] 
sortByAngleFromBottommost ps = [p0] ++ (sortPts (filter (/= p0) ps))
    where p0 = bottommost ps
          sortPts qs =
            let slope j = ((ptY j) - (ptY p0)) / ((ptX j) - (ptX p0))
            in let minusReSlope k = - (1 / slope k)
            in sortOn minusReSlope qs
    -- Here instead of calculating the angles, calculate the slope
    -- of each line and construct a monotonic function based on it,
    -- which also gives a list of points in increasing order of
    -- the angles.
