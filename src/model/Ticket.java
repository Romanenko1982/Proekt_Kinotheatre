package model;

import java.io.Serializable;

public class Ticket implements Serializable {

  private static int counter = 0;
  private int id;
  private User user;
  private String movie;
  private int numberOfPlace;
  private int cost;
  private boolean isAvailable = true;

  public Ticket(User user, String movie, int numberOfPlace, int cost, boolean isAvailable) {
    id = ++counter;
    this.user = user;
    this.movie = movie;
    this.numberOfPlace = numberOfPlace;
    this.cost = cost;
    this.isAvailable = isAvailable;
  }

  public Ticket(int numberOfPlace) {
    id = ++counter;
    this.numberOfPlace = numberOfPlace;
  }

  public Ticket() {
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("id=").append(id);
    sb.append(", user=").append(user);
    sb.append(", movie=").append(movie);
    sb.append(", numberOfPlace=").append(numberOfPlace);
    sb.append(", cost=").append(cost);
    sb.append(", isAvailable=").append(isAvailable);
    return sb.toString();
  }

  public String getFreeTickets() {
    final StringBuilder sb = new StringBuilder();
    sb.append("movie=").append(movie);
    sb.append(", numberOfPlace=").append(numberOfPlace);
    sb.append(", cost=").append(cost);
    return sb.toString();
  }

  public void setMovie(String movie) {
    this.movie = movie;
  }

  public Ticket setAvailable(boolean available) {
    isAvailable = available;
    return this;
  }

  public Ticket setUser(User user) {
    this.user = user;
    return this;
  }

  public int getId() {
    return id;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public int getNumberOfPlace() {
    return numberOfPlace;
  }
}
