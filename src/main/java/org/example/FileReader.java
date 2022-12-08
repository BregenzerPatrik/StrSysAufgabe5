package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FileReader {
    private String path = "C:\\Users\\patri\\Desktop\\Wintersemester22-23\\StreamSys\\StrSys_Aufgabe_5\\StrSys_Aufgabe_5\\sample_data.csv";

    public void readFile() {
        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String processedLine = processLine(line);
                PathData data = PathData.createPathData(processedLine);
                KafkaProducer.sendPathData(data);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String processLine(String line) {
        String[] splitLine = line.split(",");
        splitLine[2] = Long.toString(timeStringToLong(splitLine[2]));
        splitLine[3] = Long.toString(timeStringToLong(splitLine[3]));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < splitLine.length; ++i) {
            result.append(splitLine[i] + ",");
        }
        result.append(System.currentTimeMillis());

        return result.toString();
    }


    private Long timeStringToLong(String timeString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(timeString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp.getTime();
        } catch (Exception e) { //this generic but you can control another types of exception
            System.out.println(e);
        }
        return (long) 0;
    }


}
