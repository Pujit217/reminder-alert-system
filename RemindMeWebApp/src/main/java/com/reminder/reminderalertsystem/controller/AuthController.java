package com.reminder.reminderalertsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.reminder.reminderalertsystem.model.User;
import com.reminder.reminderalertsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * Handles login, register, and logout pages.
 */
@Controller
public class AuthController {

    /**
     * Service used for user logic.
     */
    private UserService userService;

    /**
     * Creates an auth controller with the given service.
     *
     * @param userService service for users
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows the login page.
     *
     * @return login page
     */
    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Shows the register page.
     *
     * @return register page
     */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    /**
     * Registers a new user.
     *
     * @param user user from the form
     * @param model page model
     * @return login page or register page
     */
    @PostMapping("/register")
    public String registerUser(User user, Model model) {

        User savedUser = userService.registerUser(user);

        if (savedUser == null) {
            model.addAttribute("error", "Email already exists.");
            return "register";
        }

        model.addAttribute("message", "Registration successful. Please login.");
        return "login";
    }

    /**
     * Logs in a user.
     *
     * @param email user's email
     * @param password user's password
     * @param session browser session
     * @param model page model
     * @return dashboard page or login page
     */
    @PostMapping("/login")
    public String loginUser(String email, String password,
            HttpSession session, Model model) {

        User user = userService.loginUser(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        session.setAttribute("loggedInUser", user);
        return "redirect:/dashboard";
    }

    /**
     * Logs out the current user.
     *
     * @param session browser session
     * @return login page
     */
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}