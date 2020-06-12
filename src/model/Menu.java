package model;

import service.MenuService;

public class Menu {

  public void printTheWelcomeMenuView(MenuService menuService) {
    System.out.println("---------------МЕНЮ ПРИВЕТСТВИЯ ПОЛЬЗОВАТЕЛЯ----------------");
    System.out.println("ВАС приветствует кинотеатр, выберите необходимые действия:");
    System.out.println("------------------------------------------------------------\n");
    System.out.println("1. Регистрация нового пользователя");
    System.out.println("2. Вход пользователя");
    System.out.println("3. Выход");
    menuService.startMenu();
  }

  public void printTheCommonMenuView(MenuService menuService) {
    System.out.println("------------------------ГЛАВНОЕ МЕНЮ-------------------------");
    System.out.println("1. Показать список фильмов");
    System.out.println("2. Купить билет");
    System.out.println("3. Просмотреть купленные билеты");
    System.out.println("4. Вернуть билет");
    System.out.println("5. Выход");
    menuService.startMenuCommon();
  }

  public void printTheManagerMenuView(MenuService menuService) {
    System.out.println("----------------------МЕНЮ МЕНЕДЖЕРА------------------------");
    System.out.println("1. Редактировать список фильмов");
    System.out.println("2. Редактировать сеансы просмотра фильмов");
    System.out.println("3. Возвращать купленные пользователем билеты");
    System.out.println("4. Выход");
    menuService.startMenuManager();
  }

  public void printTheAdministratorMenuView(MenuService menuService) {
    System.out.println("--------------------МЕНЮ АДМИНИСТРАТОРА---------------------");
    System.out.println("1. Просмотреть список пользователей");
    System.out.println("2. Редактировать пользователя");
    System.out.println("3. Удалить пользователя");
    System.out.println("4. Добавить фильм");
    System.out.println("5. Удалить фильм");
    System.out.println("6. Выход");
    menuService.startMenuAdministrator();
  }
}
