package controller;

import java.sql.SQLException;
import Model.database;

public class readuser {

    public readuser(String email, String password, database db) {
        String query = "SELECT * FROM `Users` WHERE `Email` = '" + email + "' AND `Password` = '" + password + "';";
        
        try {
            db.getStatement().executeQuery(query);

            // TODO: Process the result set if needed

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
