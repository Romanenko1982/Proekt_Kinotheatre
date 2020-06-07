package model;

import java.io.Serializable;

public class User implements Serializable {

  private static int counter;
  private int id;
  private String login;
  private String password;
  private UserType userType;

  public User(String login, String password, UserType userType) {
    id = ++counter;
    this.login = login;
    this.password = password;
    this.userType = userType;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("id=").append(id);
    sb.append(", Login=").append(login);
    sb.append(", Password=").append(password);
    sb.append(", UserType=").append(userType);
    return sb.toString();
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
  }


}
