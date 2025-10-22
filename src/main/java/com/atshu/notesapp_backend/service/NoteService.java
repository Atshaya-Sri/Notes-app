package com.atshu.notesapp_backend.service;

import com.atshu.notesapp_backend.models.Note;
import com.atshu.notesapp_backend.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.google.cloud.vertexai.VertexAI;
// import com.google.cloud.vertexai.api.GenerateContentRequest;
// import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Value("${GOOGLE_CLOUD_PROJECT_ID}")
    private String projectId;

    @Value("${GOOGLE_CLOUD_LOCATION:us-central1}") // Default region
    private String location;

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

    // ✅ Search notes
    public List<Note> searchNotes(String query) {
        return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    // ✅ Update status
    public Note updateNoteStatus(Long id, String status) {
        Note note = getNoteById(id);
        note.setStatus(status);
        return noteRepository.save(note);
    }

    // ✅ Get notes by status
    public List<Note> getNotesByStatus(String status) {
        return noteRepository.findByStatus(status);
    }

    // ✅ AI Summarization (Gemini via Vertex AI)
    public String summarizeNote(Long id) {
        Note note = getNoteById(id);
        String noteContent = note.getContent();

        if (noteContent == null || noteContent.trim().isEmpty()) {
            return "Note is empty, nothing to summarize.";
        }

        try (VertexAI vertexAI = new VertexAI(projectId, location)) {

            // Initialize Gemini model
            GenerativeModel model = new GenerativeModel("gemini-1.5-flash", vertexAI);

            // Build prompt
            String prompt = "Summarize the following note in one or two sentences:\n\n" + noteContent;

            // Send request to Vertex AI
            GenerateContentResponse response = model.generateContent(
                    Content.newBuilder()
                            .addParts(Part.newBuilder().setText(prompt).build())
                            .build()
            );

            // Extract and return summary text
            String summary = response.getCandidatesCount() > 0
                    ? response.getCandidates(0).getContent().getParts(0).getText()
                    : "No summary generated.";

            return summary.trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error initializing Vertex AI client: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not summarize note. " + e.getMessage();
        }
    }
}
