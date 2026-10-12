package eventregistrationsystem.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class DatabaseUtil {

    // pirms palaišanas NetBeans jāieslēdz Java DB serveris
    private static final String DB_URL = "jdbc:derby://localhost:1527/EventDB;create=true";
    private static final String DB_USER = "dbuser";
    private static final String DB_PASSWORD = "dbuser";

    private static final String SCHEMA_SQL = "/eventregistrationsystem/sql/schema.sql";
    private static final String DATA_SQL = "/eventregistrationsystem/sql/data.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // pirmajā palaišanā izveido tabulas un ieliek sākuma datus
    public static void init() throws SQLException {
        try (Connection conn = getConnection()) {
            if (!tableExists(conn, "LIETOTAJI")) {
                conn.setAutoCommit(false);
                try {
                    runScript(conn, SCHEMA_SQL);
                    runScript(conn, DATA_SQL);
                    conn.commit();
                    System.out.println("Datu bāze izveidota (pirmā palaišana).");
                } catch (SQLException | IOException e) {
                    conn.rollback();
                    throw new SQLException("Neizdevās izveidot datu bāzi: " + e.getMessage(), e);
                } finally {
                    conn.setAutoCommit(true);
                }
            } else {
                System.out.println("Datu bāze atrasta, tiek izmantoti saglabātie dati.");
            }
        }
    }

    private static boolean tableExists(Connection conn, String table) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, conn.getSchema(), table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    // izpilda sql failu pa vienai komandai
    private static void runScript(Connection conn, String resource) throws SQLException, IOException {
        try (Statement st = conn.createStatement()) {
            for (String sql : readStatements(resource)) {
                st.executeUpdate(sql);
            }
        }
    }

    // sadala sql failu pa komandām (pēc ;)
    private static List<String> readStatements(String resource) throws IOException {
        InputStream in = DatabaseUtil.class.getResourceAsStream(resource);
        if (in == null) {
            throw new IOException("Nav atrasts fails " + resource);
        }

        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }
                current.append(line).append('\n');
                if (trimmed.endsWith(";")) {
                    String sql = current.toString().trim();
                    statements.add(sql.substring(0, sql.length() - 1));
                    current.setLength(0);
                }
            }
        }
        return statements;
    }
}
