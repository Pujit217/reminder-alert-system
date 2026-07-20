package com.reminder.reminderalertsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reminder.reminderalertsystem.model.Priority;
import com.reminder.reminderalertsystem.model.Reminder;
import com.reminder.reminderalertsystem.model.ReminderStatus;

/**
 * Repository for performing database operations on reminders.
 */
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserId(Long userId);

    List<Reminder> findByUserIdAndTitleContainingIgnoreCase(
            Long userId, String title);

    List<Reminder> findByUserIdAndDescriptionContainingIgnoreCase(
            Long userId, String description);

    List<Reminder> findByUserIdAndStatus(
            Long userId, ReminderStatus status);

    List<Reminder> findByUserIdAndPriority(
            Long userId, Priority priority);

    List<Reminder> findByUserIdAndStatusOrderByReminderTimeAsc(
            Long userId, ReminderStatus status);

    Optional<Reminder> findByReminderIdAndUserId(
            Long reminderId, Long userId);
}