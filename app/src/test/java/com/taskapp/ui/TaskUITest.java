package com.taskapp.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUITest {
    @Mock
    private BufferedReader reader;

    @Mock
    private UserLogic userLogic;

    @Mock
    private TaskLogic taskLogic;

    private TaskUI taskUI;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskUI = new TaskUI(reader, userLogic, taskLogic);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // @Test
    // public void testDisplayMenu() throws Exception {
    //     when(reader.readLine())
    //             .thenReturn("john@example.com", "password")
    //             .thenReturn("1", "3");

    //     User user = new User(1, "John", "john@example.com", "password");
    //     when(userLogic.login("john@example.com", "password")).thenReturn(user);

    //     taskUI.displayMenu();

    //     verify(userLogic).login("john@example.com", "password");

    //     verify(taskLogic).showAll(user);
    // }

    @Tag("Q1")
    @Test
    public void testInputLogin() throws Exception {
        when(reader.readLine()).thenReturn("test@example.com", "password");
        when(userLogic.login("test@example.com", "password")).thenReturn(new User(0, null, null, null));
        taskUI.inputLogin();

        String[] expectedMessage = {
                "メールアドレスを入力してください：",
                "パスワードを入力してください："
        };
        verify(userLogic, times(1)).login("test@example.com", "password");
        assertThat(outContent.toString()).contains(expectedMessage);
    }

    @Tag("Q3")
    @Test
    public void testInputNewInformation() throws Exception {
        when(reader.readLine()).thenReturn("1", "Test Task", "1");
        doNothing().when(taskLogic).save(1, "Tst Task", 1, new User(0, null, null, null));
        taskUI.inputNewInformation();

        verify(taskLogic, times(1)).save(1, "Test Task", 1, null);
        String[] expectedMessage = {
                "タスクコードを入力してください：",
                "タスク名を入力してください：",
                "担当するユーザーのコードを選択してください："
        };
        assertThat(outContent.toString()).contains(expectedMessage);
    }

    @Test
    public void testInputChangeInformation() throws Exception {
        when(reader.readLine()).thenReturn("1", "Updated Task", "1");
        doNothing().when(taskLogic).changeStatus(1, 1, new User(0, null, null, null));
        taskUI.inputChangeInformation();

        verify(taskLogic, times(1)).changeStatus(1, 1, null);
        String[] expectedMessage = {
                "ステータスを変更するタスクコードを入力してください：",
                "どのステータスに変更するか選択してください。",
                "1. 着手中, 2. 完了"
        };
        assertThat(outContent.toString()).contains(expectedMessage);
    }

    @Test
    public void testInputDeleteInformation() throws Exception {
        when(reader.readLine()).thenReturn("1");
        doNothing().when(taskLogic).delete(1);
        taskUI.inputDeleteInformation();

        verify(taskLogic, times(1)).delete(1);
        String expectedMessage = "削除するタスクコードを入力してください：";
        assertThat(outContent.toString()).contains(expectedMessage);
    }

    @Test
    public void testIsNumeric() {
        assertThat(taskUI.isNumeric("123")).isTrue();
        assertThat(taskUI.isNumeric("0")).isTrue();

        assertThat(taskUI.isNumeric("abc")).isFalse();
        assertThat(taskUI.isNumeric("123abc")).isFalse();
        assertThat(taskUI.isNumeric("")).isFalse();
        assertThat(taskUI.isNumeric(null)).isFalse();
    }

}