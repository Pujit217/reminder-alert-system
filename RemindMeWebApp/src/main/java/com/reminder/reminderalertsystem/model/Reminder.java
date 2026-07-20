package com.reminder.reminderalertsystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a reminder created by a user.
 */
@Entity
public class Reminder {

    /**
     * Unique ID of the reminder.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    /**
     * ID of the user who owns this reminder.
     */
    private Long userId;

    /**
     * Reminder title.
     */
    private String title;

    /**
     * Reminder description.
     */
    private String description;

    /**
     * Date and time of the reminder.
     */
    private LocalDateTime reminderTime;

    /**
     * Priority level of the reminder.
     */
    @Enumerated(EnumType.STRING)
    private Priority priority;

    /**
     * Current status of the reminder.
     */
    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    /**
     * Default constructor.
     */
    public Reminder() {

    }

    /**
     * Creates a reminder with the given information.
     *
     * @param userId ID of the user who owns the reminder
     * @param title reminder title
     * @param description reminder description
     * @param reminderTime reminder date and time
     * @param priority reminder priority
     * @param status reminder status
     */
    public Reminder(Long userId, String title, String description,
            LocalDateTime reminderTime, Priority priority, ReminderStatus status) {

        this.userId = userId;
        this.title = title;
        this.description = description;
        this.reminderTime = reminderTime;
        this.priority = priority;
        this.status = status;
    }

    /**
     * Returns the reminder ID.
     *
     * @return reminder ID
     */
    public Long getReminderId() {
        return reminderId;
    }

    /**
     * Sets the reminder ID.
     *
     * @param reminderId reminder ID
     */
    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
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
     * Returns the reminder title.
     *
     * @return reminder title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the reminder title.
     *
     * @param title reminder title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the reminder description.
     *
     * @return reminder description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the reminder description.
     *
     * @param description reminder description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the reminder date and time.
     *
     * @return reminder date and time
     */
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    /**
     * Sets the reminder date and time.
     *
     * @param reminderTime reminder date and time
     */
    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    /**
     * Returns the priority level.
     *
     * @return priority level
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority level.
     *
     * @param priority priority level
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Returns the reminder status.
     *
     * @return reminder status
     */
    public ReminderStatus getStatus() {
        return status;
    }

    /**
     * Sets the reminder status.
     *
     * @param status reminder status
     */
    public void setStatus(ReminderStatus status) {
        this.status = status;
    }
    
    /**
     * Checks if the reminder is overdue.
     *
     * @return true if reminder time passed and reminder is still pending
     */
    public boolean isOverdue() {

        if (reminderTime == null || status == null) {
            return false;
        }

        return reminderTime.isBefore(LocalDateTime.now())
                && status == ReminderStatus.PENDING;
    }
    
    /**
     * Returns the display state of the reminder.
     *
     * @return reminder state as text
     */
    public String getReminderState() {

        if (status == ReminderStatus.COMPLETED) {
            return "COMPLETED";
        }

        if (reminderTime != null && reminderTime.isBefore(LocalDateTime.now())) {
            return "OVERDUE";
        }

        return "UPCOMING";
    }
}