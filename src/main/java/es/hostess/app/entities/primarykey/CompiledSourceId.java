package es.hostess.app.entities.primarykey;

import java.util.Objects;

public class CompiledSourceId {

    private long sourceFK;

    private long hostsFK;

    public CompiledSourceId(long sourceFK, long hostsFK) {
        this.sourceFK = sourceFK;
        this.hostsFK = hostsFK;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompiledSourceId that = (CompiledSourceId) o;
        return sourceFK == that.sourceFK && hostsFK == that.hostsFK;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFK, hostsFK);
    }

    @Override
    public String toString() {
        return "CompiledSourceId{" +
                "sourceFK=" + sourceFK +
                ", hostsFK=" + hostsFK +
                '}';
    }
}
