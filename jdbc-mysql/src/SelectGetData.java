import db.DB;
import db.DbException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectGetData {
    static void main() {
        try (Connection conn = DB.getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from seller")) {

            while(resultSet.next()) {
                System.out.printf("%d - %s - %s\n", resultSet.getInt("Id"), resultSet.getString("Name"), resultSet.getString("Email"));
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
