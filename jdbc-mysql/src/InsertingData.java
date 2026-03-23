import db.DB;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InsertingData {
    static void main() {
        try(Connection conn = DB.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO seller" +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
                    "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, "Bob Brown");
            statement.setString(2, "bobrown@gmail.com");
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.parse("22/03/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            statement.setDouble(4, 5000.00);
            statement.setInt(5, 1);

            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                while (resultSet.next()) {
                    System.out.println("Done! Id = " + resultSet.getInt(1));
                }
            } else {
                System.out.println("No rows affected!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}