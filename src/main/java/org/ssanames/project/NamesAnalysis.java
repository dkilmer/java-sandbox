package org.ssanames.project;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NamesAnalysis {

    private String dbName;

    public NamesAnalysis(String dbName){
        this.dbName = dbName;
    }

    private Connection connect(){
        String dataFile = "C://sqlite/db/" + this.dbName;
        new File(dataFile).getParentFile().mkdirs();
        String url = "jdbc:sqlite:C://sqlite/db/" + this.dbName;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static List<String> getYearsAvail() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("SSAData");
        String path = url.getPath();
        String filesList[] = new File(path).list();
        List<String> years = new ArrayList<>();
        //System.out.println(files.length);
        for(String file : filesList){
            if (file.charAt(0) == 'N') {
                continue;
            }
            years.add(file.substring(3,7));
        }
        return years;
    }

    /*
    This is a function that calculates the change in rank of a name between two years.
    String prevYearTable corresponds to the table holding data for the previous or smallest year. String currYearTable corresponds to the
    table holding data for the current or highest year.
    */
    public void getRankChange(String name, char sex, int prevYear, int currYear){
        //creates tables names
        String prevYearTable = "Ssa" + prevYear;
        String currYearTable = "Ssa" + currYear;

        //sql to select rank
        String prevYearSql = "SELECT rank from " + prevYearTable + " where name=" + "'" + name + "'" + " and sex=" + "'" + sex + "'";
        String currYearSql = "SELECT rank from " + currYearTable + " where name=" + "'" + name + "'" + " and sex=" + "'" + sex + "'";

        //initialize rank variables
        int prevRank = 0;
        int currRank = 0;
        int rankChg;

        //get rank for prev year from table
        try (Connection conn = this.connect();
             Statement stmt1  = conn.createStatement();
             ResultSet rs1    = stmt1.executeQuery(prevYearSql)){
            // loop through the result set
            while (rs1.next()) {
                prevRank = rs1.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //get rank for curr year from table
        try (Connection conn = this.connect();
             Statement stmt2  = conn.createStatement();
             ResultSet rs2   = stmt2.executeQuery(currYearSql)){
            // loop through the result set
            while (rs2.next()) {
                currRank = rs2.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //calculate change in rank
        rankChg = currRank - prevRank;

        //print rank change
        if(rankChg == 0){
            System.out.println("Name " + name.toUpperCase() + " has not changed in rank");
        }else if(rankChg > 0 ){
            System.out.println("Name " + name.toUpperCase() + " has decreased in rank by " + rankChg);
        }else{
            System.out.println("Name " + name.toUpperCase() + " has increased in rank by " + Math.abs(rankChg));
        }
    }

}