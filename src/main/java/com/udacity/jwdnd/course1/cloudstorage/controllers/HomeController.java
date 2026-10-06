package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public HomeController(FileService fileService, NoteService noteService,
            CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping({ "/home", "/proxy/8080/home" })
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if (user != null) {
            Integer userId = user.getUserid();
            model.addAttribute("files", fileService.getFilesByUser(userId));
            model.addAttribute("notes", noteService.getNotesByUser(userId));
            model.addAttribute("credentials", credentialService.getCredentialsByUser(userId));
            model.addAttribute("encryptionService", credentialService);
        }

        return "home";
    }
}