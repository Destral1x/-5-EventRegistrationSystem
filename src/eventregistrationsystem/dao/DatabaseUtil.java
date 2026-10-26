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
 * Savienojums ar lokālo (iegulto) Apache Derby datu bāzi.
 * Pirmajā palaišanā izveido tabulas un ieliek sākuma datus.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class DatabaseUtil {

    /** Lokālās Derby datu bāzes adrese (mape database/eventdb). */
    private static final String DB_URL = "jdbc:derby:database/eventdb;create=true";
    /** Datu bāzes lietotājs. */
    private static final String DB_USER = "dbuser";
    /** Datu bāzes parole. */
    private static final String DB_PASSWORD = "dbuser";

    /** Tabulu izveides skripts. */
    private static final String SCHEMA_SQL = "/eventregistrationsystem/sql/schema.sql";
    /** Sākuma datu skripts. */
    private static final String DATA_SQL = "/eventregistrationsystem/sql/data.sql";

    /** Klasei ir tikai statiskas metodes, objektu neveido. */
    private DatabaseUtil() {
    }

    /**
     * Atver jaunu savienojumu ar datu bāzi.
     *
     * @return savienojums ar datu bāzi
     * @throws SQLException ja datu bāzi neizdodas atvērt
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Korekti aizver lokālo Derby datu bāzi pirms programmas beigām.
     */
    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
        }
    }

    /**
     * Pirmajā palaišanā izveido tabulas (schema.sql) un ieliek sākuma datus (data.sql).
     * Ja tabulas jau ir, izmanto saglabātos datus.
     *
     * @throws SQLException ja datu bāzi neizdodas atvērt vai izveidot
     */
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

    /**
     * Pārbauda, vai tabula eksistē.
     *
     * @param conn savienojums
     * @param table tabulas nosaukums
     * @return true, ja tabula eksistē
     * @throws SQLException datu bāzes kļūdas gadījumā
     */
    private static boolean tableExists(Connection conn, String table) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, conn.getSchema(), table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    /**
     * Izpilda SQL failu pa vienai komandai.
     *
     * @param conn savienojums
     * @param resource SQL faila ceļš resursos
     * @throws SQLException ja kāda komanda neizpildās
     * @throws IOException ja failu neizdodas nolasīt
     */
    private static void runScript(Connection conn, String resource) throws SQLException, IOException {
        try (Statement st = conn.createStatement()) {
            for (String sql : readStatements(resource)) {
                st.executeUpdate(sql);
            }
        }
    }

    /**
     * Sadala SQL failu pa komandām (pēc semikola), izlaižot komentārus.
     *
     * @param resource SQL faila ceļš resursos
     * @return SQL komandu saraksts
     * @throws IOException ja failu neizdodas nolasīt
     */
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
