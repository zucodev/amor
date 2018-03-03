package ru.melodrom.amor.utils

import groovy.transform.CompileStatic

import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter

import static java.util.Calendar.MONTH
import static java.util.Calendar.YEAR

@CompileStatic
class DateHelper {

    static final String toJsFormat(LocalDateTime dateTime) {
        assert dateTime
        return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.of(dateTime, ZoneOffset.UTC))
    }

    // ISO_8601
    static final String toJsFormat(LocalDate date) {
        assert date
        return toJsFormat(date.atStartOfDay())
    }

    static final Date toDate(LocalDate date) {
        assert date
        return toDate(date.atStartOfDay())
    }

    static final Timestamp toTimestamp(LocalDate date) {
        assert date
        Timestamp.valueOf(date.atStartOfDay())
    }

    static final Date toDate(LocalDateTime dateTime) {
        assert dateTime
        return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant())
    }

    static Calendar toCalendar(Date date) {
        assert date
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(date)
        return calendar
    }

    static final String format(LocalDate date) {
        assert date
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date)
    }

    static final LocalDate toLocalDate(Date date) {
        assert date
        return date.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
    }

    static final LocalDate toLocalDateOrNull(Date date) {
        return date ? toLocalDate(date) : null
    }

    static final LocalDateTime toLocalDateTime(Date date) {
        assert date
        return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
    }

    static final LocalDateTime toLocalDateTimeOrNull(Date date) {
        return date ? toLocalDateTime(date) : null
    }

    static final Long toUnixTimestamp(LocalDateTime dateTime) {
        assert dateTime
        dateTime.toInstant(ZoneOffset.UTC).getEpochSecond()
    }

    static final LocalDateTime toUTC(ZonedDateTime dateTime) {
        assert dateTime

        dateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
    }

    static final boolean isInRange(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        if (date && start && end) {
            date.isAfter(start) && date.isBefore(end)
        } else {
            false
        }
    }

    /**
     * Can accept nullable dates
     */
    static final boolean isSameDay(LocalDate date1, LocalDate date2) {
        return (date1 == date2)
    }

    static Integer monthsDiff(Date dateFrom, Date dateTo) {
        int diffYears = (dateTo[YEAR] - dateFrom[YEAR]) * 12
        int diffMonths = dateTo[MONTH] - dateFrom[MONTH]
        return diffYears + diffMonths
    }

    static Boolean sameMonth(Date date1, Date date2) {
        date1[MONTH] == date2[MONTH] && date1[YEAR] == date2[YEAR]
    }

    static Date now() {
        new Date()
    }
}
