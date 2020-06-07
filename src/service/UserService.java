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
  private static User user;
  private Scanner scanner;
  private File usersDataBase;
  private String pathToFile = "UsersDataBase_serializationlist.txt";


  {
    scanner = new Scanner(System.in);
    usersDataBase = new File("UsersDataBase.txt");
  }

  public void userCommonRegistration() {
    checkLogin();
    checkPassword();
    user.setUserType(UserType.COMMON_USER);
    usersHashMap.put(user.getLogin(), user);
    System.out.println("User created successfully.");
  }

  private void checkLogin() {
    System.out.println("Input login:");
    String login = scanner.nextLine();
    if (login.isEmpty()) {
      System.out.println("Wrong entry repeat again");
      checkLogin();
    } else if (getUsersHashMap().containsKey(login)) {
      System.out.println("User " + login + " already exists, use a different login.");
      checkLogin();
    } else {

      user.setLogin(login);
    }
  }

  private void checkPassword() {
    System.out.println("Input password:");
    String password = scanner.nextLine();
    if (password.isEmpty()) {
      System.out.println("Wrong entry repeat again");
      checkPassword();
    } else {
      user.setPassword(password);
    }
  }

  public void userLogin() {
    System.out.println("Input login:");
    String login = scanner.nextLine();
    if (getUsersHashMap().containsKey(login)) {
      user = usersHashMap.get(login);
      System.out.println("Input password:");
      String password = scanner.nextLine();
      if (password.equals(user.getPassword())) {
        System.out.println("Login successfully.");
      } else {
        System.out.println("Wrong password, try again.");
        userLogin();
      }
    } else {
      System.out.println("Try again! Wrong data!");
      userLogin();
    }
    System.out.println();
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
          user = new User(getFieldValue(array[1]), getFieldValue(array[2]),
              UserType.valueOf(getFieldValue(array[3])));
          usersHashMap.put(user.getLogin(), user);
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
    try (FileOutputStream fos = new FileOutputStream(pathToFile,
        true)) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(usersHashMap);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<String, User> getDeserializationUsersMap() {
    try (FileInputStream fis = new FileInputStream(pathToFile)) {
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

  public static User getUser() {
    return user;
  }


}




