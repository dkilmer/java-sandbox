package org.ssanames.project;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
        ParseBabyNames pb = new ParseBabyNames("SsaNames.db");
        NamesAnalysis na = new NamesAnalysis("SsaNames.db");
        pb.createTable(2014);
        pb.loadTable(2016, 20);
        pb.loadTable(2014,10);
        na.getRankChange("Madison", 'F', 2017, 2018);
        } catch (IOException ie){
            System.err.println("I/O error: " + ie.getMessage());
        } catch (SQLException se){
            System.err.println("SQL error "+ se.getMessage());
        }
    }
}
