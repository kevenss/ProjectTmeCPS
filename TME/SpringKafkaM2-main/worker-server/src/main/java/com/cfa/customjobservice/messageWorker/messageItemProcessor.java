package com.cfa.customjobservice.messageWorker;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class messageItemProcessor implements ItemProcessor<List<String>,List<Letter>> {
    @Override
    public List<Letter> process(List<String> messages) throws Exception {
        ArrayList<Letter> letters = new ArrayList<>();
        for (String message : messages) {
            Letter letter = new Letter();
            letter.setMessage(message);
            letter.setTreatmentDate(new Date());
            letter.setCreationDate(new Date());
        }
        return letters;
    }
}
