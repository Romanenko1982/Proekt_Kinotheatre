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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Movie;
import model.Ticket;
import model.User;

public class MovieService {

  private HashMap<Integer, Movie> moviesHashMap;
  private Movie movie;
  private File moviesDataBase;
  private String pathToFile = "MoviesDataBase_serializationlist.txt";
  private List<Ticket> purchasedTicketsList;
  private Scanner scanner;

  {
    moviesDataBase = new File("MoviesDataBase.txt");
    purchasedTicketsList = new LinkedList<>();
    scanner = new Scanner(System.in);
  }

  public void createFileMovies() {
    if (!moviesDataBase.exists()) {
      try {
        moviesDataBase.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public HashMap<Integer, Movie> getMoviesHashMap() {
    if (moviesHashMap == null) {
      moviesHashMap = new HashMap<>();
      String var;
      try (BufferedReader br = new BufferedReader(new FileReader(moviesDataBase))) {
        while ((var = br.readLine()) != null) {
          String[] array = var.trim().split("; ");
          movie = new Movie(getFieldValue(array[1]), convertToDate(getFieldValue(array[2])),
              list());
          moviesHashMap.put(movie.getId(), movie);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return moviesHashMap;
  }

  public List<Ticket> list() {
    List<Ticket> list = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      list.add(new Ticket(i + 1));
    }
    return list;
  }


  public String getFieldValue(String str) {
    int index = str.indexOf("=");
    return str.substring(index + 1);
  }

  public void rewriteMovie() {
    HashMap<Integer, Movie> var = new MovieService().getMoviesHashMap();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(moviesDataBase))) {
      for (Movie movie : var.values()) {
        bw.write(movie.toString() + "\n");
      }
      bw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printHashMap() {
    for (Map.Entry<Integer, Movie> var : moviesHashMap.entrySet()) {
      System.out.println(var.getKey() + ", " + var.getValue());
    }
  }

//  public void showFreeTickets(int id) {
//    for (Ticket ticket : moviesHashMap.get(id).getTicketList()) {
//      System.out.println(ticket.getFreeTickets());
//    }
//  }

  public void showMovieList() {
    getMoviesHashMap();
    for (Movie movie : moviesHashMap.values()) {
      System.out.printf("фильм - %s, %s, дата - %s", movie.getId(), movie.getName(), movie.date());
      showFreeTicketByIdMovie(movie.getId());
      System.out.println();
    }
  }

  private void showFreeTicketByIdMovie(int id) {
    List<Ticket> list = getMoviesHashMap().get(id).getTicketList();
    System.out.print(" Свободные места:");
    for (Ticket ticket : list) {
      if (ticket.isAvailable()) {
        System.out.print(" " + ticket.getNumberOfPlace());
      }
    }
  }

  private void putTicketOfUserToPurchaseFile(User user, Ticket ticket) {
    try (FileOutputStream fos = new FileOutputStream(user.getLogin() + "purchase_ticket.txt")) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      purchasedTicketsList.add(ticket);
      oos.writeObject(purchasedTicketsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void putTicketOfUserToPurchaseFile(User user, List<Ticket> list) {
    try (FileOutputStream fos = new FileOutputStream(user.getLogin() + "purchase_ticket.txt")) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      purchasedTicketsList.addAll(list);
      oos.writeObject(purchasedTicketsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void purchaseTicket(int idMovie, int numberOfPlace) {
    Ticket ticket = getMoviesHashMap()
        .get(idMovie)
        .getTicketList()
        .get(numberOfPlace - 1);
    if (ticket.isAvailable()) {
      ticket.setAvailable(false)
          .setUser(UserService.getUser());
      putTicketOfUserToPurchaseFile(UserService.getUser(), ticket);
    } else {
      System.out.println("Данный билет уже куплен.");
    }
  }

  private List<Ticket> returnTicketsList() {
    List<Ticket> list = null;
    try (FileInputStream fis = new FileInputStream(
        UserService.getUser().getLogin() + "purchase_ticket.txt")) {
      ObjectInputStream ois = new ObjectInputStream(fis);
      try {
        list = (List<Ticket>) ois.readObject();
      } catch (ClassNotFoundException e) {
      }
    } catch (IOException e) {
      System.out.println("у Вас нет купленных билетов.");
    }
    return list;
  }

  public void showPurchaseTickets() {
    if (purchasedTicketsList.isEmpty()) {
      System.out.println("У вас нет купленных билетов");
    } else {
      for (Ticket ticket : returnTicketsList()) {
        System.out.println(ticket);
      }
    }
  }

  public void returnPurchasedTicket(int idTicket) {
    List<Ticket> ticket1 = returnTicketsList();
    List<Ticket> ticket2 = new LinkedList<>();
    for (Ticket ticket : ticket1) {
      if (ticket.getId() != idTicket) {
        ticket2.add(ticket);
      } else {
        ticket.setAvailable(true);
      }
    }
    purchasedTicketsList = new LinkedList<>();
    putTicketOfUserToPurchaseFile(UserService.getUser(), ticket2);
    rewriteMovie();
  }

  private Calendar convertToDate(String str) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.y");
    try {
      return new Calendar.Builder().setInstant(simpleDateFormat.parse(str)).build();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void createSerializationMovie() {
    try (FileOutputStream fos = new FileOutputStream(pathToFile,
        true)) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(moviesHashMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<String, Movie> getDeserializationMoviesMap() {
    try (FileInputStream fis = new FileInputStream(pathToFile)) {
      ObjectInputStream ois = new ObjectInputStream(fis);
      try {
        return (HashMap<String, Movie>) ois.readObject();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void returnTicket() {
    System.out.println("Введите id билета, который хотите вернуть.");
    returnPurchasedTicket(scanner.nextInt());
  }

  public void purchaseTicket() {
    System.out.println("Введите id фильма и номер места.");
    purchaseTicket(scanner.nextInt(), scanner.nextInt());
  }
}
