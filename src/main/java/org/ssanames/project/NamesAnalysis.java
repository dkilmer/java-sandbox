package org.ssanames.project;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NamesAnalysis {


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

    public static void getFirstYear(List<String> years, String name, char sex){
        //String sql = "SELECT rank from " + currYearTable + " where name=" + "'" + name + "'" + " and sex=" + "'" + sex + "'";

    }


}