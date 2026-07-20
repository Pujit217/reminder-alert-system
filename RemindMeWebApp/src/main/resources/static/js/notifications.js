/**
 * Opens and closes the application sidebar.
 */
function toggleSidebar() {

    const sidebar = document.getElementById("sidebar");
    const layout = document.querySelector(".app-layout");

    if (sidebar == null || layout == null) {
        return;
    }

    sidebar.classList.toggle("collapsed");
    layout.classList.toggle("sidebar-collapsed");

    const isCollapsed =
        sidebar.classList.contains("collapsed");

    localStorage.setItem(
        "sidebarCollapsed",
        isCollapsed
    );
}

/**
 * Opens or closes the settings panel.
 */
function toggleSettingsPanel() {

    const settingsPanel =
        document.getElementById("settingsPanel");

    if (settingsPanel == null) {
        return;
    }

    if (settingsPanel.classList.contains("open")) {
        closeSettingsPanel();
    } else {
        openSettingsPanel();
    }
}

/**
 * Opens the settings panel.
 */
function openSettingsPanel() {

    const settingsPanel =
        document.getElementById("settingsPanel");

    const settingsOverlay =
        document.getElementById("settingsOverlay");

    if (settingsPanel == null
            || settingsOverlay == null) {
        return;
    }

    settingsPanel.classList.add("open");
    settingsOverlay.classList.add("visible");

    settingsPanel.setAttribute(
        "aria-hidden",
        "false"
    );

    document.body.classList.add(
        "settings-open"
    );
}

/**
 * Closes the settings panel.
 */
function closeSettingsPanel() {

    const settingsPanel =
        document.getElementById("settingsPanel");

    const settingsOverlay =
        document.getElementById("settingsOverlay");

    if (settingsPanel == null
            || settingsOverlay == null) {
        return;
    }

    settingsPanel.classList.remove("open");
    settingsOverlay.classList.remove("visible");

    settingsPanel.setAttribute(
        "aria-hidden",
        "true"
    );

    document.body.classList.remove(
        "settings-open"
    );
}

/**
 * Shows or hides a password field.
 *
 * @param {string} inputId ID of the password input
 * @param {HTMLElement} button button that was clicked
 */
function togglePassword(inputId, button) {

    const passwordInput =
        document.getElementById(inputId);

    const eyeIcon =
        button.querySelector(".eye-icon");

    if (passwordInput == null || eyeIcon == null) {
        return;
    }

    if (passwordInput.type === "password") {

        passwordInput.type = "text";
        eyeIcon.textContent = "⊘";

        button.setAttribute(
            "aria-label",
            "Hide password"
        );

        button.setAttribute(
            "title",
            "Hide password"
        );

    } else {

        passwordInput.type = "password";
        eyeIcon.textContent = "◉";

        button.setAttribute(
            "aria-label",
            "Show password"
        );

        button.setAttribute(
            "title",
            "Show password"
        );
    }
}

/* =========================================================
   Calendar Variables
   ========================================================= */

let displayedCalendarDate = new Date();
let calendarReminders = [];

/**
 * Loads reminder information from hidden dashboard elements.
 */
function loadCalendarReminders() {

    const reminderRecords =
        document.querySelectorAll(
            ".calendar-reminder-record"
        );

    calendarReminders = [];

    for (const record of reminderRecords) {

        const reminderTime =
            record.dataset.time;

        if (reminderTime == null
                || reminderTime === "") {
            continue;
        }

        calendarReminders.push({
            title: record.dataset.title || "",
            description: record.dataset.description || "",
            time: reminderTime,
            priority: record.dataset.priority || "",
            status: record.dataset.status || ""
        });
    }
}

/**
 * Changes the displayed calendar month.
 *
 * @param {number} amount number of months to move
 */
function changeCalendarMonth(amount) {

    displayedCalendarDate.setMonth(
        displayedCalendarDate.getMonth() + amount
    );

    renderCalendar();
}

/**
 * Draws the compact dashboard calendar.
 */
function renderCalendar() {

    const calendarDays =
        document.getElementById("calendarDays");

    const calendarTitle =
        document.getElementById(
            "calendarMonthTitle"
        );

    if (calendarDays == null
            || calendarTitle == null) {
        return;
    }

    calendarDays.innerHTML = "";

    const year =
        displayedCalendarDate.getFullYear();

    const month =
        displayedCalendarDate.getMonth();

    calendarTitle.textContent =
        displayedCalendarDate.toLocaleString(
            "en-US",
            {
                month: "long",
                year: "numeric"
            }
        );

    const firstDayOfMonth =
        new Date(year, month, 1).getDay();

    const numberOfDays =
        new Date(
            year,
            month + 1,
            0
        ).getDate();

    for (let index = 0;
            index < firstDayOfMonth;
            index++) {

        const emptyCell =
            document.createElement("div");

        emptyCell.className =
            "calendar-day empty-day";

        calendarDays.appendChild(emptyCell);
    }

    for (let day = 1;
            day <= numberOfDays;
            day++) {

        const dayButton =
            document.createElement("button");

        dayButton.type = "button";
        dayButton.className = "calendar-day";
        dayButton.textContent = day;

        const currentDate =
            new Date(year, month, day);

        if (isToday(currentDate)) {
            dayButton.classList.add("today");
        }

        const remindersForDate =
            getRemindersForDate(currentDate);

        if (remindersForDate.length > 0) {

            dayButton.classList.add(
                "has-reminder"
            );

            const reminderDot =
                document.createElement("span");

            reminderDot.className =
                "reminder-dot";

            dayButton.appendChild(
                reminderDot
            );
        }

        dayButton.addEventListener(
            "click",
            function() {

                selectCalendarDay(
                    currentDate,
                    dayButton
                );
            }
        );

        calendarDays.appendChild(dayButton);
    }
}

/**
 * Checks whether the given date is today.
 *
 * @param {Date} date date to check
 * @return {boolean} true when the date is today
 */
function isToday(date) {

    const today = new Date();

    return date.getFullYear()
            === today.getFullYear()
        && date.getMonth()
            === today.getMonth()
        && date.getDate()
            === today.getDate();
}

/**
 * Returns reminders scheduled for a specific date.
 *
 * @param {Date} selectedDate selected calendar date
 * @return {Array} reminders for selected date
 */
function getRemindersForDate(selectedDate) {

    const matchingReminders = [];

    for (const reminder of calendarReminders) {

        const reminderDate =
            new Date(reminder.time);

        if (Number.isNaN(
                reminderDate.getTime())) {
            continue;
        }

        if (reminderDate.getFullYear()
                === selectedDate.getFullYear()
                && reminderDate.getMonth()
                === selectedDate.getMonth()
                && reminderDate.getDate()
                === selectedDate.getDate()) {

            matchingReminders.push(reminder);
        }
    }

    return matchingReminders;
}

/**
 * Displays reminders for selected calendar date.
 *
 * @param {Date} date selected date
 * @param {HTMLElement} selectedButton selected day button
 */
function selectCalendarDay(
        date,
        selectedButton) {

    const allCalendarDays =
        document.querySelectorAll(
            ".calendar-day"
        );

    for (const calendarDay
            of allCalendarDays) {

        calendarDay.classList.remove(
            "selected"
        );
    }

    selectedButton.classList.add(
        "selected"
    );

    const dateTitle =
        document.getElementById(
            "selectedDateTitle"
        );

    const reminderList =
        document.getElementById(
            "selectedDateReminderList"
        );

    if (dateTitle == null
            || reminderList == null) {
        return;
    }

    dateTitle.textContent =
        date.toLocaleDateString(
            "en-US",
            {
                weekday: "long",
                month: "long",
                day: "numeric",
                year: "numeric"
            }
        );

    reminderList.innerHTML = "";

    const matchingReminders =
        getRemindersForDate(date);

    if (matchingReminders.length === 0) {

        reminderList.innerHTML =
            "<p class=\"calendar-empty-message\">"
            + "No reminders scheduled for this date."
            + "</p>";

        return;
    }

    for (const reminder
            of matchingReminders) {

        const reminderItem =
            document.createElement("div");

        reminderItem.className =
            "selected-reminder-item";

        const reminderDate =
            new Date(reminder.time);

        const time =
            reminderDate.toLocaleTimeString(
                "en-US",
                {
                    hour: "numeric",
                    minute: "2-digit"
                }
            );

        const priorityClass =
            reminder.priority.toLowerCase();

        reminderItem.innerHTML =
            "<div>"
            + "<strong>"
            + escapeCalendarText(
                reminder.title
            )
            + "</strong>"
            + "<p>"
            + escapeCalendarText(time)
            + "</p>"
            + "</div>"
            + "<span class=\"calendar-priority "
            + escapeCalendarText(
                priorityClass
            )
            + "\">"
            + escapeCalendarText(
                reminder.priority
            )
            + "</span>";

        reminderList.appendChild(
            reminderItem
        );
    }
}

/**
 * Escapes text before inserting it into HTML.
 *
 * @param {string} text text to escape
 * @return {string} escaped text
 */
function escapeCalendarText(text) {

    if (text == null) {
        return "";
    }

    return String(text)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#039;");
}

/**
 * Switches between beige and dark themes.
 */
function toggleTheme() {

    const isDarkTheme =
        document.body.classList.contains("dark-theme");

    if (isDarkTheme) {
        applyTheme("beige");
    } else {
        applyTheme("dark");
    }
}

/**
 * Applies the selected application theme.
 *
 * @param {string} theme selected theme
 */
function applyTheme(theme) {

    const useDarkTheme = theme === "dark";

    document.body.classList.toggle(
        "dark-theme",
        useDarkTheme
    );

    localStorage.setItem(
        "applicationTheme",
        useDarkTheme ? "dark" : "beige"
    );

    updateThemeButton();
}

/**
 * Restores the theme saved by the user.
 */
function restoreSavedTheme() {

    const savedTheme =
        localStorage.getItem("applicationTheme");

    if (savedTheme === "dark") {
        document.body.classList.add("dark-theme");
    } else {
        document.body.classList.remove("dark-theme");
    }

    updateThemeButton();
}

/**
 * Updates the Settings theme button.
 */
function updateThemeButton() {

    const themeToggleButton =
        document.getElementById("themeToggleButton");

    if (themeToggleButton == null) {
        return;
    }

    const themeIcon =
        themeToggleButton.querySelector(
            ".settings-action-icon"
        );

    const themeDescription =
        document.getElementById(
            "themeButtonDescription"
        );

    const isDarkTheme =
        document.body.classList.contains("dark-theme");

    if (isDarkTheme) {

        if (themeIcon != null) {
            themeIcon.textContent = "☀";
        }

        if (themeDescription != null) {
            themeDescription.textContent =
                "Switch to beige mode";
        }

        themeToggleButton.setAttribute(
            "aria-label",
            "Switch to beige mode"
        );

        themeToggleButton.setAttribute(
            "title",
            "Switch to beige mode"
        );

    } else {

        if (themeIcon != null) {
            themeIcon.textContent = "☾";
        }

        if (themeDescription != null) {
            themeDescription.textContent =
                "Switch to dark mode";
        }

        themeToggleButton.setAttribute(
            "aria-label",
            "Switch to dark mode"
        );

        themeToggleButton.setAttribute(
            "title",
            "Switch to dark mode"
        );
    }
}

/* =========================================================
   Browser Reminder Notifications
   ========================================================= */

const NOTIFICATION_CHECK_INTERVAL = 15000;
const NOTIFICATION_GRACE_PERIOD = 5 * 60 * 1000;
const NOTIFICATION_TOAST_DURATION = 60000;
const NOTIFIED_REMINDERS_KEY = "notifiedReminderKeys";

   let notificationAudioContext = null;
let notificationCheckTimer = null;

/**
 * Starts browser notifications.
 */
function initializeBrowserNotifications() {

    if (!("Notification" in window)) {
        return;
    }

    if (Notification.permission === "granted") {
        startNotificationChecks();
        return;
    }

    if (Notification.permission === "default") {

        document.addEventListener(
            "click",
            requestNotificationPermission,
            { once: true }
        );
    }
}

/**
 * Requests browser notification permission.
 */
async function requestNotificationPermission() {

    try {

        const permission =
            await Notification.requestPermission();

        if (permission === "granted") {
            startNotificationChecks();
        }

    } catch (error) {
        console.error(error);
    }
}

/**
 * Starts checking reminders.
 */
function startNotificationChecks() {

    if (notificationCheckTimer != null) {
        return;
    }

    checkForDueReminders();

    notificationCheckTimer =
        window.setInterval(
            checkForDueReminders,
            NOTIFICATION_CHECK_INTERVAL
        );
}

/**
 * Checks reminders from the server.
 */
async function checkForDueReminders() {

    if (Notification.permission !== "granted") {
        return;
    }

    try {

        const response =
            await fetch("/api/reminders/notifications");

        if (!response.ok) {
            return;
        }

        const reminders =
            await response.json();

        const now = Date.now();

        for (const reminder of reminders) {

            const reminderTime =
                new Date(reminder.reminderTime);

            const difference =
                now - reminderTime.getTime();

            if (difference < 0
                    || difference > NOTIFICATION_GRACE_PERIOD) {
                continue;
            }

            const key =
                reminder.reminderId
                + "-"
                + reminder.reminderTime;

            if (alreadyNotified(key)) {
                continue;
            }

			showBrowserNotification(reminder);

			showReminderToast(reminder);

			saveNotification(key);
        }

    } catch (error) {
        console.error(error);
    }
}

/**
 * Plays one short notification ding.
 */
function playNotificationDing() {

    try {

        if (notificationAudioContext == null) {

            notificationAudioContext =
                new AudioContext();
        }

        if (notificationAudioContext.state
                === "suspended") {

            notificationAudioContext.resume();
        }

        const oscillator =
            notificationAudioContext
                .createOscillator();

        const volume =
            notificationAudioContext
                .createGain();

        const currentTime =
            notificationAudioContext
                .currentTime;

        oscillator.type = "sine";

        oscillator.frequency.setValueAtTime(
            880,
            currentTime
        );

        oscillator.frequency.exponentialRampToValueAtTime(
            660,
            currentTime + 0.35
        );

        volume.gain.setValueAtTime(
            0.0001,
            currentTime
        );

        volume.gain.exponentialRampToValueAtTime(
            0.22,
            currentTime + 0.02
        );

        volume.gain.exponentialRampToValueAtTime(
            0.0001,
            currentTime + 0.45
        );

        oscillator.connect(volume);

        volume.connect(
            notificationAudioContext.destination
        );

        oscillator.start(currentTime);

        oscillator.stop(
            currentTime + 0.45
        );

    } catch (error) {
        console.error(
            "Could not play notification sound.",
            error
        );
    }
}

/**
 * Shows an in-app reminder toast.
 *
 * @param {Object} reminder reminder information
 */
function showReminderToast(reminder) {

    const container =
        document.getElementById(
            "notificationContainer"
        );

    if (container == null) {
        return;
    }

    const priority =
        String(reminder.priority || "LOW")
            .toLowerCase();

    const toast =
        document.createElement("div");

    toast.className =
        "reminder-toast";

    toast.innerHTML =

        "<div class=\"reminder-toast-header\">"

        + "<span class=\"reminder-toast-title\">"
        + "⏰ Reminder Due"
        + "</span>"

        + "<button type=\"button\""
        + " class=\"reminder-toast-close\""
        + " aria-label=\"Close notification\">"
        + "×"
        + "</button>"

        + "</div>"

        + "<div class=\"reminder-toast-body\">"

        + "<h4>"
        + escapeCalendarText(
            reminder.title
            || "Reminder"
        )
        + "</h4>"

        + "<p>"
        + escapeCalendarText(
            reminder.description
            || "Reminder is due now!"
        )
        + "</p>"

        + "<div class=\"reminder-toast-footer\">"

        + "<span class=\"notification-priority "
        + escapeCalendarText(priority)
        + "\">"
        + escapeCalendarText(
            reminder.priority
            || "LOW"
        )
        + " PRIORITY"
        + "</span>"

        + "<a href=\"/reminders\""
        + " class=\"notification-view-button\">"
        + "View Reminder"
        + "</a>"

        + "</div>"

        + "</div>"

        + "<div class=\"reminder-toast-progress\">"

        + "<div class="
        + "\"reminder-toast-progress-bar\">"
        + "</div>"

        + "</div>";

    const closeButton =
        toast.querySelector(
            ".reminder-toast-close"
        );

    const progressBar =
        toast.querySelector(
            ".reminder-toast-progress-bar"
        );
		
	const viewReminderButton =
		toast.querySelector(
		    ".notification-view-button"
		);

    let toastRemovalTimer = null;

    function removeToast() {

        if (toastRemovalTimer != null) {

            clearTimeout(
                toastRemovalTimer
            );

            toastRemovalTimer = null;
        }

        toast.classList.add(
            "removing"
        );

        window.setTimeout(
            function() {
                toast.remove();
            },
            300
        );
    }

    if (closeButton != null) {

        closeButton.addEventListener(
            "click",
            removeToast
        );
    }
	if (viewReminderButton != null) {

	    viewReminderButton.addEventListener(
	        "click",
	        function(event) {

	            event.preventDefault();

	            if (toastRemovalTimer != null) {

	                clearTimeout(
	                    toastRemovalTimer
	                );

	                toastRemovalTimer = null;
	            }

	            toast.classList.add(
	                "opening-reminder"
	            );

	            window.setTimeout(
	                function() {

	                    window.location.href =
	                        "/reminders";
	                },
	                280
	            );
	        }
	    );
	}

    container.appendChild(toast);

    playNotificationDing();

    window.requestAnimationFrame(
        function() {

            window.requestAnimationFrame(
                function() {

                    if (progressBar != null) {

                        progressBar.classList.add(
                            "running"
                        );
                    }
                }
            );
        }
    );

    toastRemovalTimer =
        window.setTimeout(
            removeToast,
            NOTIFICATION_TOAST_DURATION
        );
}

/**
 * Shows browser notification.
 */
function showBrowserNotification(reminder) {

    const notification =
        new Notification(
            reminder.title,
            {
                body:
                    reminder.description
                    || "Reminder is due now!"
            }
        );

    notification.onclick =
        function() {

            window.focus();
            window.location.href =
                "/reminders";
        };
}

/**
 * Checks if notification already shown.
 */
function alreadyNotified(key) {

    return getSavedNotifications()
        .includes(key);
}

/**
 * Saves shown notification.
 */
function saveNotification(key) {

    const notifications =
        getSavedNotifications();

    notifications.push(key);

    localStorage.setItem(
        NOTIFIED_REMINDERS_KEY,
        JSON.stringify(notifications)
    );
}

/**
 * Returns saved notifications.
 */
function getSavedNotifications() {

    const saved =
        localStorage.getItem(
            NOTIFIED_REMINDERS_KEY
        );

    if (saved == null) {
        return [];
    }

    return JSON.parse(saved);
}

/**
 * Restores saved sidebar state and initializes calendar.
 */
document.addEventListener(
    "DOMContentLoaded",
    function() {

        restoreSavedTheme();

        const sidebar =
            document.getElementById("sidebar");

        const layout =
            document.querySelector(".app-layout");

        if (sidebar != null
                && layout != null) {

            const isCollapsed =
                localStorage.getItem(
                    "sidebarCollapsed"
                ) === "true";

            if (isCollapsed) {

                sidebar.classList.add(
                    "collapsed"
                );

                layout.classList.add(
                    "sidebar-collapsed"
                );
            }
        }

		loadCalendarReminders();
		renderCalendar();

		initializeBrowserNotifications();

		document.addEventListener(
		    "click",
		    function initializeNotificationAudio() {

		        if (notificationAudioContext == null) {

		            notificationAudioContext =
		                new AudioContext();
		        }

		        if (notificationAudioContext.state
		                === "suspended") {

		            notificationAudioContext.resume();
		        }

		    },
		    { once: true }
		);

		const themeToggleButton =
		    document.getElementById(
		        "themeToggleButton"
		    );

        if (themeToggleButton != null) {

            themeToggleButton.addEventListener(
                "click",
                function() {
                    toggleTheme();
                }
            );
        }
    }
);

/**
 * Closes open panels when Escape is pressed.
 */
document.addEventListener(
    "keydown",
    function(event) {

        if (event.key === "Escape") {
            closeSettingsPanel();
            closeDeleteAccountModal();
        }
    }
);

/**
 * Opens the delete-account confirmation modal.
 */
function openDeleteAccountModal() {

    const deleteModal =
        document.getElementById(
            "deleteAccountModal"
        );

    const deleteOverlay =
        document.getElementById(
            "deleteModalOverlay"
        );

    if (deleteModal == null
            || deleteOverlay == null) {
        return;
    }

    closeSettingsPanel();

    deleteModal.classList.add("open");
    deleteOverlay.classList.add("visible");

    deleteModal.setAttribute(
        "aria-hidden",
        "false"
    );

    document.body.classList.add(
        "delete-modal-open"
    );
}

/**
 * Closes the delete-account confirmation modal.
 */
function closeDeleteAccountModal() {

    const deleteModal =
        document.getElementById(
            "deleteAccountModal"
        );

    const deleteOverlay =
        document.getElementById(
            "deleteModalOverlay"
        );

    if (deleteModal == null
            || deleteOverlay == null) {
        return;
    }

    deleteModal.classList.remove("open");
    deleteOverlay.classList.remove("visible");

    deleteModal.setAttribute(
        "aria-hidden",
        "true"
    );

    document.body.classList.remove(
        "delete-modal-open"
    );
}