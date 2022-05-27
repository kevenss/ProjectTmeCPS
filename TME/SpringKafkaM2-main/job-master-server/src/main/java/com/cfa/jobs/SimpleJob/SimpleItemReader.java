package com.cfa.jobs.SimpleJob;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemReader implements ItemReader<List<String>> {

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("Hello");
        messages.add("World");
        messages.add("pppp");
        return messages;
    }
}
