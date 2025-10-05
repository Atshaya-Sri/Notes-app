package com.atshu.notesapp_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.atshu.notesapp_backend.repository.NoteRepository;
import com.atshu.notesapp_backend.models.Note;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note saveNote(Note note) {
    note.setSynced(true); // Mark as synced before saving
    return noteRepository.save(note); // Save to the DB in one step
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    public Note getNoteById(Long id) {
    return noteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
}

public Note updateNote(Long id, Note noteDetails) {
    Note existingNote = getNoteById(id);

    existingNote.setTitle(noteDetails.getTitle());
    existingNote.setContent(noteDetails.getContent());
    existingNote.setTags(noteDetails.getTags());
    existingNote.setSynced(true); // always mark synced

    return noteRepository.save(existingNote);
}

public void deleteNote(Long id) {
    Note existingNote = getNoteById(id);
    noteRepository.delete(existingNote);
}
public List<Note> searchNotes(String query) {
    return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
}
}
