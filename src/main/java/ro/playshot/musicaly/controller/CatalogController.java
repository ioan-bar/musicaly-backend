package ro.playshot.musicaly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.playshot.musicaly.model.Song;
import ro.playshot.musicaly.repository.MusicalyDatabase;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
class CatalogController {

    @Autowired
    private final MusicalyDatabase repository;


    CatalogController(MusicalyDatabase repository) {
        this.repository = repository;
    }

    @GetMapping("/songs")
    List<Song> getAllSongs() {
        return repository.getSongs();
    }

    @DeleteMapping("/song")
    public void deleteEmployee(@PathParam("title") String title) {
        repository.deleteSongsByName(title);
    }

    @GetMapping("/song")
    public List<Song> getAllSongsOfSinger(@PathParam("singer") String singer) {
        return repository.getAllSongsOfSinger(singer);
    }

    @PostMapping("/song")
    public void addSong(@RequestBody Song song) {
        repository.addSong(song);
    }
}
