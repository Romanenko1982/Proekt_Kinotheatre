package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;
import model.User;
import model.UserType;

public class UserService {

  private static final long serialVersionUID = 0;
  private HashMap<String, User> usersHashMap;
  private User currentUser;
  private Scanner scanner;
  private File usersDataBase;
  private String serializedUserDatabaseFile = "UsersDataBase_serializationlist.txt";

  {
    scanner = new Scanner(System.in);
    usersDataBase = new File("UsersDataBase.txt");
  }

  public void userRegistration() {
    String login, password;
    login = checkLogin();
    password = checkPassword();
    currentUser = new User(login, password, UserType.COMMON_USER);
    usersHashMap.put(currentUser.getLogin(), currentUser);
    System.out.println("Пользователь создан успешно.");
  }

  private String checkLogin() {
    System.out.println("Введите login:");
    String login = scanner.nextLine();
    if (login.isEmpty()) {
      System.out.println("Вы ввели пустую строку, повторите снова");
      checkLogin();
    } else if (getUsersHashMap().containsKey(login)) {
      System.out.println(
          "Пользователь " + login + " уже существует, придумайте другой login и повторите снова");
      checkLogin();
    } else {
      return login;
    }
    return null;
  }

  private String checkPassword() {
    System.out.println("Введите пароль:");
    String password = scanner.nextLine();
    if (password.isEmpty()) {
      System.out.println("Вы ввели пустую строку, повторите снова");
      checkPassword();
    } else {
      return password;
    }
    return null;
  }

  public User userLogin() {
    System.out.println("Введите login:");
    String login = scanner.nextLine();
    if (getUsersHashMap().containsKey(login)) {
      currentUser = usersHashMap.get(login);
      System.out.println("Введите пароль:");
      String password = scanner.nextLine();
      if (password.equals(currentUser.getPassword())) {
        System.out.println("Вход выполнен успешно");
      } else {
        System.out.println("Неправильно введены данные, повторите снова");
        userLogin();
      }
    } else {
      System.out.println("Неправильно введены данные, повторите снова");
      userLogin();
    }
    System.out.println();
    return currentUser;
  }

  public void createFileUsers() {
    if (!usersDataBase.exists()) {
      try {
        usersDataBase.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public HashMap<String, User> getUsersHashMap() {
    if (usersHashMap == null) {
      usersHashMap = new HashMap<>();
      User tempUser;
      String var;
      try (BufferedReader br = new BufferedReader(new FileReader(usersDataBase))) {
        while ((var = br.readLine()) != null) {
          String[] array = var.trim().split("; ");
          tempUser = new User(
              Integer.parseInt(getFieldValue(array[0])),
              getFieldValue(array[1]),
              getFieldValue(array[2]),
              UserType.valueOf(getFieldValue(array[3]))
          );
          if (tempUser.getId() > User.getCounter()) {
            User.setCounter(tempUser.getId());
          }
          usersHashMap.put(tempUser.getLogin(), tempUser);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return usersHashMap;
  }

  public String getFieldValue(String str) {
    int index = str.indexOf("=");
    return str.substring(index + 1);
  }

  public void rewriteUsers() {
    HashMap<String, User> var = getUsersHashMap();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersDataBase))) {
      for (User user : var.values()) {
        bw.write(String
            .format("id=%s; Login=%s; Password=%s; UserType=%s%n", user.getId(), user.getLogin(),
                user.getPassword(), user.getUserType()));
      }
      bw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printHashMapUsers() {
    for (User user : getUsersHashMap().values()) {
      if (user.getUserType().equals(UserType.COMMON_USER)) {
        System.out.printf("id=%s, login=%s%n", user.getId(), user.getLogin());
      }
    }
  }

  public void createSerializationUsers() {
    try (FileOutputStream fos = new FileOutputStream(serializedUserDatabaseFile,
        true)) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(usersHashMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<String, User> getDeserializationUsersMap() {
    try (FileInputStream fis = new FileInputStream(serializedUserDatabaseFile)) {
      ObjectInputStream ois = new ObjectInputStream(fis);
      try {
        return (HashMap<String, User>) ois.readObject();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public User getUser() {
    return currentUser;
  }
}




