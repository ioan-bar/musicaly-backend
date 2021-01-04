package ro.playshot.musicaly.repository;

import org.springframework.stereotype.Component;
import ro.playshot.musicaly.model.Song;

import java.util.List;

@Component
public class MusicalyDatabase {

    public List<Song> getSongs() {
        return XPathParser.getAllSongs();
    }

    public void deleteSongsByName(String title) {
        XPathParser.removeSong(title);
    }

    public void addSong(Song song) {
        XPathParser.addSong(song);
    }

    public List<String> getGenres() {
        return XPathParser.getAllGenres();
    }

    public List<String> getAllSongsOfGenre(String genre) {
        return XPathParser.getAllSongsOfGenre(genre);
    }

    public List<Song> getAllSongsOfSinger(String singer) {
        return XPathParser.getAllSongsOfSinger(singer);
    }

    public List<String> getSingers() {
        return XPathParser.getAllSingers();
    }
}
