package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping({"/credential/add-update", "/proxy/8080/credential/add-update"})
    public String addOrUpdateCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        User user = userService.getUser(authentication.getName());
        
        if (user != null) {
            credential.setUserid(user.getUserid());

            int rows;
            if (credential.getCredentialid() != null && credential.getCredentialid() > 0) {
                rows = credentialService.updateCredential(credential);
            } else {
                rows = credentialService.addCredential(credential);
            }

            if (rows > 0) {
                model.addAttribute("isSuccess", true);
            } else {
                model.addAttribute("isSuccess", false);
                model.addAttribute("errorType", "Error saving the credential.");
            }
        } else {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "User not found.");
        }

        return "result";
    }

    @GetMapping({"/credential/delete/{credentialid}", "/proxy/8080/credential/delete/{credentialid}"})
    public String deleteCredential(@PathVariable Integer credentialid, Model model) {
        int rows = credentialService.deleteCredential(credentialid);
        if (rows > 0) {
            model.addAttribute("isSuccess", true);
        } else {
            model.addAttribute("isSuccess", false);
            model.addAttribute("errorType", "Error deleting the credential.");
        }
        return "result";
    }
}