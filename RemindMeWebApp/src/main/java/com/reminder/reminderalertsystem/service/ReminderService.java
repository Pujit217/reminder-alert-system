package com.reminder.reminderalertsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.reminder.reminderalertsystem.model.Priority;
import com.reminder.reminderalertsystem.model.Reminder;
import com.reminder.reminderalertsystem.model.ReminderStatus;
import com.reminder.reminderalertsystem.repository.ReminderRepository;

/**
 * Handles the main logic for reminders.
 */
@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder saveReminder(Reminder reminder) {

        if (reminder.getStatus() == null) {
            reminder.setStatus(ReminderStatus.PENDING);
        }

        return reminderRepository.save(reminder);
    }

    public List<Reminder> getRemindersByUser(Long userId) {
        return reminderRepository.findByUserId(userId);
    }

    public List<Reminder> getPendingRemindersByUser(Long userId) {
        return reminderRepository.findByUserIdAndStatus(
                userId, ReminderStatus.PENDING);
    }

    public List<Reminder> getPendingRemindersForNotifications(Long userId) {
        return reminderRepository
                .findByUserIdAndStatusOrderByReminderTimeAsc(
                        userId, ReminderStatus.PENDING);
    }

    public Reminder getReminderById(Long reminderId) {
        return reminderRepository.findById(reminderId).orElse(null);
    }

    public Reminder getReminderByIdForUser(
            Long reminderId, Long userId) {

        return reminderRepository
                .findByReminderIdAndUserId(reminderId, userId)
                .orElse(null);
    }

    public boolean completeReminder(
            Long reminderId, Long userId) {

        Reminder reminder =
                getReminderByIdForUser(reminderId, userId);

        if (reminder == null) {
            return false;
        }

        reminder.setStatus(ReminderStatus.COMPLETED);
        reminderRepository.save(reminder);

        return true;
    }

    public boolean deleteReminder(
            Long reminderId, Long userId) {

        Reminder reminder =
                getReminderByIdForUser(reminderId, userId);

        if (reminder == null) {
            return false;
        }

        reminderRepository.delete(reminder);
        return true;
    }

    public List<Reminder> getCompletedRemindersByUser(Long userId) {
        return reminderRepository.findByUserIdAndStatus(
                userId, ReminderStatus.COMPLETED);
    }

    public List<Reminder> getHighPriorityReminders(Long userId) {
        return reminderRepository.findByUserIdAndPriority(
                userId, Priority.HIGH);
    }

    public List<Reminder> getMediumPriorityReminders(Long userId) {
        return reminderRepository.findByUserIdAndPriority(
                userId, Priority.MEDIUM);
    }

    public List<Reminder> getLowPriorityReminders(Long userId) {
        return reminderRepository.findByUserIdAndPriority(
                userId, Priority.LOW);
    }

    public List<Reminder> searchFilterAndSortReminders(
            Long userId,
            String keyword,
            String statusFilter,
            String priorityFilter,
            String sortBy) {

        List<Reminder> reminders =
                getRemindersByUser(userId);

        List<Reminder> result = new ArrayList<>();

        String cleanedKeyword =
                keyword == null
                        ? ""
                        : keyword.trim().toLowerCase();

        for (Reminder reminder : reminders) {

            boolean matchesKeyword =
                    matchesKeyword(reminder, cleanedKeyword);

            boolean matchesStatus =
                    matchesStatus(reminder, statusFilter);

            boolean matchesPriority =
                    matchesPriority(reminder, priorityFilter);

            if (matchesKeyword
                    && matchesStatus
                    && matchesPriority) {

                result.add(reminder);
            }
        }

        sortReminders(result, sortBy);
        return result;
    }

    private boolean matchesKeyword(
            Reminder reminder,
            String keyword) {

        if (keyword.isEmpty()) {
            return true;
        }

        String title =
                reminder.getTitle() == null
                        ? ""
                        : reminder.getTitle().toLowerCase();

        String description =
                reminder.getDescription() == null
                        ? ""
                        : reminder.getDescription().toLowerCase();

        return title.contains(keyword)
                || description.contains(keyword);
    }

    private boolean matchesStatus(
            Reminder reminder,
            String statusFilter) {

        if (statusFilter == null
                || statusFilter.equals("ALL")) {
            return true;
        }

        return reminder.getStatus() != null
                && reminder.getStatus()
                        .name()
                        .equals(statusFilter);
    }

    private boolean matchesPriority(
            Reminder reminder,
            String priorityFilter) {

        if (priorityFilter == null
                || priorityFilter.equals("ALL")) {
            return true;
        }

        return reminder.getPriority() != null
                && reminder.getPriority()
                        .name()
                        .equals(priorityFilter);
    }

    private void sortReminders(
            List<Reminder> reminders,
            String sortBy) {

        if ("TITLE".equals(sortBy)) {

            reminders.sort(
                    Comparator.comparing(
                            Reminder::getTitle,
                            Comparator.nullsLast(
                                    String.CASE_INSENSITIVE_ORDER)));

        } else if ("PRIORITY".equals(sortBy)) {

            reminders.sort(
                    Comparator.comparing(
                            Reminder::getPriority,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder())));

        } else {

            reminders.sort(
                    Comparator.comparing(
                            Reminder::getReminderTime,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder())));
        }
    }

    public List<Reminder> getUpcomingRemindersByUser(Long userId) {

        List<Reminder> reminders =
                getPendingRemindersForNotifications(userId);

        List<Reminder> upcomingReminders =
                new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Reminder reminder : reminders) {

            if (reminder.getReminderTime() != null
                    && reminder.getReminderTime().isAfter(now)) {

                upcomingReminders.add(reminder);
            }
        }

        if (upcomingReminders.size() > 5) {
            return new ArrayList<>(
                    upcomingReminders.subList(0, 5));
        }

        return upcomingReminders;
    }
}