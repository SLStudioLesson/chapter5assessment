package com.taskapp.logic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.User;

public class UserLogicTest {
    @Mock
    private UserDataAccess userDataAccess;

    private UserLogic userLogic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userLogic = new UserLogic(userDataAccess);
    }

    @Tag("Q1")
    @Test
    public void testLogin() throws AppException {
        // Create mock input values
        String email = "john@example.com";
        String password = "password";

        // Create a mock user
        User user = new User(1, "John", email, password);

        // Mock the findByEmailAndPassword method of userDataAccess to return the mock user
        when(userDataAccess.findByEmailAndPassword(email, password)).thenReturn(user);

        // Call the login method
        User result = userLogic.login(email, password);

        // Verify that the findByEmailAndPassword method of userDataAccess is called
        verify(userDataAccess).findByEmailAndPassword(email, password);

        // Verify that the returned user is the expected user
        assertThat(result).isEqualToComparingFieldByField(user);
    }

}
