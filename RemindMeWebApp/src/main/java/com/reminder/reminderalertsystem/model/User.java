package com.reminder.reminderalertsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a user of the reminder alert system.
 */
@Entity
public class User {

    /**
     * Unique ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * Name of the user.
     */
    private String name;

    /**
     * Email address of the user.
     */
    private String email;
    
    /**
     * Password used by the user to login.
     */
    private String password;

    /**
     * Default constructor.
     */
    public User() {

    }

    /**
     * Creates a user with the given information.
     *
     * @param name user's name
     * @param email user's email
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the user ID.
     *
     * @return user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId user ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the user's name.
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's email.
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Returns the user's password.
     *
     * @return user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password user password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}