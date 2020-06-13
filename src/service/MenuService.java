package service;

import java.util.Scanner;
import model.Menu;
import model.UserType;

public class MenuService {

  private MovieService movieService;
  private UserService userService;
  private TicketService ticketService;
  private Scanner scanner;
  private Menu menu;

  {
    movieService = new MovieService();
    userService = new UserService();
    ticketService = new TicketService();
    scanner = new Scanner(System.in);
    menu = new Menu();
  }

  public void startWelcomeMenu() {
    userService.createFileUsers();
    menu.printTheWelcomeMenuView();
    startMenu();
  }

  public void startMenu() {
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          userService.userRegistration();
          break;
        case 2:
          userService.userLogin();
          break;
        case 3:
          exit();
          break;
        default:
          System.out.println("Неверно введенные данные, попробуйте снова\n");
          startWelcomeMenu();
      }
      subMenu();
    } else {
      System.out.println("Неверно введенные данные, попробуйте снова\n");
      scanner.next();
      startWelcomeMenu();
    }
  }

  public void subMenu() {
    UserType type = userService.getUser().getUserType();
    switch (type) {
      case COMMON_USER:
        startMenuCommon();
        break;
      case MANAGER:
        startMenuManager();
        break;
      case ADMINISTRATOR:
        startMenuAdministrator();
        break;
    }
  }

  public void startMenuCommon() {
    menu.printTheCommonMenuView();
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          movieService.showMovieList();
          break;
        case 2:
          ticketService.purchaseTicket(userService, movieService);
          break;
        case 3:
          ticketService.showPurchaseTickets(userService, movieService);
          break;
        case 4:
          ticketService.returnTicket(userService, movieService);
          break;
        case 5:
          exit();
          break;
        default:
          System.out.println("Неверно введенные данные, попробуйте снова\n");
      }
      startMenuCommon();
    }
  }

  public void startMenuManager() {
    menu.printTheManagerMenuView();
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
//        case 1:
//          movieService.editMovieList();
//          break;
//        case 2:
//          movieService.editMovieDate();
//          break;
//        case 3:
//          ticketService.returnPurchaseTicket();
//          break;
//        case 4:
//          exit();
//          break;
        default:
          System.out.println("Неверно введенные данные, попробуйте снова\n");
      }
      startMenuManager();
    }
  }

  public void startMenuAdministrator() {
    menu.printTheAdministratorMenuView();
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          userService.printHashMapUsers();
          break;
//        case 2:
//          userService.editUser();
//          break;
//        case 3:
//          userService.removeUser();
//          break;
//        case 4:
//          movieService.addMovie();
//          break;
//        case 5:
//          movieService.removeMovie();
//          break;
        case 6:
          exit();
          break;
        default:
          System.out.println("Неверно введенные данные, попробуйте снова\n");
      }
      startMenuAdministrator();
    }
  }

  private void exit() {
    userService.rewriteUsers();
    scanner.close();
    System.exit(0);
  }
}
