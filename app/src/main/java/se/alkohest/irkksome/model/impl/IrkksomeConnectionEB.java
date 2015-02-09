package se.alkohest.irkksome.model.impl;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Nullable;
import se.alkohest.irkksome.orm.OneToOne;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Connection")
public class IrkksomeConnectionEB extends AbstractBean implements IrkksomeConnection {
    @Column("host")
    private String host = "";
    @Column("port")
    private int port;
    @Column("nickname")
    private String nickname = "";

    @Column("username")
    @Nullable
    private String username = "";

    @Column("realname")
    @Nullable
    private String realname = "";

    @Column("irssiPassword")
    @Nullable
    private String password = "";

    @Column("useSSL")
    private boolean useSSL;

    @Column("lastUsed")
    private Date lastUsed;

    @Column("sshConnectionData")
    @OneToOne(SSHConnectionEB.class)
    private SSHConnection sshConnection;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseSSH() {
        return sshConnection != null;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port <= 0) {
            this.port = 6667;
        }
        else {
            this.port = port;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public void setSSHConnection(SSHConnection sshConnection) {
        this.sshConnection = sshConnection;
    }

    @Override
    public SSHConnection getSSHConnection() {
        return sshConnection;
    }

    @Override
    public boolean isIrssiProxyConnection() {
        return host.equals("localhost") && isUseSSH();
    }

    @Override
    public String toString() {
        if (isIrssiProxyConnection()) {
            return sshConnection.toString();
        } else {
            return getHost();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IrkksomeConnectionEB that = (IrkksomeConnectionEB) o;

        if (port != that.port) return false;
        if (useSSL != that.useSSL) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (!realname.equals(that.realname)) return false;
        if (sshConnection != null ? !sshConnection.equals(that.sshConnection) : that.sshConnection != null) return false;
        if (!username.equals(that.username)) return false;

        return true;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}
