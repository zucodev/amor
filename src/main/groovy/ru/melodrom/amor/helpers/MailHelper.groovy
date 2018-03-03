package ru.melodrom.amor.helpers

import grails.plugins.mail.MailService
//import groovy.transform.CompileStatic
//import org.springframework.mail.MailMessage

class MailHelper {

    MailService mailService

    def sendNoReply(emailSubject, emailMessage, emailRecipient) {
        mailService.sendMail {
            from 'NO REPLY<noreply@melodrom.com>'
            to emailRecipient
            subject emailSubject
            html emailMessage
        }
    }


    def sendEmail(emailSubject, emailMessage, emailRecipient, adminEmail) {
        mailService.sendMail {
            from 'Amir<' + adminEmail + '>'
            to emailRecipient
            subject emailSubject
            html emailMessage
        }
    }

    def sendEmail(emailSubject, emailMessage, emailRecipient, adminEmail, replyToEmail) {
      mailService.sendMail {
        from 'Amir<' + adminEmail + '>'
        to emailRecipient
        replyTo replyToEmail
        subject emailSubject
        html emailMessage
      }
    }
}
