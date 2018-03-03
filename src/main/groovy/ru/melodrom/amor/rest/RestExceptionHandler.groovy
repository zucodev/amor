package ru.melodrom.amor.rest

trait RestExceptionHandler {
    def handleValidationException(ValidationException e){
        render(status: 400, text: e.errors?.join("\n"))
    }

    def handleNotFoundException(NotFoundException e){
        render(status: 404, text: e.getMessage())
    }

    def handleOtherException(Exception e){
        log.error(e.getMessage(), e)
        render(status: 500, text: e.getMessage())
    }
}
