package org.subtitle.assistor.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLConnection {
    ResultSet getQueryResult(String query) throws SQLException;
}
