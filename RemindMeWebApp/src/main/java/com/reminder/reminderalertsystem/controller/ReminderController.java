package com.reminder.reminderalertsystem.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reminder.reminderalertsystem.model.Priority;
import com.reminder.reminderalertsystem.model.Reminder;
import com.reminder.reminderalertsystem.model.ReminderStatus;
import com.reminder.reminderalertsystem.model.User;
import com.reminder.reminderalertsystem.service.ReminderService;

import jakarta.servlet.http.HttpSession;

/**
 * Handles reminder pages and reminder actions.
 */
@Controller
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping("/reminders")
    public String showReminders(
            HttpSession session,
            Model model,
            String keyword,
            String statusFilter,
            String priorityFilter,
            String sortBy) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        if (statusFilter == null) {
            statusFilter = "ALL";
        }

        if (priorityFilter == null) {
            priorityFilter = "ALL";
        }

        if (sortBy == null) {
            sortBy = "TIME";
        }

        List<Reminder> reminders =
                reminderService.searchFilterAndSortReminders(
                        user.getUserId(),
                        keyword,
                        statusFilter,
                        priorityFilter,
                        sortBy);

        model.addAttribute("reminders", reminders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("priorityFilter", priorityFilter);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("user", user);

        return "reminders";
    }

    @GetMapping("/reminders/add")
    public String showAddReminderPage(
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("reminder", new Reminder());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("user", user);

        return "add-reminder";
    }

    @PostMapping("/reminders/add")
    public String addReminder(
            Reminder reminder,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        reminder.setReminderId(null);
        reminder.setUserId(user.getUserId());
        reminderService.saveReminder(reminder);

        return "redirect:/reminders";
    }

    @GetMapping("/reminders/edit/{reminderId}")
    public String showEditReminderPage(
            @PathVariable Long reminderId,
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        Reminder reminder =
                reminderService.getReminderByIdForUser(
                        reminderId,
                        user.getUserId());

        if (reminder == null) {
            return "redirect:/reminders";
        }

        model.addAttribute("reminder", reminder);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("user", user);

        return "edit-reminder";
    }

    @PostMapping("/reminders/update")
    public String updateReminder(
            Reminder reminder,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        Reminder existingReminder =
                reminderService.getReminderByIdForUser(
                        reminder.getReminderId(),
                        user.getUserId());

        if (existingReminder == null) {
            return "redirect:/reminders";
        }

        reminder.setUserId(user.getUserId());
        reminderService.saveReminder(reminder);

        return "redirect:/reminders";
    }

    @GetMapping("/reminders/complete/{reminderId}")
    public String completeReminder(
            @PathVariable Long reminderId,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        reminderService.completeReminder(
                reminderId,
                user.getUserId());

        return "redirect:/reminders";
    }

    @GetMapping("/reminders/delete/{reminderId}")
    public String deleteReminder(
            @PathVariable Long reminderId,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/";
        }

        reminderService.deleteReminder(
                reminderId,
                user.getUserId());

        return "redirect:/reminders";
    }

    @GetMapping("/api/reminders/notifications")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>>
            getNotificationReminders(HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }

        List<Reminder> reminders =
                reminderService
                        .getPendingRemindersForNotifications(
                                user.getUserId());

        List<Map<String, Object>> notificationData =
                new ArrayList<>();

        for (Reminder reminder : reminders) {

            if (reminder.getReminderTime() == null) {
                continue;
            }

            Map<String, Object> reminderData =
                    new LinkedHashMap<>();

            reminderData.put(
                    "reminderId",
                    reminder.getReminderId());

            reminderData.put(
                    "title",
                    reminder.getTitle());

            reminderData.put(
                    "description",
                    reminder.getDescription());

            reminderData.put(
                    "reminderTime",
                    reminder.getReminderTime());

            reminderData.put(
                    "priority",
                    reminder.getPriority());

            notificationData.add(reminderData);
        }

        return ResponseEntity.ok(notificationData);
    }

    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }
}