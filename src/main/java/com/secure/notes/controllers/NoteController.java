package com.secure.notes.controllers;

import com.secure.notes.exceptions.ResourceNotFoundException;
import com.secure.notes.models.Note;
import com.secure.notes.response.ApiResponse;
import com.secure.notes.services.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createNote(@RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            Note note = noteService.createNoteForUser(username, content);
            return ResponseEntity.ok(new ApiResponse("Success.", note));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("")
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long noteId, @RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", noteService.updateNoteForUser(noteId, content)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse> deleteNote(@PathVariable Long noteId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            noteService.deleteNoteForUser(noteId, userDetails.getUsername());
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
