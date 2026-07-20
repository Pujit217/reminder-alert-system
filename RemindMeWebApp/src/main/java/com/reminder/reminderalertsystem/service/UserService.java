package com.reminder.reminderalertsystem.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reminder.reminderalertsystem.model.User;
import com.reminder.reminderalertsystem.repository.ReminderRepository;
import com.reminder.reminderalertsystem.repository.UserRepository;

/**
 * Handles the main logic for users.
 */
@Service
public class UserService {

    /**
     * Repository used to access user data.
     */
    private UserRepository userRepository;

    /**
     * Repository used to access reminder data.
     */
    private ReminderRepository reminderRepository;

    /**
     * Encoder used to safely store passwords.
     */
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates a user service with the required repositories and encoder.
     *
     * @param userRepository repository for users
     * @param reminderRepository repository for reminders
     * @param passwordEncoder encodes passwords
     */
    public UserService(
            UserRepository userRepository,
            ReminderRepository reminderRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.reminderRepository = reminderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param user user to register
     * @return saved user, or null if email already exists
     */
    public User registerUser(User user) {

        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return null;
        }

        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    /**
     * Checks if the login information is correct.
     *
     * @param email user's email
     * @param password user's password
     * @return user if login is correct, otherwise null
     */
    public User loginUser(String email, String password) {

        User user =
                userRepository.findByEmail(email);

        if (user != null
                && passwordEncoder.matches(
                        password,
                        user.getPassword())) {

            return user;
        }

        return null;
    }

    /**
     * Returns all users.
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by ID.
     *
     * @param userId user ID
     * @return user if found, otherwise null
     */
    public User getUserById(Long userId) {

        return userRepository
                .findById(userId)
                .orElse(null);
    }

    /**
     * Deletes a user and all reminders belonging to the user.
     *
     * @param userId ID of the user to delete
     */
    @Transactional
    public void deleteUserAccount(Long userId) {

        if (userId == null) {
            return;
        }

        reminderRepository.deleteAll(
                reminderRepository.findByUserId(userId)
        );

        userRepository.deleteById(userId);
    }
}