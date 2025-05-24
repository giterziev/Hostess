package es.hostess.app.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "HOSTS_COMPILED")
public class HostsCompiled implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOSTS_ID")
    private long id;
    @Column(name = "LAST_UPDATE")
    private LocalDate lastUpdate;
    @Column(name = "HOSTS_COMPILED")
    private String compiledHosts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCompiledHosts() {
        return compiledHosts;
    }

    public void setCompiledHosts(String compiledHosts) {
        this.compiledHosts = compiledHosts;
    }

    @Override
    public String toString() {
        return "HostsCompiled{" +
                "hostsId=" + id +
                ", lastUpdate=" + lastUpdate +
                ", hostsCompiled='" + compiledHosts + '\'' +
                '}';
    }
}
