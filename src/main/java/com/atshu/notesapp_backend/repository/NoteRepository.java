package com.atshu.notesapp_backend.repository;
import java.util.List; 
import com.atshu.notesapp_backend.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}
