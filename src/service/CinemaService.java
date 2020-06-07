package service;

import java.util.Scanner;

public class CinemaService {

  Scanner scanner;
  UserService us;
  MovieService ms;

  {
    us = new UserService();
    ms = new MovieService();
    scanner = new Scanner(System.in);
  }

  private void printMainConsole() {
    System.out.println("---------------Main MENU----------------");
    System.out.println("Hello user, select the procedure you need:\n");
    System.out.println("-----------------------------------------");
    System.out.println("1. Registration.");
    System.out.println("2. Login/Password.");
    System.out.println("3. Exit.\n");
  }

  public void mainMenu() {
    printMainConsole();
    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          us.userCommonRegistration();
          break;
        case 2:
          us.userLogin();
          break;
        case 3:
          exit();
        default:
          System.out.println("Invalid menu value selected. Try again.\n");
          mainMenu();
      }
      subMenu();
    } else {
      System.out.println("Invalid input. Try again.\n");
      scanner.next();
      mainMenu();
    }
  }

  private void printSubMenu() {
    System.out.println("---------------------------Sub MENU-----------------------");
    System.out.println("1. Show movie list.");
    System.out.println("2. Purchase ticket.");
    System.out.println("3. Show my purchases ticket.");
    System.out.println("4. Return ticket.");
    System.out.println("5. Transfer money to a card.");
    System.out.println("6. Exit to Main Menu\n");
  }

  public void subMenu() {
    printSubMenu();

    if (scanner.hasNextInt()) {
      int var = scanner.nextInt();
      switch (var) {
        case 1:
          ms.showMovieList();
          break;
        case 2:
          ms.purchaseTicket();
          break;
        case 3:
          ms.showPurchaseTickets();
          break;
        case 4:
          ms.returnTicket();
          break;
        case 5:
//          transferMoneyToCard();
          break;
        case 6:
          mainMenu();
          break;
        default:
          System.out.println("Invalid menu value selected.\n");
      }
      subMenu();
    }
  }

  private void exit() {
    us.rewriteUsers();
    scanner.close();
    System.exit(0);
  }


}
