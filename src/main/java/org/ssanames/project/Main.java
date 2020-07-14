package org.ssanames.project;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
//        try {
            ParseBabyNames pb = new ParseBabyNames("SsaNames.db");
            //pb.createTable("Ssa2017");
            //pb.loadTable(2017);
            NamesAnalysis.getYearsAvail();
//        } catch (IOException ie){
//            System.err.println("I/O error: " + ie.getMessage());
//        } catch (SQLException se){
//            System.err.println("SQL error "+ se.getMessage());
//        }
    }
}
