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
    menu.printTheWelcomeMenuView(this);
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
        menu.printTheCommonMenuView(this);
        break;
      case MANAGER:
        menu.printTheManagerMenuView(this);
        break;
      case ADMINISTRATOR:
        menu.printTheAdministratorMenuView(this);
        break;
    }
  }

  public void startMenuCommon() {
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          movieService.showMovieList(userService);
          break;
        case 2:
          movieService.purchaseTicket(userService);
          break;
        case 3:
          movieService.showPurchaseTickets(userService);
          break;
        case 4:
          movieService.returnTicket(userService);
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
  }

  public void startMenuAdministrator() {
    if(scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          userService.printHashMap();
          break;
      }
    }
  }

  private void exit() {
    userService.rewriteUsers();
    scanner.close();
    System.exit(0);
  }
}
