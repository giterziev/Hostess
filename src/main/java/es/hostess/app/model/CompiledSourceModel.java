package es.hostess.app.model;

import es.hostess.app.config.HibernateUtil;
import es.hostess.app.entities.CompiledSource;
import es.hostess.app.entities.primarykey.CompiledSourceId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.logging.Logger;

/**
 * Class to perform operations in the database for entity {@link CompiledSource}.
 */
public class CompiledSourceModel extends DatabaseAccessBase<CompiledSource, CompiledSourceId> {
    private static final Logger LOGGER = Logger.getLogger(CompiledSourceModel.class.getName());

    private static final String EXISTS_QUERY = "SELECT COUNT(*) FROM %s WHERE sourceFK = %s and hostsFK = %s";

    public CompiledSource saveSource(CompiledSource entry) {
        return saveEntry(entry, new CompiledSourceId(entry.getSourceFK(), entry.getHostsFK()));
    }

    /**
     * Inserts or updates an object in the database.
     * @param entry The entry to insert.
     */
    @Override
    public CompiledSource saveEntry(CompiledSource entry, CompiledSourceId entryId) {
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long count = session.createQuery(String.format(EXISTS_QUERY,
                    entry.getClass().getName(), entryId.getSourceFK(), entryId.getHostsFK()), Long.class).getSingleResult();
            if (count > 0) {
                session.merge(entry);
            } else {
                session.persist(entry);
            }
            transaction.commit();
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }

        return entry;
    }
}
