package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping({"/file/upload", "/proxy/8080/file/upload"})
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                   Authentication authentication,
                                   Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if (fileUpload.isEmpty()) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Please select a file to upload.");
            return "result";
        }

        if (!fileService.isFilenameAvailable(user.getUserid(), fileUpload.getOriginalFilename())) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "A file with that name already exists.");
            return "result";
        }

        try {
            fileService.addFile(fileUpload, user.getUserid());
            model.addAttribute("isSuccess", true);
        } catch (IOException e) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Could not save the file.");
        }

        return "result";
    }

    @GetMapping({"/file/download/{id}", "/proxy/8080/file/download/{id}"})
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file.getFiledata());
    }

    @GetMapping({"/file/delete/{id}", "/proxy/8080/file/delete/{id}"})
    public String deleteFile(@PathVariable("id") Integer fileId, Model model) {
        if (fileId != null) {
            fileService.deleteFile(fileId);
            model.addAttribute("isSuccess", true);
        } else {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Error deleting the file.");
        }
        return "result";
    }
}