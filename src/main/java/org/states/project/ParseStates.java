package org.states.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ParseStates {

    private static String WIKI_URL = "https://en.m.wikipedia.org/wiki/List_of_U.S._state_abbreviations";
    private static String CSV_FILE = System.getProperty("user.dir") + "\\states.csv";

    public static void ParseWiki() throws IOException {
        //gets the html for wikipedia page
        Document document = Jsoup.connect(WIKI_URL).get();

        //removes all "th" elements from page. This prevents printout of codes and abbreviation tables.
        document.select("th").remove();

        //grabs the state names
        Elements wikiTable = document.select("table.wikitable.sortable td:eq(0)").select("a[href^=/wiki/]");

        //initializes writer with location wherec csv file will be generated and creates a CSVPrinter with header
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("State/Territory"));

        //writes to csv file
        for(Element element: wikiTable){
            if(element.text().equals("United States of America") || element.text().equals("")){
                continue;
            }
            System.out.println(element.text());
            csvPrinter.printRecord(element.text());
        }
        csvPrinter.flush();
    }

    public static void main(String[] args) {
        try {
            ParseWiki();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
