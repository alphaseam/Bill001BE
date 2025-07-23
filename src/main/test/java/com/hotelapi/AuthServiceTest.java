package com.hotelapi;

import com.hotelapi.dto.LoginRequest;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RegisterRequest;
import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
import com.hotelapi.service.AuthService;
import com.hotelapi.service.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private User sampleUser;
    private LoginRequest loginForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("test@example.com");
        sampleUser.setPassword("encodedPassword");

        loginForm = new LoginRequest();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword("password");
    }

    @DisplayName("Test case for Successful Login")
    @Test
    void testSuccessfulLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(loginForm.getPassword(), sampleUser.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(sampleUser.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(sampleUser.getEmail())).thenReturn("refresh-token");

        LoginResponse response = authService.login(loginForm);

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @DisplayName("Test case for Invalid Email Login")
    @Test
    void testInvalidEmailOnLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(loginForm));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @DisplayName("Test case for Wrong Password Login")
    @Test
    void testWrongPasswordOnLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(loginForm.getPassword(), sampleUser.getPassword())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(loginForm));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @DisplayName("Test case for Token is Returned After Login")
    @Test
    void testTokenIsReturnedAfterLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(loginForm.getPassword(), sampleUser.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(sampleUser.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(sampleUser.getEmail())).thenReturn("refresh-token");

        LoginResponse response = authService.login(loginForm);

        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @DisplayName("Test case for Successful Registration")
    @Test
    void testSuccessfulRegistration() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("newPassword");

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedNewPassword");

        String result = authService.register(registerRequest);

        assertEquals("User registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Test case for Duplicate Email on Registration")
    @Test
    void testDuplicateEmailOnRegistration() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        assertEquals("Email already in use", ex.getMessage());
    }

    @DisplayName("Test case for JWT Not Generated on Failed Login")
    @Test
    void testJwtNotGeneratedForFailedLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(loginForm.getPassword(), sampleUser.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(loginForm));

        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

    @DisplayName("Test case for Verify Method Call Order on Login")
    @Test
    void testVerifyMethodCallOrderOnLogin() {
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(loginForm.getPassword(), sampleUser.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        authService.login(loginForm);

        InOrder inOrder = inOrder(userRepository, passwordEncoder);
        inOrder.verify(userRepository).findByEmail(loginForm.getEmail());
        inOrder.verify(passwordEncoder).matches(loginForm.getPassword(), sampleUser.getPassword());
    }
}
