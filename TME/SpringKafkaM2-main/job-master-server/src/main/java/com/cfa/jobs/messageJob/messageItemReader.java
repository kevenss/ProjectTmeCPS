package com.cfa.jobs.messageJob;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class messageItemReader implements ItemReader<List<String>> {

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ArrayList<String> messages = new ArrayList<>();
        try {
            File myFile = new File("filename.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                messages.add(data);
            }
            myReader.close();
            return  messages;
        } catch (FileNotFoundException e) {
            return  null;
        }
    }

}
