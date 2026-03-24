import db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteData {
    static void main() {
        try(Connection conn = DB.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM department WHERE (Id = ?)")) {

            ps.setInt(1, 2);

            System.out.printf("Rows affected: %d\n", ps.executeUpdate());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
