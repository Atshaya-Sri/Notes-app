package com.atshu.notesapp_backend.controller;

import org.springframework.web.bind.annotation.*;
import com.atshu.notesapp_backend.service.NoteService;
import com.atshu.notesapp_backend.models.Note;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable("id") Long id) {
    return noteService.getNoteById(id);
    }

@PutMapping("/{id}")
public Note updateNote(@PathVariable("id") Long id, @RequestBody Note note) {
    return noteService.updateNote(id, note);
}

@DeleteMapping("/{id}")
public void deleteNote(@PathVariable("id") Long id) {
    noteService.deleteNote(id);
}
@GetMapping("/search")
    public List<Note> searchNotes(@RequestParam("query") String query) {
        return noteService.searchNotes(query);
    }
}
