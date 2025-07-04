package com.hotelapi;

import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
import com.hotelapi.service.CustomUserDetailsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomUserDetailsService.
 * Tests user loading logic with mocked UserRepository.
 */
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository; // Mocked dependency

    @InjectMocks
    private CustomUserDetailsService userDetailsService; // Class under test

    private User testUser;


     // Initializes test data before each test.

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }


     // Test successful loading of user by email.

    @Test
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    // Test user not found scenario should throw UsernameNotFoundException.

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));
    }


     // Test that the returned UserDetails has the correct username.

    @Test
    public void testUserDetails_HasCorrectUsername() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals(testUser.getEmail(), userDetails.getUsername());
    }


    //  Test that the returned UserDetails has the correct password.

    @Test
    public void testUserDetails_HasCorrectPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals(testUser.getPassword(), userDetails.getPassword());
    }


     // Test that the returned UserDetails has the "ROLE_USER" authority.

    @Test
    public void testUserDetails_RoleIsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }


     // Test that passing null email throws UsernameNotFoundException.

    @Test
    public void testLoadUserByUsername_NullEmail_ShouldThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }


     // Test that empty email string throws UsernameNotFoundException.

    @Test
    public void testLoadUserByUsername_EmptyEmail_ShouldThrowException() {
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(""));
    }


     // Verify repository is called exactly once during user loading.

    @Test
    public void testLoadUserByUsername_CallsRepositoryExactlyOnce() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        userDetailsService.loadUserByUsername("test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }


     // Test loading user with different email works as expected.

    @Test
    public void testLoadUserByUsername_WithDifferentEmail() {
        testUser.setEmail("different@example.com");
        when(userRepository.findByEmail("different@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("different@example.com");

        assertEquals("different@example.com", userDetails.getUsername());
    }


     // Ensure exception thrown has the correct message.

    @Test
    public void testLoadUserByUsername_ThrowsExactMessage() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email));

        assertEquals("User not found", ex.getMessage());
    }
}
