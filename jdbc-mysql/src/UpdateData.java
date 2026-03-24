import db.DB;

import java.sql.*;

public class UpdateData {
    static void main() {
        try(Connection conn = DB.getConnection();
            PreparedStatement psUPDATE = conn.prepareStatement(
                    "UPDATE seller SET BaseSalary = BaseSalary + ? WHERE (DepartmentId = ?)");
            PreparedStatement psSELECT = conn.prepareStatement("SELECT Id FROM seller WHERE (DepartmentId = ?)")) {

            psSELECT.setInt(1, 2);

            psUPDATE.setDouble(1, 200.50);
            psUPDATE.setInt(2, 2);

            int rowsAffected = psUPDATE.executeUpdate();

            System.out.printf("Rows affected: %d\n", rowsAffected);

            if (rowsAffected > 0) {
                try(ResultSet rs = psSELECT.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("ID: %d affected\n", rs.getInt("Id"));
                    }
                }
            } else  {
                System.out.println("No rows affected!");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error! " + e.getMessage());
        }
    }
}
