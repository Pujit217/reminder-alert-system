package com.reminder.reminderalertsystem.controller;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.reminder.reminderalertsystem.model.Priority;
import com.reminder.reminderalertsystem.model.Reminder;
import com.reminder.reminderalertsystem.model.ReminderStatus;
import com.reminder.reminderalertsystem.model.User;
import com.reminder.reminderalertsystem.service.ReminderService;
import com.reminder.reminderalertsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * Handles the dashboard page.
 */
@Controller
public class DashboardController {

    /**
     * Service used for reminder logic.
     */
    private ReminderService reminderService;

    /**
     * Service used for user logic.
     */
    private UserService userService;

    /**
     * Creates a dashboard controller with the required services.
     *
     * @param reminderService service for reminders
     * @param userService service for users
     */
    public DashboardController(
            ReminderService reminderService,
            UserService userService) {

        this.reminderService = reminderService;
        this.userService = userService;
    }

    /**
     * Shows the dashboard page for the logged in user.
     *
     * @param session browser session
     * @param model page model
     * @return dashboard page or login page
     */
    @GetMapping("/dashboard")
    public String showDashboard(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        List<Reminder> reminders =
                reminderService.getRemindersByUser(
                        user.getUserId());

        List<Reminder> pendingReminders =
                reminderService.getPendingRemindersByUser(
                        user.getUserId());

        List<Reminder> completedReminders =
                reminderService.getCompletedRemindersByUser(
                        user.getUserId());

        List<Reminder> highPriorityReminders =
                reminderService.getHighPriorityReminders(
                        user.getUserId());

        List<Reminder> mediumPriorityReminders =
                reminderService.getMediumPriorityReminders(
                        user.getUserId());

        List<Reminder> lowPriorityReminders =
                reminderService.getLowPriorityReminders(
                        user.getUserId());

        List<Reminder> upcomingReminders =
                reminderService.getUpcomingRemindersByUser(
                        user.getUserId());

        YearMonth currentMonth =
                YearMonth.now();

        int monthlyTotal = 0;
        int monthlyPending = 0;
        int monthlyCompleted = 0;
        int monthlyHighPriority = 0;

        for (Reminder reminder : reminders) {

            if (reminder.getReminderTime() == null) {
                continue;
            }

            YearMonth reminderMonth =
                    YearMonth.from(
                            reminder.getReminderTime());

            if (!reminderMonth.equals(currentMonth)) {
                continue;
            }

            monthlyTotal++;

            if (reminder.getStatus()
                    == ReminderStatus.PENDING) {

                monthlyPending++;
            }

            if (reminder.getStatus()
                    == ReminderStatus.COMPLETED) {

                monthlyCompleted++;
            }

            if (reminder.getPriority()
                    == Priority.HIGH) {

                monthlyHighPriority++;
            }
        }

        String monthlySummaryMonth =
                currentMonth
                        .getMonth()
                        .getDisplayName(
                                TextStyle.FULL,
                                Locale.ENGLISH);

        model.addAttribute(
                "user",
                user);

        model.addAttribute(
                "totalReminders",
                reminders.size());

        model.addAttribute(
                "pendingReminders",
                pendingReminders.size());

        model.addAttribute(
                "completedReminders",
                completedReminders.size());

        model.addAttribute(
                "highPriority",
                highPriorityReminders.size());

        model.addAttribute(
                "mediumPriority",
                mediumPriorityReminders.size());

        model.addAttribute(
                "lowPriority",
                lowPriorityReminders.size());

        model.addAttribute(
                "reminders",
                reminders);

        model.addAttribute(
                "upcomingReminders",
                upcomingReminders);

        model.addAttribute(
                "monthlySummaryMonth",
                monthlySummaryMonth);

        model.addAttribute(
                "monthlyTotal",
                monthlyTotal);

        model.addAttribute(
                "monthlyPending",
                monthlyPending);

        model.addAttribute(
                "monthlyCompleted",
                monthlyCompleted);

        model.addAttribute(
                "monthlyHighPriority",
                monthlyHighPriority);

        return "dashboard";
    }

    /**
     * Deletes the logged in user's account.
     *
     * @param session browser session
     * @return redirect to the login page
     */
    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        userService.deleteUserAccount(
                user.getUserId());

        session.invalidate();

        return "redirect:/?accountDeleted=true";
    }
}