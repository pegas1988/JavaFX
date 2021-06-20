package com.javafx.exampl.dao;

import com.javafx.exampl.entity.Note;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NoteDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public static final String INSERT_QUERY = "INSERT INTO note(description, created_time) VALUES (?, ?)";
    public static final String DELETE_QUERY = "DELETE from note where id = ?";
    public static final String FIND_ALL = "SELECT* from note";

    public Note create(Note note) throws DaoException {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, note.getDescription());
            Timestamp timestamp = Timestamp.valueOf(note.getCreatedTime());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(3);
            note.setId(id);
            return note;
        } catch (SQLException | ClassNotFoundException e) {
            throw new DaoException("Failed to connect");
        }
    }

    public ArrayList<Note> getAllNotes() throws DaoException {
        ArrayList<Note> array = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL);
        ) {
            while (resultSet.next()) {
                Note note = new Note();
                note.setCreatedTime(resultSet.getTimestamp("created_time").toLocalDateTime());
                note.setDescription(resultSet.getString("description"));
                note.setId(resultSet.getInt("id"));
                array.add(note);
            }
        } catch (SQLException | ClassNotFoundException w) {
            throw new DaoException("Failed to delete");
        }
        return array;
    }

    public void delete(Note note) throws DaoException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)
        ) {
            preparedStatement.setInt(1, note.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException w) {
            throw new DaoException("Failed to delete");
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
