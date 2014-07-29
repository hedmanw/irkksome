package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.orm.AbstractBean;

public class IrkksomeConnectionEB extends AbstractBean implements IrkksomeConnection {
    private String host;
    private int port;
    private String nickname;
    private String username;
    private String realname;
    private String password;

    private boolean useSSL;

    private boolean useSSH;
    private String sshHost;
    private String sshUser;
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
        this.port = port;
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
    public ContentValues createRow(long dependentPK) {
        return null;
    }
}
