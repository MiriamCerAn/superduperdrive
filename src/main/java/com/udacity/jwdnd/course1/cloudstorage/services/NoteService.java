package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUser(Integer userid) {
        return noteMapper.getNotesByUserId(userid);
    }

    public int addNote(Note note) {
        return noteMapper.insert(note);
    }

    public int updateNote(Note note) {
        return noteMapper.update(note);
    }

    public int deleteNote(Integer noteid) {
        return noteMapper.delete(noteid);
    }
}