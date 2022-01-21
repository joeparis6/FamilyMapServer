package service;

import JsonConvert.DataCollection;
import JsonConvert.Location;
import RequestAndResponse.FillRequest;
import RequestAndResponse.FillResponse;

import dao.*;
import model.Event;
import model.Person;
import model.User;

import java.io.IOException;
import java.util.Random;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class Fill {

    Database db = new Database();
    FillResponse confirmFill = new FillResponse();
    UserDAO uDao;
    PersonDAO pDao;
    EventDAO eDao;
    String[] maleNames;
    String[] femaleNames;
    String[] surnames;
    List<Location> locations;
    int generationsToGenerate;
    int gensAdded = 0;
    String userName;
    int birthYear = 1980;
    int marriageYear = 2005;

    DataCollection collection;
    int personCount = 0;
    int eventCount = 0;
    boolean base = true;


    /**
     * Fills a given number of generations for a user's family tree with random data
     * @param r the incoming request
     * @return new register response with information of the registered user
     */
    public FillResponse fill(FillRequest r) throws DataAccessException, IOException {

        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        pDao = new PersonDAO(conn);
        eDao = new EventDAO(conn);
        collection = new DataCollection();
        String originalID;

        if (r.getGenerations() == null) {
            generationsToGenerate = 4;
        }
        else {
            generationsToGenerate = Integer.parseInt(r.getGenerations());
            if (generationsToGenerate < 0) {
                confirmFill = generateFailureResponse("Invalid number of generations");

                return confirmFill;
            }
        }
        userName = r.getUserName();
        User search = null;
        if (uDao.findUser(userName) == null) {
            confirmFill = generateFailureResponse("Invalid username");
            return confirmFill;
        }

        search = uDao.findUser(userName);
        originalID = search.getPersonID();
        pDao.deleteAllPersons(userName);
        eDao.deleteAllEvents(userName);

        Person basePerson = new Person(originalID, userName, search.getFirstName(),
                                       search.getLastName(), search.getGender(), generateUniqueID(),
                                       generateUniqueID(), null);
        insertPerson(basePerson);

        if (generationsToGenerate > 0) {
            createNextGeneration(basePerson);
        }

        db.closeConnection(true);
        confirmFill.setMessage("Successfully added " + personCount + " persons " +
                                "and " + eventCount + " events to the database.");
        confirmFill.setSuccess(true);
        return confirmFill;
    }

    public String generateUniqueID() {
        String uniqueID;
        UUID uuid = UUID.randomUUID();
        uniqueID = uuid.toString(); //UUID.randomUUID().toString().replace("-", "");
        return uniqueID;
    }

    public void createNextGeneration(Person basePerson) throws IOException, DataAccessException {
        birthYear -= 30;
        marriageYear -= 30;
        gensAdded++;
        int tmpBirthYear = birthYear;
        int tmpMarriageYear = marriageYear;
        int tmpGensAdded = gensAdded;


        Person father = createFather(basePerson.getFatherID(), basePerson.getMotherID(), basePerson.getLastName());
        insertPerson(father);
        Person mother = createMother(basePerson.getMotherID(), basePerson.getFatherID());
        insertPerson(mother);

        Location marriageLocation = getRandomLocation();
        Event fatherMarriage = createMarriageEvent(father.getPersonID(), marriageLocation, marriageYear);
        insertEvent(fatherMarriage);
        Event motherMarriage = createMarriageEvent(mother.getPersonID(), marriageLocation, marriageYear);
        insertEvent(motherMarriage);

        if (gensAdded < generationsToGenerate) {
            createNextGeneration(father);
            birthYear = tmpBirthYear;
            marriageYear = tmpMarriageYear;
            gensAdded = tmpGensAdded;
            createNextGeneration(mother);
        }

    }

    public void insertPerson(Person newPerson) throws IOException, DataAccessException {
        try {
            pDao.insert(newPerson);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        personCount++;
        Event birth = createBirthEvent(newPerson.getPersonID(), birthYear);
        insertEvent(birth);

        if (!base) {
            Event death = createDeathEvent(newPerson.getPersonID(), birthYear + 80);
            insertEvent(death);
        }
        base = false;
    }

    public Person createFather(String personID, String spouseID, String lastName) throws IOException {
        Person father = new Person(personID, userName, getMaleName(), lastName, "m",
                obtainNewPersonID(), obtainNewPersonID(), spouseID);
        return father;
    }

    public Person createMother(String personID, String spouseID) throws IOException {
        Person mother = new Person(personID, userName, getFemaleName(), getLastName(), "f",
                obtainNewPersonID(), obtainNewPersonID(), spouseID);
        return mother;
    }

    public String getMaleName() throws IOException {
        maleNames = collection.getMaleNames();
        Random random = new Random();
        int index = random.nextInt(maleNames.length);

        return maleNames[index];
    }

    public String getFemaleName() throws IOException {
        femaleNames = collection.getFemaleNames();
        Random random = new Random();
        int index = random.nextInt(femaleNames.length);

        return femaleNames[index];
    }

    public String getLastName() throws IOException {
        surnames = collection.getSurnames();
        Random random = new Random();
        int index = random.nextInt(surnames.length);

        return surnames[index];
    }

    public Location getRandomLocation() throws IOException {

        locations = collection.getLocations();
        Random random = new Random();
        int index = random.nextInt(locations.size());
        Location randomLocation = locations.get(index);
        return randomLocation;
    }

    public Event createBirthEvent(String personID, int birthYear) throws IOException {
        Location birthLocation = getRandomLocation();
        Event birth = new Event(generateUniqueID(), userName, personID, birthLocation.getLatitude(),
                                birthLocation.getLongitude(), birthLocation.getCountry(),
                                birthLocation.getCity(), "birth", birthYear);
        return birth;
    }

    public Event createMarriageEvent(String personID, Location marriageLocation, int marriageYear) throws IOException {
        Event marriage = new Event(generateUniqueID(), userName, personID, marriageLocation.getLatitude(),
                                   marriageLocation.getLongitude(), marriageLocation.getCountry(),
                                   marriageLocation.getCity(), "marriage", marriageYear);
        return marriage;
    }

    public Event createDeathEvent(String personID, int deathYear) throws IOException {
        Location deathLocation = getRandomLocation();
        Event death = new Event(generateUniqueID(), userName, personID, deathLocation.getLatitude(),
                                   deathLocation.getLongitude(), deathLocation.getCountry(),
                                   deathLocation.getCity(), "death", deathYear);
        return death;
    }

    public FillResponse generateFailureResponse(String message) throws DataAccessException {
        confirmFill.setSuccess(false);
        confirmFill.setMessage(message);
        db.closeConnection(false);
        return confirmFill;
    }

    public void insertEvent(Event newEvent) throws DataAccessException {
        try {
            eDao.insert(newEvent);
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmFill = generateFailureResponse("Fill unsuccessful: Data Access error");
        }
        eventCount++;
    }

    public String obtainNewPersonID() {
        if (gensAdded >= generationsToGenerate) {
            return null;
        }
        return generateUniqueID();
    }

}
