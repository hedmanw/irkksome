package se.alkohest.irkksome.model.impl;

import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Nullable;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("sshConnection")
public class SSHConnectionEB extends AbstractBean implements SSHConnection {

    @Column("sshHost")
    @Nullable
    private String sshHost = "";

    @Column("sshUser")
    @Nullable
    private String sshUser = "";

    private String sshPassword = "";

    @Column("sshPort")
    private int sshPort = 22;

    @Column("useKeyPair")
    private boolean useKeyPair;

    public boolean isUseKeyPair() {
        return useKeyPair;
    }

    public void setUseKeyPair(boolean saved) {
        useKeyPair = saved;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getSshUser() {
        return sshUser;
    }

    public void setSshUser(String sshUser) {
        this.sshUser = sshUser;
    }

    public String getSshHost() {
        return sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }

    @Override
    public String toString() {
        return getSshUser() + "@" + getSshHost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SSHConnectionEB that = (SSHConnectionEB) o;

        if (useKeyPair != that.useKeyPair) return false;
        if (sshPort != that.sshPort) return false;
        if (!sshHost.equals(that.sshHost)) return false;
        if (!sshUser.equals(that.sshUser)) return false;

        return true;
    }
}
