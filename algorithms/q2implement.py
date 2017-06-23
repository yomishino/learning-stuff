'''
An implementation of the algorithm ``ParetoOptimal`` for Question 2
of Algorithms & Complexity (Semester 1, 2017) Assignment 2.
'''

__author__ = "yomishino"
__version__ = "1.0"


def test():
    '''
    A test suit.
    Example points provided in the assignment handout.
    '''
    points = [(4, 3, "A"), (6, 6, "B"), (4, 8, "C"), (5, 10, "D"),\
              (8, 7, "E"), (10, 4, "F"), (11, 2, "G"), (2, 1, "H"),\
              (2, 6, "I"), (1, 11, "J")]
    results = pareto_optimal(points)
    print "Input points: ", points
    print "Pareto optimal points: ", results


def pareto_optimal(points):
    '''
    Returns a list of Pareto optimal points for
    the given set of points.
    '''
    ptlist = list(points)
    # Sorting by x-coordinates
    mergesort_by_x(ptlist)
    # Scan the list from right to left
    ptlist.reverse()
    opt = []
    max_y = float("-inf")
    for pnt in ptlist:
        if pnt[1] > max_y:
            # Pick the point whose y-coordinate is larger than
            # the point to its right
            max_y = pnt[1]
            opt.append(pnt)
    return opt


def mergesort_by_x(pts):
    '''
    MergeSort on points by their *x*-coordinates.
    ``pts`` is a list.

    The sort is perform on the list of points itself.
    '''
    if len(pts) > 1:
        mid = len(pts) / 2
        left = pts[0:mid]
        right = pts[mid:]
        mergesort_by_x(left)
        mergesort_by_x(right)
        merge(left, right, pts)


def merge(left, right, org):
    ''' Merge the lists. '''
    idx = 0
    while len(left) > 0 and len(right) > 0:
        if left[0][0] <= right[0][0]:
            org[idx] = left.pop(0)
        else:
            org[idx] = right.pop(0)
        idx += 1
    while len(left) > 0:
        org[idx] = left.pop(0)
        idx += 1
    while len(right) > 0:
        org[idx] = right.pop(0)
        idx += 1


test()
