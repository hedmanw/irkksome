package se.alkohest.irkksome.model.entity;

import se.alkohest.irkksome.orm.BeanEntity;

public interface SSHConnection extends BeanEntity {
    public boolean isUseKeyPair();

    public void setUseKeyPair(boolean saved);

    public int getSshPort();

    public void setSshPort(int sshPort);

    public String getSshPassword();

    public void setSshPassword(String sshPass);

    public String getSshUser();

    public void setSshUser(String sshUser);

    public String getSshHost();

    public void setSshHost(String sshHost);
}
