package com.taskapp.dataaccess;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.taskapp.model.User;

public class UserDataAccessTest {
    private static final String TEST_FILE_PATH = "src/test/resources/test_users.csv";
    private UserDataAccess userDataAccess;

    @BeforeEach
    public void setUp() {
        userDataAccess = new UserDataAccess(TEST_FILE_PATH);
    }

    @Tag("Q1")
    @Test
    public void testFindByEmailAndPassword() {
        User expectedUser = new User(1, "鈴木一郎", "test1@example.com", "password1");

        User actualUser = userDataAccess.findByEmailAndPassword("test1@example.com", "password1");

        assertThat(actualUser).isEqualToComparingFieldByField(expectedUser);
    }

    @Test
    public void testFindByCode() {
        User expectedUser = new User(1, "鈴木一郎", "test1@example.com", "password1");

        User actualUser = userDataAccess.findByCode(1);

        assertThat(actualUser).isEqualToComparingFieldByField(expectedUser);
    }
}
