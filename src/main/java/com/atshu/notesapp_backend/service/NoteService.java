package com.atshu.notesapp_backend.service;

import com.atshu.notesapp_backend.models.Note;
import com.atshu.notesapp_backend.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value; // Add this import

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executors;
import java.util.List;


@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Value("${GEMINI_API_KEY}") // Injects the key from Render
    private String geminiApiKey;
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

    public String summarizeNote(Long id) {
        Note note = getNoteById(id);
        String noteContent = note.getContent();
        if (noteContent == null || noteContent.trim().isEmpty()) {
            return "Note is empty, nothing to summarize.";
        }

        try {
            // Set up the model
            GenerativeModel gm = new GenerativeModel("gemini-1.5-flash-latest", geminiApiKey);
            GenerativeModelFutures model = GenerativeModelFutures.from(gm);

            // Create the prompt
            Content content = new Content.Builder()
                    .addText("Summarize the following note in one or two sentences: " + noteContent)
                    .build();

            // Send the request
            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            GenerateContentResponse result = response.get(); // Waits for the response

            // Get and return the text
            return result.getText();

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return "Error: Could not summarize note. " + e.getMessage();
        }
    }
}
