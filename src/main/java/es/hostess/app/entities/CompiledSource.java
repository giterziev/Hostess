package es.hostess.app.entities;

import es.hostess.app.entities.primarykey.CompiledSourceId;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "COMPILED_SOURCES")
@IdClass(CompiledSourceId.class)
public class CompiledSource implements Serializable {

    @Id
    @Column(name = "SOURCE_ID")
    private long sourceFK;

    @Id
    @Column(name = "HOSTS_ID")
    private long hostsFK;

    @ManyToOne
    @JoinColumn(name = "SOURCE_ID", updatable = false, insertable = false)
    private HostsSource hostsSource;

    public long getSourceFK() {
        return sourceFK;
    }

    public void setSourceFK(long sourceFK) {
        this.sourceFK = sourceFK;
    }

    public long getHostsFK() {
        return hostsFK;
    }

    public void setHostsFK(long hostsFK) {
        this.hostsFK = hostsFK;
    }

    public HostsSource getHostsSource() {
        return hostsSource;
    }

    public void setHostsSource(HostsSource hostsSource) {
        this.hostsSource = hostsSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompiledSource that = (CompiledSource) o;
        return sourceFK == that.sourceFK && hostsFK == that.hostsFK;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFK, hostsFK);
    }

    @Override
    public String toString() {
        return "CompiledSource{" +
                "sourceFK=" + sourceFK +
                ", hostsFK=" + hostsFK +
                ", hostsSource=" + hostsSource +
                '}';
    }
}
