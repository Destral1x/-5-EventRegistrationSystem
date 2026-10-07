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
 * Savienojums ar iegulto Apache Derby datu bāzi.
 * Datu bāze glabājas mapē "database/eventdb" (projekta mapē), tāpēc dati
 * saglabājas starp programmas palaišanas reizēm.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class DatabaseUtil {

    private static final String DB_URL = "jdbc:derby:database/eventdb;create=true";
    private static final String SHUTDOWN_URL = "jdbc:derby:;shutdown=true";

    private static final String SCHEMA_SQL = "/eventregistrationsystem/sql/schema.sql";
    private static final String DATA_SQL = "/eventregistrationsystem/sql/data.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Jāizsauc vienreiz programmas sākumā.
     * Ja tabulu vēl nav (pirmā palaišana) — izveido tās un ievieto sākotnējos datus.
     * Ja tabulas jau ir — neko nemaina, tiek izmantoti saglabātie dati.
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

        // Aizverot programmu, datu bāze tiek korekti aizvērta
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseUtil::shutdown));
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection(SHUTDOWN_URL);
        } catch (SQLException e) {
            // Derby veiksmīgas aizvēršanas gadījumā vienmēr met XJ015
            if (!"XJ015".equals(e.getSQLState())) {
                System.err.println("Kļūda aizverot datu bāzi: " + e.getMessage());
            }
        }
    }

    private static boolean tableExists(Connection conn, String table) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, conn.getSchema(), table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    /** Nolasa .sql failu no resursiem un izpilda katru komandu (atdalītas ar ";"). */
    private static void runScript(Connection conn, String resource) throws SQLException, IOException {
        try (Statement st = conn.createStatement()) {
            for (String sql : readStatements(resource)) {
                st.executeUpdate(sql);
            }
        }
    }

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
