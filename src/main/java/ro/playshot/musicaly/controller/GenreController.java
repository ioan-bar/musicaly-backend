package ro.playshot.musicaly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;
import ro.playshot.musicaly.model.Song;
import ro.playshot.musicaly.repository.MusicalyDatabase;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class GenreController {

    @Autowired
    private final MusicalyDatabase repository;

    GenreController(MusicalyDatabase repository) {
        this.repository = repository;
    }

    @GetMapping("/genres")
    List<String> getAllSongs() {
        return repository.getGenres();
    }

    @GetMapping("/genres/songs")
    List<String> getAllSongs(@PathParam("genre") String genre) {
        return repository.getAllSongsOfGenre(genre);
    }


}
