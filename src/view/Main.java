package view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import model.Movie;
import model.Ticket;
import model.User;
import model.UserType;
import service.CinemaService;
import service.MovieService;
import service.UserService;

public class Main {

  public static void main(String[] args) {
    UserService userService = new UserService();

//    userService.rewriteUsers();
//    userService.printHashMap();

//    MovieService movieService = new MovieService();
//    User user = new User(1, "no", "123", UserType.COMMON_USER);
//
//    List<Ticket> tickets = new LinkedList<>();
//    for (int i = 0; i < 5; i++) {
//      tickets.add(new Ticket());
//    }
//
//    Movie movie = new Movie("Po", new Calendar.Builder().setInstant(new Date()).build(), tickets);
//    System.out.println(movie.getId() + " " + movie.getName());

//    userService.getUsersHashMap();
//    userService.printHashMap();
//    userService.printHashMap();
//    userService.createSerializationUsers();
//    System.out.println(userService.getDeserializationUsersMap().get("Romanenko"));

    CinemaService cinemaService = new CinemaService();
    cinemaService.mainMenu();







  }

}
