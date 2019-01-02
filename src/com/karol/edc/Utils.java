package com.karol.edc;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


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
        if(file.exists()) {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNext()) {
                result.add(getTask(scanner.nextLine()));
            }
            while (scanner.hasNext()) {
                result.add(getTask(scanner.nextLine()));
            }
        }
        return result;
    }

    protected static ArrayList<Task> getDataFromCSV(File report) throws FileNotFoundException {
        ArrayList<Task> result = new ArrayList<>();
        File file = report;
        if(file.exists()) {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNext()) {
                result.add(getTask(scanner.nextLine()));
            }
            while (scanner.hasNext()) {
                result.add(getTask(scanner.nextLine()));
            }
        }
        return result;
    }

    private static Task getTask(String line){
        StringTokenizer stringTokenizer = new StringTokenizer(line);

        return new Task(Integer.parseInt(stringTokenizer.nextToken(";")),stringTokenizer.nextToken(";"),Integer.parseInt(stringTokenizer.nextToken(";")));
    }

    protected static void createCopyofCSV() throws IOException {
        Date date = new Date();
        String suffix = String.valueOf(date.getTime());
        File file = new File("zlecenia.csv");
        if(file.exists()) {
            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(file.getAbsolutePath() + suffix));
        }
    }

    protected static void safe2csv(ArrayList<Task> tasks) throws  IOException{
        Collections.reverse(tasks);

        File file = new File("zlecenia.csv");
        if(file.exists()) {
            Files.delete(Paths.get(file.getAbsolutePath()));
        }
        File newFile = new File("zlecenia.csv");
        newFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(newFile);
        for (Task task : tasks) {
            printWriter.write(task.toString());
            printWriter.write("\r\n");
        }
        printWriter.close();
    }

    protected static void delZlecCsv() throws IOException{
        File file = new File("zlecenia.csv");
        if(file.exists()) {
            Files.delete(Paths.get(file.getAbsolutePath()));
        }
    }

    protected static void safe2csvEndMonth(ArrayList<Task> tasks,String month) throws  IOException{
        Collections.reverse(tasks);

        File file = new File(String.format("zlecenia_%s.csv",month));
        if(file.exists()) {
            Files.delete(Paths.get(file.getAbsolutePath()));
        }
        File newFile = new File(String.format("zlecenia_%s.csv",month));
        newFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(newFile);
        for (Task task : tasks) {
            printWriter.write(task.toString());
            printWriter.write("\r\n");
        }
        printWriter.close();
    }


}
