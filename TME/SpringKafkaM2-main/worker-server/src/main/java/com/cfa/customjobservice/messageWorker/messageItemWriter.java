package com.cfa.customjobservice.messageWorker;

import com.cfa.objects.letter.Letter;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class messageItemWriter implements ItemWriter<List<Letter>> {
    @Override
    public void write(List<? extends List<Letter>> letters) throws Exception {
        for (List<Letter> listLetter : letters){
            for (Letter l : listLetter) {
                System.out.println("La demande "+l.getMessage()+" est traitée");
            }
        }
    }
}
