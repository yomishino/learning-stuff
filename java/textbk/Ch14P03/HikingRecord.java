import java.util.ArrayList;

/**
 * A class for managing the hiking record. Each object represents a record of a hiking.
 * It allows the user to add as many waypoints as possible along some route of hiking.
 * It also provides basic calculation abilities, such as calculating total distance
 * travelled and the average speed of the hiker.
 * @author yomishino
 * @version 1.0
 */
public class HikingRecord {
    /** A list of waypoints added by the user. */
    private ArrayList<WaypointTimestamped> waypoints;
    /** The current unit of length used. */
    private static final String DIST_UNIT = "mile(s)";
    /** The current unit of speed used. */
    private static final String SPEED_UNIT = "mph";
    /** It indicates how many miles/metres 1 coordinate unit represents. */
    private static final double SCALE_FACTOR = 0.1;
    /** The actual time is the timestamp time * TIME_FACTOR. */
    private static final double TIME_FACTOR = 1.0 / 3600.0;
    
    /** 
     * Constructs a <code>HikingRecord</code> object, with no waypoints added yet.
     * Set the current unit and the scale factor to default values, that is,
     * {@value #Unit.MILE} and 0.1.
     */
    public HikingRecord() {
        waypoints = new ArrayList<>();
    }

    /**
     * Adds a timestamped waypoint into the list of waypoints.
     * @param x A <code>double</code> of the <i>x</i>-coordinate of the location.
     * @param y A <code>double</code> of the <i>y</i>-coordiante of the location.
     * @param timestamp A <code>int</code> of the timestamp.
     * @throws NegativeTimestampException if the added timestamp is a negative number.
     * @throws WaypointNotInOrderException if the added waypoint has a timestamp 
     * smaller than the previous one, that is, the waypoint is earlier than the previous
     * added one.
     */
     public void addWaypoint(double x, double y, int timestamp) 
                 throws NegativeTimestampException, WaypointNotInOrderException {
         if (waypoints.size() > 0 && 
                 timestamp < waypoints.get(waypoints.size() - 1).getTimestamp())
             throw new WaypointNotInOrderException(
                 "The given timestamp is smaller than that of the previous waypoint.");
         WaypointTimestamped p = new WaypointTimestamped(x, y, timestamp);
         waypoints.add(p);
    }
        
    /**
     * Resets the record. Clear all the waypoints added to the record.
     */
    public void reset() {
        waypoints.clear();
    }

    /**
     * Returns a string representation of the list of waypoints.
     * @return A <code>String</code> representing the list of waypoints.
     */
    @Override
    public String toString() {
        String s = "";
        for (WaypointTimestamped w : waypoints)
            s += w.toString() + " ";
        return s;
    }
   
    /**
     * Computes the total distance travelled according to the added waypoints.
     * The distance is calculated in the coordinate unit, regardless how many
     * meters/miles one unit represents.
     * @return A <code>double</code> of the distance.
     */
    public double totalDistance() {
        double result = 0.0;
        for (int i = 0; i < waypoints.size() - 1; i++)
            result += waypoints.get(i).distance(waypoints.get(i + 1));
        return result;
    }
    
    /**
     * Computes the actual total distance travelled according to the added 
     * waypoints.
     * The actual distance is the distance calcualted in the coordinate
     * unit times the scale factor, {@link #SCALE_FACTOR}.
     * @return A <code>double</code> of the distance.
     */
    public double totalDistanceActual() {
        return totalDistance() * SCALE_FACTOR;
    }

    /**
     * Gets the actual total distance travelled in <code>String</code> in 
     * a more human-readable manner, including the unit of length.
     * @return A <code>String</code> of the information of the total distance.
     */
    public String totalDistanceToString() {
        return "Total distance travelled: " 
               + String.format("%.2f", totalDistanceActual()) 
               + " " + DIST_UNIT;
    }

    /**
     * Computes the total time elapsed according to the added waypoints.
     * It is in fact the timestamp of the last waypoint added in the list.
     * @return An <code>int</code> of the total time.
     */
    public int totalTime() {
        return waypoints.get(waypoints.size() - 1).getTimestamp();
    }

    /**
     * Computes the actual total time according to the added waypoints,
     * but in the actual unit, calculated using the {@link #TIME_FACTOR}.
     */
    public double totalTimeActual() {
        return ((double) totalTime()) * TIME_FACTOR;
    }

    /**
     * Computes the average speed of the hiker, according to the added
     * waypoints.
     * The distance used in calculation is the actual one, already taken
     * the scale factor into account.
     * @return A <code>double</code> of the average speed.
     */
    public double averageSpeed() {
        return totalDistanceActual() / totalTimeActual();
    }

    /**
     * Gets the average speed of the hiker in <code>String</code> in a more
     * human-readable manner, including the unit of speed.
     * @return A <code>String</code> of the speed information.
     */
    public String averageSpeedToString() {
        return "Average speed: " + String.format("%.2f", averageSpeed())
                + " " + SPEED_UNIT;
    }

    /** A test suit for this class. */
    private static void test1() {
        HikingRecord hr = new HikingRecord();
        System.out.println("Adding the following waypoints:");
        System.out.println("5.0, 10.0, 1200");
        System.out.println("3.0, 8.3, 2500");
        System.out.println("9.0, 11.0, 5000");
        System.out.println("12.7, 6.5, 5500");
        hr.addWaypoint(5.0, 10.0, 1200);
        hr.addWaypoint(3.0, 8.3, 2500);
        hr.addWaypoint(9.0, 11.0, 5000);
        hr.addWaypoint(12.7, 6.5, 5500);
        System.out.println("The record now: " + hr);
        System.out.println(hr.totalDistanceToString());
        System.out.println(hr.averageSpeedToString());
    }

    public static void main(String[] args) {
        test1();
    }
}
