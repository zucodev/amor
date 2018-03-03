package ru.melodrom.amor.helpers

class DateHelper {

    static void setDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR, 10)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
}
