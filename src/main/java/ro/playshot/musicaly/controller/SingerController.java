package ro.playshot.musicaly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.playshot.musicaly.repository.MusicalyDatabase;

import java.util.List;

@RestController
public class SingerController {

    @Autowired
    private final MusicalyDatabase repository;

    SingerController(MusicalyDatabase repository) {
        this.repository = repository;
    }

    @GetMapping("/singers")
    List<String> getAllSingers() {
        return repository.getSingers();
    }


}
