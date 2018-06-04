package com.karol.edc;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;



public class Utils {

    protected static String Double2String(Double x){
        int y = x.intValue();
        return String.valueOf(y);
    }

    protected static int String2Int(String x){
        StringTokenizer stringTokenizer = new StringTokenizer(x);
        String tmp =stringTokenizer.nextToken(" ");
        int y = Integer.parseInt(tmp);
        return y;
    }

    protected static ArrayList<Task> getDataFromCSV() throws FileNotFoundException {
        ArrayList<Task> result = new ArrayList<>();
        File file = new File("zlecenia.csv");
        Scanner scanner = new Scanner(file);
        result.add(getTask(scanner.nextLine()));
        while (scanner.hasNext()){
            result.add(getTask(scanner.nextLine()));
        }
        return result;
    }

    private static Task getTask(String line){
        StringTokenizer stringTokenizer = new StringTokenizer(line);

        return new Task(Integer.parseInt(stringTokenizer.nextToken(";")),stringTokenizer.nextToken(";"),Integer.parseInt(stringTokenizer.nextToken(";")));
    }

    protected static void createCopyofCSV() throws IOException {
        Date date = new Date();
        String suffix = date.toString();
        File file = new File("zlecenia.csv");
        Files.copy(Paths.get(file.getAbsolutePath()),Paths.get(file.getAbsolutePath()+suffix));
    }

    protected static void safe2csv(ArrayList<Task> tasks) throws  IOException{
        File file = new File("zlecenia.csv");
        Files.delete(Paths.get(file.getAbsolutePath()));
        File newFile = new File("zlecenia.csv");
        PrintWriter printWriter = new PrintWriter(newFile);
        for (int x=0;x<tasks.size();x++){
            printWriter.write(tasks.get(x).toString());
            printWriter.write("\r\n");
        }
        printWriter.close();
    }

}
