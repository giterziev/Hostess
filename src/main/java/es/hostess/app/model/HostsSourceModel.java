package es.hostess.app.model;

import es.hostess.app.entities.HostsSource;

import java.io.Serializable;
import java.util.List;

/**
 * Class to perform operations in the database for entity {@link HostsSource}.
 */
public class HostsSourceModel extends DatabaseAccessBase<HostsSource, Long> {
    public List<HostsSource> findAll(){
        return this.getAllEntries(HostsSource.class);
    }
}
