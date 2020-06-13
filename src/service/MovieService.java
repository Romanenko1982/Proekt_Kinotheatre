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
import java.util.List;
import java.util.Map;
import model.Movie;
import model.Ticket;

public class MovieService {

  private static HashMap<Integer, Movie> moviesHashMap;
  private File moviesDataBase;
  private String pathToFile = "MoviesDataBase_serializationlist.txt";
  private TicketService ticketService;

  {
    moviesDataBase = new File("MoviesDataBase.txt");
    ticketService = new TicketService();
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
          Movie movie = new Movie(getFieldValue(array[1]), convertToDate(getFieldValue(array[2])),
              ticketService.ticketList(this));
          moviesHashMap.put(movie.getId(), movie);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return moviesHashMap;
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

  public void printMovieHashMap() {
    for (Map.Entry<Integer, Movie> var : moviesHashMap.entrySet()) {
      System.out.println(var.getKey() + ", " + var.getValue());
    }
  }

  public void showMovieList() {
    getMoviesHashMap();
    for (Movie movie : moviesHashMap.values()) {
      System.out
          .printf("id - %s, фильм - %s, дата - %s", movie.getId(), movie.getName(), movie.date());
      ticketService.showFreeTicketByIdMovie(movie.getId(), this);
      System.out.println();
    }
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
}
