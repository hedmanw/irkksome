package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Nullable;
import se.alkohest.irkksome.orm.Table;
import se.alkohest.irkksome.orm.Transient;

@Table("t_connection")
public class IrkksomeConnectionEB extends AbstractBean implements IrkksomeConnection {
    private String host;
    private int port;
    private String nickname;
    @Nullable
    private String username;
    @Nullable
    private String realname;
    @Transient
    private String password;

    private boolean useSSL;
    private boolean useSSH;
    @Nullable
    private String sshHost;
    @Nullable
    private String sshUser;
    @Transient
    private String sshPass;
    private int sshPort = 22;

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
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("host", host);
        contentValues.put("port", port);
        contentValues.put("nickname", nickname);
        contentValues.put("username", username);
        contentValues.put("realname", realname);
        contentValues.put("useSSL", useSSL);
        contentValues.put("useSSH", useSSH);
        contentValues.put("sshHost", sshHost);
        contentValues.put("sshUser", sshUser);
        contentValues.put("sshPort", sshPort);
        return contentValues;
    }

    @Override
    public String toString() {
        return "IrkksomeConnectionEB[ id = " + getId() + "\n" +
                nickname + '@' + host + ':' + port +
                ", username='" + username + '\'' + ", realname='" + realname + '\'' + ", useSSL=" + useSSL +
                ", SSH: [ " + sshUser + '@' + sshHost + ':' + sshPort + ']' +
                "\n]";
    }
}
