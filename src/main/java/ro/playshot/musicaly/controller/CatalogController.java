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

    @GetMapping("/catalog")
    List<String> all() {
        return repository.getCatalog();
    }

    @GetMapping("/songs")
    List<Song> getAllSongs() {
        return repository.getSongs();
    }

    @DeleteMapping("/song")
    public void deleteEmployee(@PathParam("title") String title) {
        System.out.println("title = " + title);
        repository.deleteSongsByName(title);
    }

    @GetMapping("/song")
    public List<Song> getAllSongsOfSinger(@PathParam("singer") String singer) {
        System.out.println("singer = " + repository.getAllSongsOfSinger(singer));
        return repository.getAllSongsOfSinger(singer);
    }

    @PostMapping("/song")
    public void addSong(@RequestBody Song song) {
        repository.addSong(song);
    }
//
//
//    @GetMapping("/employees/{id}")
//    Employee one(@PathVariable Long id) {
//
//        return repository.findById(id)
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//    }
//
//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        return repository.findById(id)
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//                    return repository.save(employee);
//                })
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(newEmployee);
//                });
//    }
//
//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }
}
