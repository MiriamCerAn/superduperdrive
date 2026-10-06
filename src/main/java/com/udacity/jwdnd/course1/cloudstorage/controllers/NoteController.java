package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping({ "/note/add-update", "/proxy/8080/note/add-update" })
    public String addOrUpdateNote(Authentication authentication, @ModelAttribute Note note, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if (user != null) {
            if (note.getNoteid() != null && note.getNoteid() > 0) {
                noteService.updateNote(note);
            } else {
                note.setUserid(user.getUserid());
                noteService.addNote(note);
            }
            model.addAttribute("isSuccess", true);
        } else {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Error saving the note.");
        }

        return "result";
    }

    @GetMapping({ "/note/delete/{id}", "/proxy/8080/note/delete/{id}" })
    public String deleteNote(@PathVariable("id") Integer noteId, Model model) {
        if (noteId != null) {
            noteService.deleteNote(noteId);
            model.addAttribute("isSuccess", true);
        } else {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Error deleting the note.");
        }
        return "result";
    }
}