package com.hotelapi;

import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
import com.hotelapi.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    @DisplayName("Should load user successfully by email")
    void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));
    }

    @Test
    @DisplayName("Returned UserDetails should have correct username")
    void testUserDetails_HasCorrectUsername() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    @DisplayName("Returned UserDetails should have correct password")
    void testUserDetails_HasCorrectPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    @DisplayName("Returned UserDetails should contain ROLE_USER authority")
    void testUserDetails_RoleIsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Null email should throw UsernameNotFoundException")
    void testLoadUserByUsername_NullEmail_ShouldThrowException() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(null));
    }

    @Test
    @DisplayName("Empty email should throw UsernameNotFoundException")
    void testLoadUserByUsername_EmptyEmail_ShouldThrowException() {
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(""));
    }

    @Test
    @DisplayName("Repository should be called exactly once")
    void testLoadUserByUsername_CallsRepositoryExactlyOnce() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        userDetailsService.loadUserByUsername("test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should load user with different email correctly")
    void testLoadUserByUsername_WithDifferentEmail() {
        testUser.setEmail("different@example.com");
        when(userRepository.findByEmail("different@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("different@example.com");

        assertEquals("different@example.com", userDetails.getUsername());
    }

    @Test
    @DisplayName("Exception should have exact message when user not found")
    void testLoadUserByUsername_ThrowsExactMessage() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email));

        assertEquals("User not found", ex.getMessage());
    }
}
