package se.alkohest.irkksome.model.entity;

import java.util.Date;

import se.alkohest.irkksome.orm.BeanEntity;

/**
 * Created by wilhelm 2014-07-29.
 */
public interface IrkksomeConnection extends BeanEntity {
    public boolean isIrssiProxyConnection();

    public String getPassword();

    public void setPassword(String password);

    public boolean isUseSSH();

    public String getRealname();

    public void setRealname(String realname);

    public String getUsername();

    public void setUsername(String username);

    public String getNickname();

    public void setNickname(String nickname);

    public int getPort();

    public void setPort(int port);

    public String getHost();

    public void setHost(String host);

    public boolean isUseSSL();

    public void setUseSSL(boolean useSSL);

    public Date getLastUsed();

    public void setLastUsed(Date date);

    public void setSSHConnection(SSHConnection sshConnection);

    public SSHConnection getSSHConnection();
}
