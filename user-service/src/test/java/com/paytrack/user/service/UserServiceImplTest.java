package com.paytrack.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.paytrack.user.dto.UserResponse;
import com.paytrack.user.entity.User;
import com.paytrack.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    void setup() {

        user = new User();

        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@gmail.com");
        user.setPhone("9999999999");
        user.setCity("Bangalore");
        user.setMonthlyBudget(5000.0);
        user.setRemainingBudget(5000.0);
        user.setSavingsGoal(1000.0);
    }

    /* ================= CREATE ================= */

    @Test
    void testCreate() {

        when(repository.save(any(User.class)))
                .thenReturn(user);

        User result = service.create(user);

        assertNotNull(result);

        assertEquals(
                "john",
                result.getUsername()
        );

        verify(repository).save(user);
    }

    /* ================= GET ALL ================= */

    @Test
    void testGetAll() {

        when(repository.findAll())
                .thenReturn(List.of(user));

        List<UserResponse> users =
                service.getAll();

        assertEquals(1, users.size());

        assertEquals(
                "john",
                users.get(0).getUsername()
        );

        verify(repository).findAll();
    }

    /* ================= GET USERNAME ================= */

    @Test
    void testGetByUsername() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        User result =
                service.getByUsername("john");

        assertEquals(
                "john",
                result.getUsername()
        );
    }

    @Test
    void testGetByUsername_NotFound() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.getByUsername("john")
        );
    }

    /* ================= UPDATE PROFILE ================= */

    @Test
    void testUpdateProfile() {

        User update = new User();

        update.setPhone("8888888888");
        update.setCity("Chennai");
        update.setMonthlyBudget(6000.0);
        update.setSavingsGoal(2000.0);

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(repository.save(any(User.class)))
                .thenReturn(user);

        service.updateProfile(
                "john",
                update
        );

        assertEquals(
                "8888888888",
                user.getPhone()
        );

        assertEquals(
                "Chennai",
                user.getCity()
        );

        assertEquals(
                6000.0,
                user.getMonthlyBudget()
        );

        verify(repository).save(user);
    }

    /* ================= DEDUCT BUDGET ================= */

    @Test
    void testDeductBudget() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(repository.save(any(User.class)))
                .thenReturn(user);

        service.deductBudget(
                "john",
                1000.0
        );

        assertEquals(
                4000.0,
                user.getRemainingBudget()
        );
    }

    @Test
    void testDeductBudget_WhenNegative() {

        user.setRemainingBudget(500.0);

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(repository.save(any(User.class)))
                .thenReturn(user);

        service.deductBudget(
                "john",
                1000.0
        );

        assertEquals(
                0.0,
                user.getRemainingBudget()
        );
    }
}