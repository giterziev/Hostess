package es.hostess.app.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "HOSTS_SOURCES")
public class HostsSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOURCE_ID")
    private long id;
    @Column(name = "SOURCE_URL")
    private String sourceURL;
    @Column(name = "SOURCE_DATE")
    private LocalDate sourceDate;
    @Column(name = "HOSTS_FILE")
    private String hostsFile;

    public HostsSource() {
    }

    public HostsSource(String sourceURL, LocalDate sourceDate, String hostsFile) {
        this.sourceURL = sourceURL;
        this.sourceDate = sourceDate;
        this.hostsFile = hostsFile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public LocalDate getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(LocalDate sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getHostsFile() {
        return hostsFile;
    }

    public void setHostsFile(String hostsFile) {
        this.hostsFile = hostsFile;
    }

    @Override
    public String toString() {
        return "HostsSource{" +
                "sourceId=" + id +
                ", sourceURL='" + sourceURL + '\'' +
                ", sourceDate=" + sourceDate +
                ", hostsFile='" + hostsFile + '\'' +
                '}';
    }
}
