package view;

import service.MenuService;


public class Application {

  private MenuService menuService;

  {
    menuService = new MenuService();
      }

  public void startApplication() {
    menuService.startWelcomeMenu();
  }
}
