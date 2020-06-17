package org.ssanames.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ParseBabyNames {
    private static final String COMMA_DELIM = ",";

    //method to establish connection to the database
    private Connection connect(String dbName){
        String url = "jdbc:sqlite:C://sqlite/db/" + dbName;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //method that creates database in C:/sqlite folder
    public void createDatabase(String dbName){
        try(Connection conn = this.connect(dbName)){
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database " + dbName + " has been created.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    };

    //method that creates table in specified database
    public void createTable(String tableName, String dbName){

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n"
                + " name text PRIMARY KEY NOT NULL, \n"
                + " sex CHAR(1) NOT NULL, \n"
                + " occurrence NUMERIC, \n"
                + " year      NUMERIC\n"
                + ");";

        try(Connection conn = this.connect(dbName)){
            if (conn != null){
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println("Table " + tableName + " created...");
                }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    };


    //method to parse txt file and load it into sql database
    public void loadTable(String year, String tableName, String dbName){
        
        String sql = "INSERT INTO " + tableName + "(name,sex,occurrence,year) VALUES (?,?,?,?)";
        List<List<String>> records = new ArrayList<>();
        String fileName = "src/main/resources/names/yob" + year + ".txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while(( line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIM);
                records.add(Arrays.asList(values));
            }

            //variable to limit the amount of records loaded into table
            int cnt = 0;
            //loads first 10 rows
            while (cnt < 10){
                String name = records.get(cnt).get(0);
                String sex = records.get(cnt).get(1);
                String occurrence = records.get(cnt).get(2);

                try(Connection conn = this.connect(dbName);
                    PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setString(1, name);
                    pstmt.setString(2,sex);
                    pstmt.setString(3, occurrence);
                    pstmt.setString(4, year);
                    pstmt.executeUpdate();
                    System.out.println("Values being inserted: " + name + ", " + sex + ", " + occurrence + ", " + year);
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                ++cnt;
            }
            System.out.println(cnt + " rows inserted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    //method returns all records in specified table and database
    public void selectAll(String tableName, String dbName){
        String sql = "SELECT name,sex,occurrence,year from " + tableName;

        try (Connection conn = this.connect(dbName);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(
                    rs.getString("name") + "\t" +
                    rs.getString("sex") + "\t" +
                    rs.getInt("occurrence") + "\t" +
                    rs.getInt("year")
                );
            };
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    };


    public static void main(String[] args){
//        ParseBabyNames pb = new ParseBabyNames();
//        pb.createDatabase("SsaNames3.db");
//        pb.createTable("Yob2017", "SsaNames3.db");
//        pb.loadTable("2017", "Yob2017","SsaNames3.db");
//        pb.selectAll("Yob2017", "SsaNames3.db");
    }

}
