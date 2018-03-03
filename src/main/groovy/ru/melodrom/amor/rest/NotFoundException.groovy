package ru.melodrom.amor.rest

class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
