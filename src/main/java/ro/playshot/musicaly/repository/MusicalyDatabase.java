package ro.playshot.musicaly.repository;

import org.springframework.stereotype.Component;
import ro.playshot.musicaly.model.Song;

import java.util.ArrayList;
import java.util.List;

@Component
public class MusicalyDatabase {


    public List<String> getCatalog() {
        List<String> result = new ArrayList<>();
        result.add("Love Again");
        result.add("Melodie2");
        result.add("Melodie3");
        return result;
    }

    public List<Song> getSongs() {
        return XPathParser.getAllSongs();
    }

    public void deleteSongsByName(String title) {
        XPathParser.removeSong(title);
    }

    public void addSong(Song song) {
        System.out.println("song = " + song);
        XPathParser.addSong(song);
    }

    public List<String> getGenres() {
        return XPathParser.getAllGenres();
    }

    public List<String> getAllSongsOfGenre(String genre) {
        return XPathParser.getAllSongsOfGenre(genre);
    }
}
