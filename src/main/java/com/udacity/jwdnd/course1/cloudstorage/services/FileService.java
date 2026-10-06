package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUser(Integer userid) {
        return fileMapper.getFilesByUserId(userid);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean isFilenameAvailable(Integer userid, String filename) {
        return fileMapper.getFileByNameAndUser(userid, filename) == null;
    }

    public int addFile(MultipartFile multipartFile, Integer userid) throws IOException {
        byte[] fileData = multipartFile.getBytes();
        String filename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());

        File file = new File(null, filename, contentType, fileSize, userid, fileData);
        return fileMapper.insert(file);
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.delete(fileId);
    }
}