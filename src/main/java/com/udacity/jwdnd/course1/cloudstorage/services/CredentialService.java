package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUser(Integer userid) {
        return credentialMapper.getCredentialsByUserId(userid);
    }

    public int addCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword1(), encodedKey);
        credential.setKey1(encodedKey);
        credential.setPassword1(encryptedPassword);
        
        return credentialMapper.insert(credential);
    }

    public int updateCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword1(), encodedKey);
        credential.setKey1(encodedKey);
        credential.setPassword1(encryptedPassword);
        
        return credentialMapper.update(credential);
    }

    public int deleteCredential(Integer credentialid) {
        return credentialMapper.delete(credentialid);
    }

    public String decryptPassword(String encryptedPassword, String key) {
        return encryptionService.decryptValue(encryptedPassword, key);
    }
}