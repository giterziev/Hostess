package es.hostess.app.config;

import es.hostess.app.exceptions.HostessGeneticException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * Util class to create the connection.
 */
public final class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());

    /**
     * Constructor that should not be used.
     */
    @SuppressWarnings("unused")
    private HibernateUtil() {
        throw new HostessGeneticException("Can't instance the class.");
    }

    public static String dbCheck() throws IOException {

        InputStream is = HibernateUtil.class.getClassLoader().getResourceAsStream("database.db");
        String appDataPath = System.getenv("APPDATA");
        String destinationPath = appDataPath + "\\hostess";
        String dbPath = destinationPath + "\\database.db";

        if (Files.notExists(Paths.get(destinationPath))){
            Files.createDirectory(Paths.get(destinationPath));
        }

        if (Files.notExists(Paths.get(dbPath))){
                Files.copy(is, Paths.get(dbPath), StandardCopyOption.REPLACE_EXISTING);
                is.close();
        }
    return dbPath;
    }
    /**
     * Returns the JDBC connection.
     * @return The JDBC connection as a {@link SessionFactory} object.
     */
    public static SessionFactory connection() {
        if(sessionFactory != null && !sessionFactory.isClosed()) return sessionFactory;
        try {
            // Get the path of the url in case we are running from a jar
            String databasePath = dbCheck();


            // Crea la SessionFactory desde el fichero hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", String.format("jdbc:sqlite:%s", databasePath));
            configuration.configure();
            LOGGER.info("Configuration complete");
            sessionFactory = configuration.buildSessionFactory();

            return sessionFactory;
        } catch (Exception ex) {
            LOGGER.severe("Initial SessionFactory creation failed.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }
}
