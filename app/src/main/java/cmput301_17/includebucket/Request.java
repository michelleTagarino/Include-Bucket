package cmput301_17.includebucket;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;

import io.searchbox.annotations.JestId;

/**
 * Request
 *
 * This is the Request class. It holds the data that is attached to each request made by a specific
 * UserAccount.
 */
public class Request implements Serializable {

    @JestId
    String requestID;

    private GeoPoint location;
    private GeoPoint endLocation;
    private String startAddress;
    private String endAddress;
    private UserAccount rider;
    private UserAccount driver = new UserAccount();
    private String riderStory = null;
    private double fare;
    private double roadLength;
    private ArrayList<String> keywords;
    private Collection<UserAccount> pendingDrivers;
    private boolean driverAccepted;
    private boolean riderAccepted;
    private boolean isCompleted, isPaid;
    private UserAccount chosenDriver;

    protected ArrayList<Listener> listeners;

    /**
     * Enums that specify the state of the status.
     * A request can be:
     *     Open (Rider just made a ride request, no driver has accepted it)
     *     Accepted by driver (A driver accepted the open request)
     *     Pending (A request that was accepted by a driver is waiting to be confirmed by the rider)
     *     Confirmed and completed (The rider confirmed that driver's acceptance and payment is completed)
     */
    public enum RequestStatus {
        Open, PendingDrivers, Closed
    }

    private RequestStatus requestStatus = RequestStatus.Open;

    /**
     * The empty constructor.
     */
    public Request() {
        listeners = new ArrayList<>();
    }

    public Request(String story) {
        this.riderStory = story;
        listeners = new ArrayList<>();
    }
    /**
     * Intantiates a new Request.
     * @param loc1  The start location
     * @param loc2  The end location
     * @param rider The rider making a request
     * @param story The rider's story (where is the rider going?)
     */
    public Request(GeoPoint loc1, GeoPoint loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver, String startAddress, String endAddress, Double fare, Double distance) {
        this.requestID = null;
        this.location = loc1;
        this.endLocation = loc2;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.fare = fare;
        this.rider = rider;
        this.riderStory = story;
        this.pendingDrivers = pendingDrivers;
        this.driver = driver;
        this.requestStatus = RequestStatus.Open;
        listeners = new ArrayList<>();
    }

    public Request(String loc1, String loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver, String startAddress, String endAddress, Double fare, String id) {
        this.requestID = id;
        this.location = new GeoPoint(Double.parseDouble(loc1.split(",")[0]), Double.parseDouble(loc1.split(",")[1]));
        this.endLocation = new GeoPoint(Double.parseDouble(loc2.split(",")[0]), Double.parseDouble(loc2.split(",")[1]));
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.fare = fare;
        this.rider = rider;
        this.riderStory = story;
        this.pendingDrivers = pendingDrivers;
        this.driver = driver;
        this.requestStatus = RequestStatus.Open;
        listeners = new ArrayList<>();
    }

    public Request(GeoPoint loc1, GeoPoint loc2, UserAccount rider, String story, ArrayList<UserAccount> pendingDrivers, UserAccount driver, String startAddress, String endAddress, Double fare, String id) {
        this.requestID = id;
        this.location = loc1;
        this.endLocation = loc2;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.fare = fare;
        this.rider = rider;
        this.riderStory = story;
        this.pendingDrivers = pendingDrivers;
        this.driver = driver;
        this.requestStatus = RequestStatus.Open;
        listeners = new ArrayList<>();
    }

    private void notifyListeners() {
        for (Listener listener : getListeners()) {
            listener.update();
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        return listeners;
    }

    public String getRequestID() {return requestID; }

    public String getStartLocation() {
        return location.toString();
    }

    public void setStartLocation(String loc1) {
        this.location= new GeoPoint(Double.parseDouble(loc1.split(",")[0]), Double.parseDouble(loc1.split(",")[1]));;
        notifyListeners();
    }

    public void setStartLocation(GeoPoint startLocation) {
        this.location = startLocation;
        notifyListeners();
    }


    public String getEndLocation() {
        return endLocation.toString();
    }

    public void setEndLocation(String loc2) {
        this.endLocation= new GeoPoint(Double.parseDouble(loc2.split(",")[0]), Double.parseDouble(loc2.split(",")[1]));
        notifyListeners();
    }

    public void setEndLocation(GeoPoint endLocation) {
        this.endLocation = endLocation;
        notifyListeners();
    }

    public String getRiderStory() {
        return riderStory;
    }

    public void setRiderStory(String riderStory) {
        this.riderStory = riderStory;
        notifyListeners();
    }

    public UserAccount getRider() {
        return rider;
    }

    public void setRider(UserAccount rider) {
        this.rider = rider;
        notifyListeners();
    }

    //public String getRiderUserName() { return getRider().getUniqueUserName(); }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
        notifyListeners();
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public Collection<UserAccount> getDrivers() {
        return pendingDrivers;
    }

    public void setDrivers(Collection<UserAccount> drivers) {
        this.pendingDrivers = pendingDrivers;
    }

    public void clearDrivers(){
        this.pendingDrivers.clear();
    }

    public void addDriver(UserAccount driver){
        this.pendingDrivers.add(driver);
        notifyListeners();
    }

    public void removeDriver(UserAccount driver){
        this.pendingDrivers.remove(driver);
    }

    public boolean isDriverAccepted() {
        return driverAccepted;
    }

    public void setDriverAccepted(boolean driverAccepted) {
        this.driverAccepted = driverAccepted;
        notifyListeners();
    }

    public boolean hasRiderAccepted() {
        return riderAccepted;
    }

    public void setRiderAccepted(boolean riderAccepted) {
        this.riderAccepted = riderAccepted;
        notifyListeners();
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        notifyListeners();
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
        notifyListeners();
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
        notifyListeners();
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
        notifyListeners();
    }

    public double getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(double roadLength) {
        this.roadLength = roadLength;
        notifyListeners();
    }

    public void setUser(UserAccount user){
        this.rider = user;
        notifyListeners();
    }

    public UserAccount getDriver() {
        return driver;
    }

    public void setDriver(UserAccount driver) {
        this.driver = driver;
    }

    public UserAccount getChosenDriver() {
        return chosenDriver;
    }

    public void setChosenDriver(UserAccount user){
        this.chosenDriver = user;
        notifyListeners();
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        notifyListeners();
    }

    @Override
    public String toString() {


        String status = this.requestStatus.toString();

        if (isDriverAccepted() && requestStatus != RequestStatus.Closed) {
            if(getDrivers().size()== 1)
            {
                status = "1 Pending Driver";
            }
            else
            {
                status = getDrivers().size() +" Pending Drivers";
            }
        }

        Formatter formatter = new Formatter();
        String p = formatter.format("%.2f%n", getFare()).toString();

        notifyListeners();
        return "\"" + getRiderStory() + "\"" + "\n" + "Price: $" + p + "\nStatus: " + status;
    }
}