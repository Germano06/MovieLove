package org.germano;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class MovieSearch {
    static String movName, actor, actress, director, year, q, query, sqlServer, getFilePath, filePath, jdbcUrl;
    static Scanner sc = new Scanner(System.in);
    static int ch;
    static ResultSet resultSet;
    static Statement statement;

    public static void main(String[] args) {
        sqlServer = "jdbc:sqlite:";
        getFilePath = new File("").getAbsolutePath();
        filePath = getFilePath.concat("\\database\\moviesDB.db");
        jdbcUrl = sqlServer + filePath;

        if (!dbExist(filePath)) {
            createDbfile("moviesDB");
        }

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            statement = connection.createStatement();

            do {
                ch = menu();
                System.out.println();
                switch (ch) {
                    case 0:
                        System.out.println("Thank You");
                        break;

                    case 1:
                        enterData();
                        statement.execute(query);
                        break;

                    case 2:
                        search(1);
                        break;

                    case 3:
                        search(2);
                        break;

                    case 4:
                        search(3);
                        break;

                    case 5:
                        search(4);
                        break;

                    case 6:
                        searchDuo();
                        break;

                    case 7:
                        query = "SELECT * FROM moviesinfo;";
                        break;

                    default:
                        System.out.printf("Enter a valid input");
                        ch = 1;
                }
                if (ch > 1) {
                    resultSet = statement.executeQuery(query);
                    try {
                        resultSet.getString("moviename");
                        display();

                        while (resultSet.next()) {
                            movName = resultSet.getString("moviename");
                            actor = resultSet.getString("leadactor");
                            actress = resultSet.getString("leadactress");
                            year = resultSet.getString("yearofrelease");
                            director = resultSet.getString("director");
                            display(movName, actor, actress, director, year);
                        }
                    } catch (Exception e) {
                        System.out.println("No result found!!");
                    }
                }
                System.out.println();
            } while (ch != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDbfile(String moviesDB) {
        getFilePath = new File("").getAbsolutePath();
        filePath = getFilePath.concat("\\database\\" + moviesDB + ".db");
        query = "CREATE TABLE moviesinfo(" +
                "id INTEGER NOT NULL," +
                "moviename TEXT NOT NULL," +
                "leadactor TEXT NOT NULL," +
                "leadactress TEXT NOT NULL," +
                "director TEXT," +
                "yearofrelease TEXT NOT NULL," +
                "PRIMARY KEY(id)" +
                ");";
        jdbcUrl = sqlServer + filePath;
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean dbExist(String filePath) {
        File dbFile = new File(filePath);
        return dbFile.exists();
    }

    private static void searchDuo() {
        String x, y;
        System.out.print("Enter the Actor's Name: ");
        sc.nextLine();
        x = sc.nextLine();
        System.out.print("Entert he Actress' Name: ");
        y = sc.nextLine();
        query = "SELECT * FROM moviesinfo WHERE leadactor LIKE '%" + x + "%' AND leadactress LIKE '%" + y + "%';";
    }

    private static void display(String movName, String actor, String actress, String director, String year) {
        System.out.format("%-30s %-25s %-25s %-44s %-12s", movName, actor, actress, director, year);
        System.out.println();
    }

    private static void display() {
        System.out.printf("%-30s %-25s %-25s %-44s %-16s", "Movie Name", "Actor", "Actress", "Director(s)",
                "Release Year");
        System.out.println();
        System.out.println("============================================================================" +
                "=================================================================");
    }

    private static void search(int x) {
        String y;
        if (x == 1) {
            System.out.print("Enter the Actor's name: ");
            q = "leadactor";
        } else if (x == 2) {
            System.out.print("Enter the Actress' name: ");
            q = "leadactress";
        } else if (x == 3) {
            System.out.print("Enter the Movie name: ");
            q = "moviename";
        } else {
            System.out.print("Enter the Movie Release year: ");
            q = "yearofrelease";
        }
        sc.nextLine();
        y = sc.nextLine();
        query = "SELECT * FROM moviesinfo WHERE " + q + " LIKE '%" + y + "%';";
    }

    private static int menu() {
        System.out.println("1-> Enter data");
        System.out.println("2-> Search by Actor");
        System.out.println("3-> Search by Actress");
        System.out.println("4-> Search by Movie Name");
        System.out.println("5-> Search by Movie Release Year");
        System.out.println("6-> Search by Actor and Actress pair");
        System.out.println("7-> Show all movies");
        System.out.println("0-> Exit");
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }

    private static void enterData() {
        System.out.print("Enter the Movie Name: ");
        sc.nextLine();
        movName = sc.nextLine();
        System.out.print("Enter the Lead Actor's Name: ");
        actor = sc.nextLine();
        System.out.print("Enter the Lead Actress' Name: ");
        actress = sc.nextLine();
        System.out.print("Enter the Director's Name: ");
        director = sc.nextLine();
        System.out.print("Enter the Year of Release: ");
        year = sc.next();

        query = "INSERT INTO moviesinfo(moviename,leadactor,leadactress,director,yearofrelease) VALUES('" + movName
                + "','" + actor + "','" + actress + "','" + director + "','" + year + "');";
    }
}
