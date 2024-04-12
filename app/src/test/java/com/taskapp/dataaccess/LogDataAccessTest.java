package com.taskapp.dataaccess;
import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.taskapp.model.Log;

public class LogDataAccessTest {
    private static final String TEST_FILE_PATH = "src/test/resources/test_logs.csv";
    private static final String BACKUP_FILE_PATH = "src/test/resources/test_logs.csv.bak";
    private LogDataAccess logDataAccess;

    @BeforeEach
    public void setUp() throws IOException {
        Path originalFile = Paths.get(TEST_FILE_PATH);
        Path backupFile = Paths.get(BACKUP_FILE_PATH);

        // 万が一バックアップ用のファイルが削除されていなかった時の対処
        if (Files.exists(backupFile)) {
            tearDown();
        }

        Files.copy(originalFile, backupFile);

        logDataAccess = new LogDataAccess(TEST_FILE_PATH);
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
        Log newLog = new Log(1, 1, 1, LocalDate.now());

        logDataAccess.save(newLog);

        List<Log> logs = readLogsFromFile(TEST_FILE_PATH);

        assertThat(logs).contains(newLog);
    }

    @Tag("Q5")
    @Test
    public void testFindAll() {
        List<Log> actuaList = logDataAccess.findAll();
        List<Log> expectedList = readLogsFromFile(TEST_FILE_PATH);
        assertThat(actuaList).isEqualTo(expectedList);
    }

    @Tag("Q5")
    @Test
    public void testDeleteByTaskCode() {
        Log log1 = new Log(1, 1, 1, LocalDate.now());
        Log log2 = new Log(2, 2, 2, LocalDate.now());
        Log log3 = new Log(3, 3, 3, LocalDate.now());

        logDataAccess.save(log1);
        logDataAccess.save(log2);
        logDataAccess.save(log3);

        logDataAccess.deleteByTaskCode(2);

        List<Log> logs = readLogsFromFile(TEST_FILE_PATH);

        assertThat(logs).hasSize(8);
        assertThat(logs).doesNotContain(log2);
    }

    private List<Log> readLogsFromFile(String filePath) {
        List<Log> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int taskCode = Integer.parseInt(values[0]);
                int changeUserCode = Integer.parseInt(values[1]);
                int status = Integer.parseInt(values[2]);
                LocalDate date = LocalDate.parse(values[3]);
                Log log = new Log(taskCode, changeUserCode, status, date);
                logs.add(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }
}