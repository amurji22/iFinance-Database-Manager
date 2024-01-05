package se2203b.assignments.ifinance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class GroupsAdapter {
    int delete = 0;
    Connection connection;

    public GroupsAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE Groups");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }


            try {
                // Create the table of Login Info
                stmt.execute("CREATE TABLE Groups ("
                        + "ID INT NOT NULL PRIMARY KEY,"
                        + "Names VARCHAR(20) NOT NULL,"
                        + "Parent INT NOT NULL,"
                        + "Element VARCHAR(20) NOT NULL REFERENCES AccountCategory(NAME)"
                        + ")");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table exists and may have some data.
            }
            add_Data();
        }
    }


    public void add_Data() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Fixed assets' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Investments' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Branch/divisions' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Cash in hand' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Bank accounts' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Deposits (assets)' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Advances (assets)' , 0, 'Assets')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Capital account' , 0, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Long term loans' , 0, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Current liabilities' , 0, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Reserves and surplus' , 0, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Sales account' , 0, 'Income')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Purchase account' , 0, 'Expenses')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Expenses (direct)' , 0, 'Expenses')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Expenses (indirect)' , 0, 'Expenses')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Secured loans' , 9, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Unsecured loans' , 9, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Duties taxes payable' , 10, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Provisions' , 10, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Sundry creditors' , 10, 'Liabilities')");
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) VALUES (" + next_row_ID() + " , 'Bank od & limits' , 10, 'Liabilities')");
        } catch (SQLException exception) {
            System.out.println("From the add Data method");
            System.out.println(exception.getMessage());
        }
    }
    public int next_row_ID() throws SQLException {
        int rows = 1;
            Statement stmt = this.connection.createStatement();
            String sqlStatment = "SELECT ID FROM Groups";
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
        String sqlStatement = "SELECT * FROM Groups";
        ResultSet results = stmt.executeQuery(sqlStatement);
        while (results.next()) {
            result[x] = results.getString("Names");
            x += 1;
        }
        return result;
    }

    public int get_ID_name(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM Groups WHERE Names = '"+name+"'";
        ResultSet results = stmt.executeQuery(sqlStatement);
        if (results.next()) {
            return results.getInt("ID");
        }else {
            return 0;
        }
    }
    public Boolean is_parent(String name) throws SQLException{
        Statement stmt = connection.createStatement();
        int id = get_ID_name(name);
        String sqlStatement = "SELECT * FROM Groups WHERE Parent ="+id;
        ResultSet results = stmt.executeQuery(sqlStatement);
        return results.next();
    }
    public Boolean is_parent_ID(int ID) throws SQLException{
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM Groups WHERE Parent ="+ID;
        ResultSet results = stmt.executeQuery(sqlStatement);
        return results.next();
    }
    public boolean in_DB(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM Groups WHERE Names = '"+name+"'";
        ResultSet results = stmt.executeQuery(sqlStatement);
        return results.next();
    }

    public int get_Parent(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM Groups WHERE Names = '"+name+"'";
        ResultSet results = stmt.executeQuery(sqlStatement);
        if (results.next()) {
           return results.getInt("Parent");
        }
        return 0;
    }

    public void edit_name(String og_name, String new_name) throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate("UPDATE Groups SET Names = '" + new_name + "'WHERE Names = '" + og_name + "'");
    }
    public void delete_row(String name) throws Exception {
        Statement stmt = this.connection.createStatement();
        int ID = get_ID_name(name);
        stmt.executeUpdate("DELETE FROM Groups WHERE ID = "+ID);
    }


    public void add_row(String clicked, String new_name) throws Exception {
        if (new_name == null || clicked == null){
            return;
        }
        if (get_ID_name(clicked) == 0) {
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) "
                    + "VALUES (" + next_row_ID() + " , '" + new_name + "' , " + get_ID_name(clicked) + " , '" + clicked + "')");
        }else{
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate("INSERT INTO Groups (ID, Names, Parent, Element) "
                    + "VALUES (" + next_row_ID() + " , '" + new_name + "' , " + get_ID_name(clicked) + " , '" + get_element(clicked) + "')");
        }
    }
    public String get_element(String clicked) throws SQLException {
        Statement stmt = connection.createStatement();
        String sqlStatement = "SELECT * FROM Groups WHERE Names = '"+clicked+"'";
        ResultSet results = stmt.executeQuery(sqlStatement);
        while (results.next()) {
           return results.getString("Element");
        }
        return null;
    }
    public void refactor_table() throws SQLException {
        int rows = 1;
        try (Statement stmt = this.connection.createStatement()) {
            String sqlStatement = "SELECT * FROM Groups";
            ResultSet num_rows = stmt.executeQuery(sqlStatement);
            while (num_rows.next()) {
                if (rows != num_rows.getInt("ID")){
                    if (is_parent_ID(num_rows.getInt("ID"))){
                        stmt.executeUpdate("UPDATE Groups SET Parent = " + rows + " WHERE Parent = " + num_rows.getInt("ID") );
                    }
                    else{
                        stmt.executeUpdate("UPDATE Groups SET ID = " + rows + " WHERE ID = " + num_rows.getInt("ID") );
                    }
                }
                rows += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        }
}
