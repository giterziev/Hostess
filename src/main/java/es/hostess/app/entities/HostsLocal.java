package es.hostess.app.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "HOSTS_LOCAL")
public class HostsLocal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCAL_ID")
    private long id;

    @Column(name = "LOCAL_HOSTS")
    private String localHosts;

    @Column(name = "IS_EXCLUSION", nullable = false)
    private boolean isExclusion;

    @Column(name = "MODIFICATION_DATE")
    private LocalDate modificationDate;

    public HostsLocal() {
    }

    public HostsLocal(String localHosts, boolean isExclusion, LocalDate modificationDate) {
        this.localHosts = localHosts;
        this.isExclusion = isExclusion;
        this.modificationDate = modificationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalHosts() {
        return localHosts;
    }

    public void setLocalHosts(String localHosts) {
        this.localHosts = localHosts;
    }

    public boolean isExclusion() {
        return isExclusion;
    }

    public void setExclusion(boolean exclusion) {
        isExclusion = exclusion;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "HostsLocal{" +
                "id='" + id + "'" +
                ", localHosts='" + localHosts + '\'' +
                ", isExclusion=" + isExclusion +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
