package com.reminder.reminderalertsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reminder.reminderalertsystem.model.User;

/**
 * Repository for performing database operations on users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email user's email
     * @return user with the matching email
     */
    User findByEmail(String email);
}