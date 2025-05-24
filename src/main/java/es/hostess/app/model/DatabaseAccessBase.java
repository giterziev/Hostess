package es.hostess.app.model;

import es.hostess.app.config.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract class to perform all database operations.
 * @param <T> The entry object class.
 * @param <I> The entry object id.
 */
public abstract class DatabaseAccessBase<T, I> {
    private static final Logger LOGGER = Logger.getLogger(DatabaseAccessBase.class.getName());
    private static final String EXISTS_QUERY = "SELECT COUNT(*) FROM %s WHERE id = %s";

    /**
     * Inserts or updates an object in the database.
     * @param entry The entry to insert.
     */
    public T saveEntry(T entry, I entryId) {
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long count = session.createQuery(String.format(EXISTS_QUERY,
                    entry.getClass().getName(), entryId), Long.class).getSingleResult();
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

    /**
     * Inserts a new object in the database.
     * @param entryList The entry to insert.
     */
    public List<T> insertEntries(List<T> entryList){
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            entryList.forEach(session::persist);
            transaction.commit();
        }catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }

        return entryList;
    }

    /**
     * Updates an entry in the database.
     * @param entryList The entry to update.
     */
    public List<T> updateEntries(List<T> entryList) {
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            entryList.forEach(session::merge);
            transaction.commit();
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }

        return entryList;
    }

    /**
     * Retrieves an entry from the database.
     * @param entry The entry to fill.
     * @param entryId The id object to search for.
     * @return The filled entry from the database.
     */
    public T getEntryById(T entry, I entryId){
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(true);
            session.get(entry.getClass(), entryId);
            Hibernate.initialize(entry);
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }
        return entry;
    }

    /**
     * Retrieves an entry from the database.
     * @return The filled entry from the database.
     */
    public List<T> getAllEntries(Class<T> entryClass){
        Transaction transaction = null;
        List<T> results = new ArrayList<>();

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.setDefaultReadOnly(true);
            results = session.createQuery(String.format("FROM %s", entryClass.getName()), entryClass).list();
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }
        return results;
    }

    /**
     * Deletes an entry from the database.
     * @param entry The entry to fill.
     * @param entryId The id object to search for.
     */
    @SuppressWarnings("unchecked")
    public void deleteById(T entry, I entryId) {
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T entryFound = (T) session.get(entry.getClass(), entryId);
            session.remove(entryFound);
            session.flush();
            transaction.commit();
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }
    }

    /**
     * Deletes an entry from the database.
     * @param entryList The entry to fill.
     */
    public void deleteAll(List<T> entryList) {
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            entryList.forEach(session::remove);
            transaction.commit();
        } catch (Exception e){
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
        }
    }
}
