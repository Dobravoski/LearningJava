import db.DB;

import java.sql.Connection;

public class test {
    static void main() {
        Connection conn = DB.getConnection();

        DB.closeConnection();
    }
}
