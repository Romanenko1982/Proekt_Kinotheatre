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
import java.util.Map;
import java.util.Scanner;
import model.User;
import model.UserType;

public class UserService {

  private static HashMap<String, User> usersHashMap;
  private User currentUser;
  private Scanner scanner;
  private File usersDataBase;
  private String serializedUserDatabaseFile = "UsersDataBase_serializationlist.txt";
  private  static final long serialVersionUID = 0;

  {
    scanner = new Scanner(System.in);
    usersDataBase = new File("UsersDataBase.txt");
  }

  public void userRegistration() {
    checkLogin();
    checkPassword();
    currentUser.setUserType(UserType.COMMON_USER);
    usersHashMap.put(currentUser.getLogin(), currentUser);
    System.out.println("Пользователь создан успешно.");
  }

  private void checkLogin() {
    System.out.println("Введите login:");
    String login = scanner.nextLine();
    if (login.isEmpty()) {
      System.out.println("Неправильно введены данные, повторите снова");
      checkLogin();
    } else if (getUsersHashMap().containsKey(login)) {
      System.out.println("Пользователь " + login + " уже существует, выберите другой login.");
      checkLogin();
    } else {
      currentUser.setLogin(login);
    }
  }

  private void checkPassword() {
    System.out.println("Введите пароль:");
    String password = scanner.nextLine();
    if (password.isEmpty()) {
      System.out.println("Неправильно введены данные, повторите снова");
      checkPassword();
    } else {
      currentUser.setPassword(password);
    }
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
      String var;
      try (BufferedReader br = new BufferedReader(new FileReader(usersDataBase))) {
        while ((var = br.readLine()) != null) {
          String[] array = var.trim().split("; ");
          currentUser = new User(getFieldValue(array[1]), getFieldValue(array[2]),
              UserType.valueOf(getFieldValue(array[3])));
          usersHashMap.put(currentUser.getLogin(), currentUser);
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
    HashMap<String, User> var = new UserService().getUsersHashMap();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersDataBase))) {
      for (User user : var.values()) {
        bw.write(user.toString() + "\n");
      }
      bw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printHashMap() {
    for (Map.Entry<String, User> var : usersHashMap.entrySet()) {
      System.out.println(var.getKey() + ", " + var.getValue());
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




