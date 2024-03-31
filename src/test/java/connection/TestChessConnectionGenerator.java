package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestChessConnectionGenerator {

    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String TEST_DATABASE = "chess_test"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    private TestChessConnectionGenerator() {
    }

    public static Connection getConnection() throws SQLException {
        // 드라이버 연결
        return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + TEST_DATABASE + OPTION, USERNAME, PASSWORD);
    }
}
