package bot.processor;

import bot.externalservice.apium.data.Station;

import java.io.*;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Utilities {
    private Utilities() {
        throw new UnsupportedOperationException();
    }
    public static String parseInput(String input) {
        input = input.toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        input = input.replaceAll("ł","l");
        input = input.replaceAll("[()]","");
        input = input.replaceAll("[.+-]"," ");
        input = input.replaceAll("\\.$","");
        input = removeExcessiveWhitespaces(input);
        input = input.trim();
        return input;
    }


    public static String removeExcessiveWhitespaces(String str){
        return str.replaceAll("\\s+"," ");
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getTime(String pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static long compareTimes(String time1, String time2, String pattern){
        //returns 0 if time1 is equal time2
        //returns 1 if time1 is before time2
        //returns -1 if time1 if after time2
        long diff;
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date d1 = dateFormat.parse(time1);
            Date d2 = dateFormat.parse(time2);
            diff = d2.getTime() - d1.getTime();
        }
        catch(Exception e)
        {
            diff = 0;
        }
        return diff;
    }

    public static int msToMin(long ms){
        return Math.round(ms/60000);
    }

    public static <T> void serializeObject(T o, String path){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (Exception e){

        }
    }

    public static <T> T deserializeObject(String path){
        T o;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            o = (T) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e){
            return null;
        }
        return o;
    }

    public static boolean objectExists(String path){
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

}