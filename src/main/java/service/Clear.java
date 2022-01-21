package service;


import RequestAndResponse.ClearResponse;
import dao.DataAccessException;
import dao.Database;

import java.sql.Connection;

public class Clear {
    private ClearResponse confirmClear = new ClearResponse();

    /**
     * Deletes ALL data from the database, including user accounts,
     * auth tokens, and generated person and event data.
     * @return response message of success or failure
     */
    public ClearResponse clear() throws DataAccessException {

        System.out.println("Clearing");
        Database db = new Database();
        Connection conn = db.getConnection();

        try {
            db.clearTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            confirmClear.setMessage("Clear database error");
            confirmClear.setSuccess(false);
            db.closeConnection(false);
            System.out.println("Error");
            return confirmClear;
        }

        db.closeConnection(true);
        System.out.println("Success");
        confirmClear.setMessage("Clear succeeded.");
        confirmClear.setSuccess(true);

        return confirmClear;
    }
}
