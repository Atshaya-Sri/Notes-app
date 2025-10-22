package com.atshu.notesapp_backend.controller;

import org.springframework.web.bind.annotation.*;
import com.atshu.notesapp_backend.service.NoteService;
import com.atshu.notesapp_backend.models.Note;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(origins = {"https://notes-app-2-apin.onrender.com/api/notes", "http://localhost:5173"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ✅ Create a new note (default status: active)
    @PostMapping
    public Note createNote(@RequestBody Note note) {
        if (note.getStatus() == null || note.getStatus().isEmpty()) {
            note.setStatus("active");
        }
        return noteService.createNote(note);
    }

    // ✅ Get all notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    // ✅ Get note by ID
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable("id") Long id) {
        return noteService.getNoteById(id);
    }

    // ✅ Update note
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable("id") Long id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    // ✅ Delete note
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") Long id) {
        noteService.deleteNote(id);
    }

    // ✅ Search notes
    @GetMapping("/search")
    public List<Note> searchNotes(@RequestParam("query") String query) {
        return noteService.searchNotes(query);
    }

    // ✅ Archive a note
    @PutMapping("/{id}/archive")
    public Note archiveNote(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "archived");
    }

    // ✅ Move note to bin
    @PutMapping("/{id}/bin")
    public Note moveToBin(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "binned");
    }

    // ✅ Restore note to active
    @PutMapping("/{id}/restore")
    public Note restoreNote(@PathVariable Long id) {
        return noteService.updateNoteStatus(id, "active");
    }

    // ✅ Get all active notes
    @GetMapping("/active")
    public List<Note> getActiveNotes() {
        return noteService.getNotesByStatus("active");
    }

    // ✅ Get all archived notes
    @GetMapping("/archived")
    public List<Note> getArchivedNotes() {
        return noteService.getNotesByStatus("archived");
    }

    // ✅ Get all binned notes
    @GetMapping("/binned")
    public List<Note> getBinnedNotes() {
        return noteService.getNotesByStatus("binned");
    }
}
