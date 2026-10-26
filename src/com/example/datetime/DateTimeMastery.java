package com.example.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMastery {

    public static void main(String[] args) {

        // =========================================================================
        // 1. Creating and Managing Date-Based Events (LocalDate)
        // =========================================================================
        System.out.println("--- 1. DATE-BASED EVENTS ---");
        LocalDate today = LocalDate.now();
        LocalDate projectDeadline = LocalDate.of(2026, Month.DECEMBER, 15);

        // Managing date events (manipulation & comparison)
        LocalDate reviewDate = projectDeadline.minusWeeks(2);
        boolean isBeforeDeadline = today.isBefore(projectDeadline);

        System.out.println("Today: " + today);
        System.out.println("Project Deadline: " + projectDeadline);
        System.out.println("Review Date (2 weeks prior): " + reviewDate);
        System.out.println("Is today before deadline? " + isBeforeDeadline);
        System.out.println();


        // =========================================================================
        // 2. Creating and Managing Time-Based Events (LocalTime)
        // =========================================================================
        System.out.println("--- 2. TIME-BASED EVENTS ---");
        LocalTime currentTime = LocalTime.now();
        LocalTime meetingStartTime = LocalTime.of(14, 30); // 14:30 (2:30 PM)

        // Managing time events
        LocalTime meetingEndTime = meetingStartTime.plusHours(1).plusMinutes(15);

        System.out.println("Current Time: " + currentTime);
        System.out.println("Meeting Starts: " + meetingStartTime);
        System.out.println("Meeting Ends: " + meetingEndTime);
        System.out.println();


        // =========================================================================
        // 3. Combining Date and Time into a Single Object (LocalDateTime)
        // =========================================================================
        System.out.println("--- 3. COMBINING DATE AND TIME ---");
        // Creating LocalDateTime directly or by combining LocalDate & LocalTime
        LocalDateTime conferenceDateTime = LocalDateTime.of(projectDeadline, meetingStartTime);
        LocalDateTime eventStart = LocalDateTime.now();

        System.out.println("Conference Schedule: " + conferenceDateTime);
        System.out.println("Event Started At: " + eventStart);
        System.out.println();


        // =========================================================================
        // 4. Working with Dates and Times Across Time Zones (ZonedDateTime & ZoneId)
        // =========================================================================
        System.out.println("--- 4. TIME ZONES (ZonedDateTime) ---");
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");

        // Converting LocalDateTime to a ZonedDateTime
        ZonedDateTime nyConference = conferenceDateTime.atZone(newYorkZone);

        // Converting time from one zone to another (Same moment in time)
        ZonedDateTime tokyoConference = nyConference.withZoneSameInstant(tokyoZone);

        System.out.println("NY Conference Time: " + nyConference);
        System.out.println("Tokyo Time for same event: " + tokyoConference);
        System.out.println();


        // =========================================================================
        // 5. Defining Timestamps, Periods, and Durations
        // =========================================================================
        System.out.println("--- 5. TIMESTAMPS, PERIODS, AND DURATIONS ---");

        // Timestamp (Machine-readable UTC point in time)
        Instant currentTimestamp = Instant.now();
        System.out.println("Current Instant (UTC Timestamp): " + currentTimestamp);

        // Period (Date-based amount of time: Years, Months, Days)
        LocalDate startDate = LocalDate.of(2025, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2026, Month.OCTOBER, 26);
        Period ageOrGap = Period.between(startDate, endDate);
        System.out.println("Period Difference: " + ageOrGap.getYears() + " Years, "
                + ageOrGap.getMonths() + " Months, " + ageOrGap.getDays() + " Days");

        // Duration (Time-based amount of time: Seconds, Nanoseconds)
        LocalTime startShift = LocalTime.of(9, 0);
        LocalTime endShift = LocalTime.of(17, 30);
        Duration shiftLength = Duration.between(startShift, endShift);
        System.out.println("Shift Duration: " + shiftLength.toHours() + " hours and "
                + (shiftLength.toMinutes() % 60) + " minutes");
        System.out.println();


        // =========================================================================
        // 6. Applying Formatting to Local and Zoned Dates and Times
        // =========================================================================
        System.out.println("--- 6. FORMATTING & PARSING ---");

        // Formatting Local Date & Time
        DateTimeFormatter customLocalFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedLocalDateTime = conferenceDateTime.format(customLocalFormatter);
        System.out.println("Formatted Local DateTime: " + formattedLocalDateTime);

        // Formatting Zoned Date & Time
        DateTimeFormatter customZonedFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a (z)");
        String formattedZonedDateTime = nyConference.format(customZonedFormatter);
        System.out.println("Formatted Zoned DateTime: " + formattedZonedDateTime);

        // Parsing a formatted String back to LocalDate
        String dateString = "25/12/2026";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(dateString, inputFormatter);
        System.out.println("Parsed LocalDate: " + parsedDate);
    }
}
