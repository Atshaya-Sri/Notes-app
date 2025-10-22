package com.atshu.notesapp_backend.service;

import com.atshu.notesapp_backend.models.Note;
import com.atshu.notesapp_backend.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // ✅ Create note
    public Note createNote(Note note) {
        if (note.getStatus() == null || note.getStatus().isEmpty()) {
            note.setStatus("active");
        }
        return noteRepository.save(note);
    }

    // ✅ Get all notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // ✅ Get note by ID
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    // ✅ Update note
    public Note updateNote(Long id, Note updatedNote) {
        Note existingNote = getNoteById(id);
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        existingNote.setStatus(updatedNote.getStatus());
        return noteRepository.save(existingNote);
    }

    // ✅ Delete note
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    // ✅ Search notes (simple example)
    public List<Note> searchNotes(String query) {
        return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    // ✅ Update status (used for archive, bin, restore)
    public Note updateNoteStatus(Long id, String status) {
        Note note = getNoteById(id);
        note.setStatus(status);
        return noteRepository.save(note);
    }

    // ✅ Get notes by status
    public List<Note> getNotesByStatus(String status) {
        return noteRepository.findByStatus(status);
    }
}
