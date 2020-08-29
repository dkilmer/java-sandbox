package org.ssanames.project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import  java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamesAnalysis {

    private String dbName;

    public NamesAnalysis(String dbName) {
        this.dbName = dbName;
    }

    private Connection connect() {
        String dataFile = "C://sqlite/db/" + this.dbName;
        new File(dataFile).getParentFile().mkdirs();
        String url = "jdbc:sqlite:C://sqlite/db/" + this.dbName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public int getMaxYear() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("SSAData");
        String path = url.getPath();
        String filesList[] = new File(path).list();
        Set<Integer> years = new HashSet<>();
        //System.out.println(files.length);
        for (String file : filesList) {
            if (file.charAt(0) == 'N') {
                continue;
            }
            years.add(Integer.parseInt(file.substring(3, 7)));
        }
        return Collections.max(years);
    }

    /*
    This is a function that calculates the change in rank of a name between two years.
    String prevYearTable corresponds to the table holding data for the previous or smallest year. String currYearTable corresponds to the
    table holding data for the current or highest year.
    */
    public void getRankChange(String name, char sex, int prevYear, int currYear) {
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
             Statement stmt1 = conn.createStatement();
             ResultSet rs1 = stmt1.executeQuery(prevYearSql)) {
            // loop through the result set
            while (rs1.next()) {
                prevRank = rs1.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //get rank for curr year from table
        try (Connection conn = this.connect();
             Statement stmt2 = conn.createStatement();
             ResultSet rs2 = stmt2.executeQuery(currYearSql)) {
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
        if (rankChg == 0) {
            System.out.println("Name " + name.toUpperCase() + " has not changed in rank");
        } else if (rankChg > 0) {
            System.out.println("Name " + name.toUpperCase() + " has decreased in rank by " + rankChg);
        } else {
            System.out.println("Name " + name.toUpperCase() + " has increased in rank by " + Math.abs(rankChg));
        }
    }

    //This method returns the first occurrence of a name in the SSA Database
    public int nameFirstListed(String name, char sex) throws IOException {
        //gets list of database files available in project
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("SSAData");
        // the files we want to look at have the following name pattern
        Pattern fileNamePattern = Pattern.compile("yob\\d{4}\\.txt");
        // get the files, keeping only those that match the pattern (and are files)
        String filesArray[] = new File(url.getPath())
          .list((file, s) -> (new File(file, s).isFile() && fileNamePattern.matcher(s).matches()));
/*
        // the code above is equivalent to the following...
        String filesArray[] = new File(url.getPath())
          .list(new FilenameFilter() {
              @Override
              public boolean accept(File file, String s) {
                  return (new File(file, s).isFile() && fileNamePattern.matcher(s).matches());
              }
          });
*/
        // Turn the array of files into a list so we can sort it by file name
        List<String> filesList = Arrays.asList(Objects.requireNonNull(filesArray));
        // Make sure our files are sorted by year (Sorts in normal string order).
        // File.list() does not guarantee alphabetical order (see https://docs.oracle.com/javase/8/docs/api/java/io/File.html#list--)
        filesList.sort(Comparator.naturalOrder());

        String firstYear = null;

        searchLoop:
        for (String fileName : filesList) {
            BufferedReader br = new BufferedReader(new InputStreamReader(ParseBabyNames.class.getResourceAsStream("/SSAData/" + fileName)));
            String line;
            //System.out.println(fileName);
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(name) && values[1].charAt(0) == sex) {
                    firstYear = fileName.substring(3, 7);
                    br.close();
                    break searchLoop;
                }
            }
        }
        //System.out.println(firstYear);
        if (firstYear == null) throw new IOException("Name "+name+" ("+sex+") was not found in the database");
        return Integer.parseInt(firstYear);
    }

    public int getOccurenceVal (String name, char sex, int year) throws IOException {
        String fileName = "yob" + year + ".txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(ParseBabyNames.class.getResourceAsStream("/SSAData/" + fileName)));
        String line;
        int occurVal = 0;
        searchLoop:
        while ((line = br.readLine()) != null){
            String[] values = line.split(",");
            if (values[0].equals(name) && values[1].charAt(0) == sex){
                occurVal = Integer.parseInt(values[2]);
                br.close();
                break searchLoop;
            }
        }
        //System.out.println(occurVal);
        return occurVal;
    }

    public HashMap<Integer,Integer> getPopularity(String name, char sex) throws IOException {
        HashMap<Integer, Integer> occurMap = new HashMap<>();
        int firstYearOccur = nameFirstListed(name,sex);
        int stopYear = getMaxYear();
        for(int i = firstYearOccur; i <= stopYear; i++){
            occurMap.put(i,getOccurenceVal(name,sex,i));
        }
        return occurMap;
    }

    public void LineChart(String name, char sex) throws IOException {
        String title = "Popularity of Name: " + name.toUpperCase() + " ("+sex+") Over Time";
        String userDir = System.getProperty("user.dir");
        DefaultCategoryDataset lineChartDate = new DefaultCategoryDataset();
        HashMap<Integer, Integer> nameData = getPopularity(name, sex);

        for(Integer keyVal: nameData.keySet()){
            lineChartDate.addValue(nameData.get(keyVal),"names",keyVal);
        }

        JFreeChart lineChartObj  = ChartFactory.createLineChart(title, "Year",
                "Occurrence", lineChartDate,PlotOrientation.VERTICAL, true, true, false
        );

        // remove the legend since we're only plotting one name
        lineChartObj.removeLegend();
        // get the x axis of the chart (years)
        CategoryAxis xaxis = lineChartObj.getCategoryPlot().getDomainAxis();
        // make the year label text vertical to make more space
        xaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
        // transparent white
        Color c = new Color(255, 255, 255, 0);
        for(Integer keyVal: nameData.keySet()) {
            // Hack - set the label to be transparent if the year isn't an even decade.
            // This makes the chart more readable, since otherwise year text would overlap.
            if ((keyVal % 10) != 0) {
                xaxis.setTickLabelPaint(keyVal, c);
            }
        }

        int width = 800;
        int height = 800;

        File newDir = new File(userDir + "/images/");

        if(!newDir.exists()) {
            System.out.println("Creating images directory...");
            newDir.mkdir();
        }
            File lineChart = new File(userDir + "/images/" + name + "_Occur_Over_Time.jpeg");
            ChartUtils.saveChartAsJPEG(lineChart, lineChartObj, width, height);
    }

}

