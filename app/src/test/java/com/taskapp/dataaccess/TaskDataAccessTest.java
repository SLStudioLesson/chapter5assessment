package com.taskapp.dataaccess;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccessTest {
    private static final String TEST_FILE_PATH = "src/test/resources/test_tasks.csv";
    private static final String BACKUP_FILE_PATH = "src/test/resources/test_tasks.csv.bak";
    private static final String TEST_FILE_PATH_USER = "src/test/resources/test_users.csv";
    private TaskDataAccess taskDataAccess;
    private UserDataAccess userDataAccess;

    @BeforeEach
    public void setUp() throws IOException {
        // Backup the original file
        Path originalFile = Paths.get(TEST_FILE_PATH);
        Path backupFile = Paths.get(BACKUP_FILE_PATH);

        // 万が一バックアップ用のファイルが削除されていなかった時の対処
        if (Files.exists(backupFile)) {
            tearDown();
        }
        Files.copy(originalFile, backupFile);

        userDataAccess = new UserDataAccess(TEST_FILE_PATH_USER);
        taskDataAccess = new TaskDataAccess(TEST_FILE_PATH, userDataAccess);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Path originalFile = Paths.get(TEST_FILE_PATH);
        Path backupFile = Paths.get(BACKUP_FILE_PATH);
        Files.copy(backupFile, originalFile, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(backupFile);
    }

    @Tag("Q3")
    @Test
    public void testSave() {
        User repUser = new User(1, "鈴木一郎", "test1@example.com", "password1");
        Task newTask = new Task(5, "Task test", 0, repUser);

        taskDataAccess.save(newTask);

        List<Task> tasks = readTasksFromFile(TEST_FILE_PATH);

        assertThat(tasks).contains(newTask);
    }

    @Tag("Q2")
    @Test
    public void testFindAll() {
        List<Task> actualList = taskDataAccess.findAll();
        List<Task> expectedList = readTasksFromFile(TEST_FILE_PATH);
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Tag("Q4")
    @Test
    public void testFindByCode() {
        User repUser = new User(1, "鈴木一郎", "test1@example.com", "password1");
        Task task1 = new Task(1, "Task 1", 1, repUser);
        Task task2 = new Task(2, "Task 2", 2, repUser);
        Task task3 = new Task(3, "Task 3", 3, repUser);

        taskDataAccess.save(task1);
        taskDataAccess.save(task2);
        taskDataAccess.save(task3);

        Task foundTask = taskDataAccess.findByCode(2);

        assertThat(foundTask).isEqualTo(task2);
    }

    @Tag("Q4")
    @Test
    public void testUpdate() {
        User repUser = new User(1, "鈴木一郎", "test1@example.com", "password1");
        Task task1 = new Task(1, "Task 1", 1, repUser);
        Task task2 = new Task(2, "Task 2", 2, repUser);
        Task task3 = new Task(3, "Task 3", 3, repUser);

        taskDataAccess.save(task1);
        taskDataAccess.save(task2);
        taskDataAccess.save(task3);

        Task updatedTask = new Task(2, "Updated Task 2", 2, repUser);
        taskDataAccess.update(updatedTask);

        List<Task> tasks = readTasksFromFile(TEST_FILE_PATH);

        assertThat(tasks).contains(updatedTask);
    }

    @Tag("Q5")
    @Test
    public void testDelete() {
        User repUser = new User(1, "鈴木一郎", "test1@example.com", "password1");
        Task task1 = new Task(1, "Task 1", 1, repUser);
        Task task2 = new Task(2, "Task 2", 2, repUser);
        Task task3 = new Task(3, "Task 3", 3, repUser);

        taskDataAccess.save(task1);
        taskDataAccess.save(task2);
        taskDataAccess.save(task3);

        taskDataAccess.delete(2);

        List<Task> tasks = readTasksFromFile(TEST_FILE_PATH);

        assertThat(tasks).hasSize(5);
        assertThat(tasks).doesNotContain(task2);
    }

    private List<Task> readTasksFromFile(String filePath) {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int code = Integer.parseInt(values[0]);
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                int repUserCode = Integer.parseInt(values[3]);
                User repUser = userDataAccess.findByCode(repUserCode);
                Task task = new Task(code, name, status, repUser);
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}