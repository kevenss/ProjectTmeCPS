package com.cfa.objects.letter;

//import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import java.util.List;

@RestController
@RequestMapping(value = "/letter")

public class LetterApiController {
    @Autowired
    LetterRepository _letterRepository;

    @GetMapping("/getAll")
    public List<Letter> getAll() {
        return _letterRepository.findAll();
    }

    @GetMapping("/getByMessage")
    public Letter getMyMessage(@RequestParam(value = "message", defaultValue = "message_1") String input) {
        return _letterRepository.getByMessage(input);
    }
}
