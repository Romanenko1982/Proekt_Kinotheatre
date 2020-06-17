package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import model.Ticket;
import model.User;

public class TicketService {

  private File purchasedTicketUserFile;
  private List<Ticket> purchasedTicketsList;
  private Scanner scanner;

  {
    purchasedTicketsList = new LinkedList<>();
    scanner = new Scanner(System.in);
    purchasedTicketUserFile = new File("purchase_ticket.txt");
  }

  public List<Ticket> createListOfPlaces() {
    List<Ticket> ticketList = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      ticketList.add(new Ticket(i + 1));
    }
    return ticketList;
  }

  public void showFreeTicketByIdMovie(int id, MovieService movieService) {
    List<Ticket> list = movieService.getMoviesHashMap().get(id).getTicketList();
    System.out.print(" Свободные места:");
    for (Ticket ticket : list) {
      if (ticket.isAvailable()) {
        System.out.print(" " + ticket.getNumberOfPlace());
      }
    }
  }

  private void putTicketOfUserToPurchaseFile(User user, Ticket ticket) {
    try (FileOutputStream fos = new FileOutputStream(user.getLogin() + purchasedTicketUserFile)) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      purchasedTicketsList.add(ticket);
      oos.writeObject(purchasedTicketsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void putTicketOfUserToPurchaseFile(User user, List<Ticket> list) {
    try (FileOutputStream fos = new FileOutputStream(user.getLogin() + purchasedTicketUserFile)) {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      purchasedTicketsList.addAll(list);
      oos.writeObject(purchasedTicketsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void purchaseTicket(UserService userService, MovieService movieService) {
    System.out.println("Введите id фильма и номер места через пробел.");
    purchaseTicket(scanner.nextLine(), userService, movieService);
  }

  public void purchaseTicket(String request, UserService userService, MovieService movieService) {
    String[] array = request.trim().split(" +");
    Ticket ticket = movieService.getMoviesHashMap()
        .get(Integer.parseInt(array[0]))
        .getTicketList()
        .get(Integer.parseInt(array[1]) - 1);
    if (ticket.isAvailable()) {
      ticket.setUser(userService.getUser());
      movieService.getMoviesHashMap()
          .get(Integer.parseInt(array[0])).getTicketList()
          .remove(Integer.parseInt(array[1]) - 1);
      putTicketOfUserToPurchaseFile(userService.getUser(), ticket);
    } else {
      System.out.println("Данный билет уже куплен.");
    }
  }

  private List<Ticket> getTicketsListUser(UserService userService) {
    List<Ticket> list = null;
    try (FileInputStream fis = new FileInputStream(
        userService.getUser().getLogin() + purchasedTicketUserFile)) {
      ObjectInputStream ois = new ObjectInputStream(fis);
      try {
        list = (List<Ticket>) ois.readObject();
      } catch (ClassNotFoundException ignored) {
      }
    } catch (IOException e) {
      System.out.println("у Вас нет купленных билетов.");
    }
    return list;
  }

  public void showPurchaseTickets(UserService userService, MovieService movieService) {
    if (purchasedTicketsList.isEmpty()) {
      System.out.println("У вас нет купленных билетов");
    } else {
      String date;
      for (Ticket ticket : getTicketsListUser(userService)) {
        date = movieService.getMoviesHashMap()
            .get(Integer.parseInt(ticket.getMovie().split(" +")[0])).date();
//        date = movieService.getMoviesHashMap().get(ticket.getId()).date();
        System.out.printf("id=%s, фильм=%s, дата просмотра=%s, номер места=%s\n", ticket.getId(),
            ticket.getMovie(), date, ticket.getNumberOfPlace());
      }
    }
  }

  public void returnPurchasedTicket(int idTicket, UserService userService,
      MovieService movieService) {
    List<Ticket> ticket1 = getTicketsListUser(userService);
    List<Ticket> ticket2 = new LinkedList<>();
    for (Ticket ticket : ticket1) {
      if (ticket.getId() != idTicket) {
        ticket2.add(ticket);
      } else {
        String[] array = ticket.getMovie().split(" ");
        movieService.getMoviesHashMap()
            .get(Integer.parseInt(array[0]))
            .getTicketList().add(ticket.getId() - 1, ticket);
      }
    }
    purchasedTicketsList = new LinkedList<>();
    putTicketOfUserToPurchaseFile(userService.getUser(), ticket2);
    movieService.rewriteMovie();
  }

  public void returnTicket(UserService userService, MovieService movieService) {
    System.out.println("Введите id билета, который хотите вернуть.");
    returnPurchasedTicket(scanner.nextInt(), userService, movieService);
  }

  public void purchaseTicketForManager(String request, UserService userService,
      MovieService movieService) {
    String[] array = request.trim().split(" +");
    Ticket ticket = movieService.getMoviesHashMap()
        .get(Integer.parseInt(array[0]))
        .getTicketList()
        .get(Integer.parseInt(array[1]) - 1);
    if (ticket.isAvailable()) {
      ticket.setUser(userService.getUser());
      movieService.getMoviesHashMap()
          .get(Integer.parseInt(array[0])).getTicketList()
          .remove(Integer.parseInt(array[1]) - 1);
      putTicketOfUserToPurchaseFile(userService.getUsersHashMap().get(array[2]), ticket);
    } else {
      System.out.println("Данный билет уже куплен.");
    }
  }

  public void purchaseTicketForManager(UserService userService, MovieService movieService) {
    System.out.println("Введите id фильма, номер места и login пользователя через пробел.");
    purchaseTicketForManager(scanner.nextLine(), userService, movieService);
  }


}
