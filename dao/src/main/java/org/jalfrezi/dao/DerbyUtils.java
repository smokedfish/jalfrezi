package org.jalfrezi.dao;

import java.sql.SQLException;

public final class DerbyUtils {

    private DerbyUtils() {
    }

    public static boolean tableAlreadyExists(SQLException e) {
        return e.getSQLState().equals("X0Y32");
    }
}
