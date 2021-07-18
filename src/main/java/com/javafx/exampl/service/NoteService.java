package com.javafx.exampl.service;

import com.javafx.exampl.dao.DaoException;
import com.javafx.exampl.dao.NoteDao;
import com.javafx.exampl.entity.Note;

import java.util.ArrayList;

public class NoteService {

    private NoteDao noteDao = new NoteDao();

    public Note create(Note note) throws ServiceException {
        try {
            return noteDao.create(note);
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }
    }

    public void delete(Note note) {
        try {
            noteDao.delete(note);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Note> findAllNotes() throws DaoException {
        return noteDao.getAllNotes();
    }

}
