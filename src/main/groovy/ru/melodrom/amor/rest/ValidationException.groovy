package ru.melodrom.amor.rest

class ValidationException extends RuntimeException {
    List<String> errors

    ValidationException(String var1) {
        super(var1)
    }

    ValidationException(Collection<String> errors) {
        this.errors = errors
    }
}
