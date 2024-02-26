package com.taskapp.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taskapp.dataaccess.LogDataAccess;
import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.Log;
import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskLogicTest {
    @Mock
    private TaskDataAccess taskDataAccess;
    @Mock
    private LogDataAccess logDataAccess;
    @Mock
    private UserDataAccess userDataAccess;

    private TaskLogic taskLogic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskLogic = new TaskLogic(taskDataAccess, logDataAccess, userDataAccess);
    }

    @Tag("Q2")
    @Test
    public void testShowAll() {
        // Create a mock user
        User loginUser = new User(1, "John", "", "");

        // Create a mock tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Task 1", 0, new User(2, "Alice", "", "")));
        tasks.add(new Task(2, "Task 2", 1, new User(1, "John", "", "")));

        // Mock the findAll method of taskDataAccess to return the mock tasks
        when(taskDataAccess.findAll()).thenReturn(tasks);

        // Call the showAll method
        taskLogic.showAll(loginUser);

        // Verify that the expected output is printed
        verify(taskDataAccess).findAll();
        assertThat(tasks).hasSize(2);
    }

    @Tag("Q3")
    @Test
    public void testSave() throws AppException {
        // Create mock input values
        int code = 1;
        String name = "Task 1";
        int repUserCode = 2;
        User loginUser = new User(1, "John", "", "");

        // Create a mock user
        User repUser = new User(repUserCode, "Alice", "", "");

        // Mock the findByCode method of userDataAccess to return the mock repUser
        when(userDataAccess.findByCode(repUserCode)).thenReturn(repUser);

        // Call the save method
        taskLogic.save(code, name, repUserCode, loginUser);

        // Verify that the save method of taskDataAccess and logDataAccess are called
        verify(taskDataAccess).save(any(Task.class));
        verify(logDataAccess).save(any(Log.class));
    }

    @Tag("Q4")
    @Test
    public void testChangeStatus() throws AppException {
        // Create mock input values
        int code = 1;
        int status = 1;
        User loginUser = new User(1, "John", "", "");

        // Create a mock task
        Task task = new Task(code, "Task 1", 0, new User(2, "Alice", "", ""));

        // Mock the findByCode method of taskDataAccess to return the mock task
        when(taskDataAccess.findByCode(code)).thenReturn(task);

        // Call the changeStatus method
        taskLogic.changeStatus(code, status, loginUser);

        // Verify that the update method of taskDataAccess and save method of logDataAccess are called
        verify(taskDataAccess).update(any(Task.class));
        verify(logDataAccess).save(any(Log.class));
    }

    @Tag("Q5")
    @Test
    public void testDelete() throws AppException {
        // Create mock input value
        int code = 1;

        // Create a mock task
        Task task = new Task(code, "Task 1", 2, new User(2, "Alice", "", ""));

        // Mock the findByCode method of taskDataAccess to return the mock task
        when(taskDataAccess.findByCode(code)).thenReturn(task);

        // Call the delete method
        taskLogic.delete(code);

        // Verify that the delete method of taskDataAccess and deleteByTaskCode method of logDataAccess are called
        verify(taskDataAccess).delete(code);
        verify(logDataAccess).deleteByTaskCode(code);
    }
}