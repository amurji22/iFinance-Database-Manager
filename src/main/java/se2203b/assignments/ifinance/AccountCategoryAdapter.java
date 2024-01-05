package se2203b.assignments.ifinance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountCategoryAdapter {

    Connection connection;

    public AccountCategoryAdapter(Connection conn, Boolean reset) throws SQLException{
        connection = conn;
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE AccountCategory");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            // Create the table
            stmt.execute("CREATE TABLE AccountCategory ("
                    + "NAME VARCHAR(20) NOT NULL PRIMARY KEY,"
                    + "TYPE VARCHAR(20) NOT NULL"
                    + ")");


        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
        }
        try {
            addData();
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
        }
    }
    public void addData() throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO AccountCategory (NAME, TYPE) VALUES ('Assets', 'Debit')");
        stmt.executeUpdate("INSERT INTO AccountCategory (NAME, TYPE) VALUES ('Liabilities', 'Credit')");
        stmt.executeUpdate("INSERT INTO AccountCategory (NAME, TYPE) VALUES ('Income', 'Credit')");
        stmt.executeUpdate("INSERT INTO AccountCategory (NAME, TYPE) VALUES ('Expenses', 'Debit')");
    }
    public int next_row_ID() throws SQLException {
        int rows = 1;
        Statement stmt = this.connection.createStatement();
        String sqlStatment = "SELECT NAME FROM AccountCategory";
        ResultSet num_rows = stmt.executeQuery(sqlStatment);
        while (num_rows.next()) {
            rows += 1;
        }
        return rows;
    }
    public String[] all_names() throws SQLException {
        int x = 0;
        String[] result = new String[next_row_ID()];
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM AccountCategory";
        ResultSet results = stmt.executeQuery(sqlStatement);
        while (results.next()) {
            result[x] = results.getString("NAME");
            x += 1;
        }
        return result;
    }
}
