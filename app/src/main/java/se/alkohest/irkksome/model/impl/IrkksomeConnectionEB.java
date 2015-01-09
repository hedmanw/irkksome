package se.alkohest.irkksome.model.impl;

import java.security.KeyPair;
import java.util.Date;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Nullable;
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
    @Column("useSSH")
    private boolean useSSH;

    @Column("sshHost")
    @Nullable
    private String sshHost = "";
    @Column("sshUser")
    @Nullable
    private String sshUser = "";
//    @Transient
    private String sshPass = "";
    @Column("sshPort")
    private int sshPort = 22;
    @Column("sshKeySaved")
    private boolean SSHKeySaved;
    @Column("lastUsed")
    private Date lastUsed;

    private KeyPair keyPair;

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair kp) {
        keyPair = kp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseSSH() {
        return useSSH;
    }

    public void setUseSSH(boolean useSSH) {
        this.useSSH = useSSH;
    }

    @Override
    public boolean isSSHKeySaved() {
        return SSHKeySaved;
    }

    @Override
    public void setSSHKeySaved(boolean saved) {
        SSHKeySaved = saved;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public String getSshPass() {
        return sshPass;
    }

    public void setSshPass(String sshPass) {
        this.sshPass = sshPass;
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
    public boolean isIrssiProxyConnection() {
        return host.equals("localhost");
    }

    @Override
    public String toString() {
        if (isIrssiProxyConnection()) {
            return getSshUser() + "@" + getSshHost();
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
        if (sshPort != that.sshPort) return false;
        if (useSSH != that.useSSH) return false;
        if (useSSL != that.useSSL) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (!realname.equals(that.realname)) return false;
        if (sshHost != null ? !sshHost.equals(that.sshHost) : that.sshHost != null) return false;
//        if (sshPass != null ? !sshPass.equals(that.sshPass) : that.sshPass != null) return false;
        if (sshUser != null ? !sshUser.equals(that.sshUser) : that.sshUser != null) return false;
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
