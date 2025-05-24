package es.hostess.app.model;

import es.hostess.app.config.HibernateUtil;
import es.hostess.app.entities.HostsLocal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class to perform operations in the database for entity {@link HostsLocal}.
 */
public class HostsLocalModel extends DatabaseAccessBase<HostsLocal, Long> {
    private static final Logger LOGGER = Logger.getLogger(HostsLocalModel.class.getName());

    public void saveLocal(HostsLocal localHost){
        localHost.setExclusion(false);
        LocalDate localDate = LocalDate.now();
        localHost.setModificationDate(localDate);

        this.saveEntry(localHost, localHost.getId());
    }

    public void saveExclusion(HostsLocal hostsLocal){
        hostsLocal.setExclusion(true);
        LocalDate localDate = LocalDate.now();
        hostsLocal.setModificationDate(localDate);

        this.saveEntry(hostsLocal, hostsLocal.getId());
    }

    public HostsLocal getLocal(){
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            HostsLocal localHost = session.createQuery("FROM HostsLocal WHERE isExclusion = false",
                            HostsLocal.class)
                    .setMaxResults(1)
                    .getSingleResult();
            return localHost;
        } catch (Exception e) {
            LOGGER.severe(e.getLocalizedMessage());
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    public List<HostsLocal> getExclusion(){
        Transaction transaction = null;

        try(SessionFactory sessionFactory = HibernateUtil.connection();
            Session session = sessionFactory.openSession()) {
            List<HostsLocal> results = session.createQuery("FROM HostsLocal WHERE isExclusion = true")
                    .getResultList();
            return results;
        }catch (Exception e){
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
            return new ArrayList<>();
        }
    }
}
