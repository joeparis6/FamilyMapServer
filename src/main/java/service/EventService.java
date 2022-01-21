package service;

import RequestAndResponse.AllEventsResponse;
import RequestAndResponse.EventResponse;
import dao.*;
import model.AuthToken;
import model.Event;
import java.sql.Connection;
import java.util.ArrayList;

public class EventService {

    private Database db = new Database();
    private AuthTokenDAO aDao;
    private EventDAO eDao;
    private AuthToken authTokenObject;
    private Event searchedEvent;
    String userName;
    EventResponse confirmEventService = new EventResponse();
    AllEventsResponse confirmAllEventsService = new AllEventsResponse();
    ArrayList<Event> data;


    public boolean eventService(String authToken) throws DataAccessException {

        Connection conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
        eDao = new EventDAO(conn);

        try {
            authTokenObject = aDao.findUsernameFromAuthToken(authToken);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        if (!(authTokenObject == null)) {
            userName = authTokenObject.getUserName();
        }
        else {
            return false;
        }

        return true;
    }

    /**
     * Returns the single Event object with the specified ID.
     * @param eventID
     * @return a response with the information of the object
     */
    public EventResponse getEvent(String eventID, String authToken) throws DataAccessException {

        if (!eventService(authToken)) {
            confirmEventService = generateFailureResponse("Invalid AuthToken error");
            db.closeConnection(false);
            return confirmEventService;
        }

        try {
            searchedEvent = eDao.find(eventID);
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmEventService = generateFailureResponse("Event not found error");
            db.closeConnection(false);
            return confirmEventService;
        }

        if (!searchedEvent.getUsername().equals(userName)) {
            confirmEventService = generateFailureResponse("Unauthorized user error");
            db.closeConnection(false);
            return confirmEventService;
        }

        confirmEventService = generateSuccessResponse();
        return confirmEventService;
    }

    /**
     * Returns ALL events for ALL family members of the current user.
     * The current user is determined from the provided auth token.
     * @return a response that contains all of the events' information
     */
    public AllEventsResponse getAllEvents(String authToken) throws DataAccessException {

        if (!eventService(authToken)) {
            confirmAllEventsService = generateAllEventsResponse(null, false, "Retrieval error");
            db.closeConnection(false);
            return confirmAllEventsService;
        }

        try {
            data = eDao.getAllEvents(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmAllEventsService = generateAllEventsResponse(null,false, "Retrieval error");
            db.closeConnection(false);
            return confirmAllEventsService;
        }

        confirmAllEventsService = generateAllEventsResponse(data, true, null);
        db.closeConnection(true);
        return confirmAllEventsService;
    }

    public EventResponse generateFailureResponse(String message) throws DataAccessException {
        confirmEventService.setMessage(message);
        confirmEventService.setSuccess(false);
        confirmEventService.setEventID(null);
        confirmEventService.setAssociatedUsername(null);
        confirmEventService.setPersonID(null);
        confirmEventService.setCity(null);
        confirmEventService.setCountry(null);
        confirmEventService.setLatitude(null);
        confirmEventService.setLongitude(null);
        confirmEventService.setEventType(null);
        confirmEventService.setYear(null);
        confirmEventService.setEvents(null);
        return confirmEventService;
    }

    public EventResponse generateSuccessResponse() throws DataAccessException {
        confirmEventService.setMessage(null);
        confirmEventService.setSuccess(true);
        confirmEventService.setEventID(searchedEvent.getEventID());
        confirmEventService.setAssociatedUsername(searchedEvent.getUsername());
        confirmEventService.setPersonID(searchedEvent.getPersonID());
        confirmEventService.setCity(searchedEvent.getCity());
        confirmEventService.setCountry(searchedEvent.getCountry());
        confirmEventService.setLatitude(searchedEvent.getLatitude());
        confirmEventService.setLongitude(searchedEvent.getLongitude());
        confirmEventService.setEventType(searchedEvent.getEventType());
        confirmEventService.setYear(searchedEvent.getYear());
        confirmEventService.setEvents(null);
        db.closeConnection(true);
        return confirmEventService;
    }

    public AllEventsResponse generateAllEventsResponse(ArrayList<Event> events, boolean success, String message) {
        confirmAllEventsService.setEvents(events);
        confirmAllEventsService.setSuccess(success);
        confirmAllEventsService.setMessage(message);
        return confirmAllEventsService;
    }
}
