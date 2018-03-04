package ru.melodrom.amor.web

import grails.util.Environment

class UrlMappings {

    static mappings = {
        // AUTH
        '/api/auth/ajaxLogin'(controller: 'auth', action: 'ajaxLogin')
        '/api/auth/ajaxLogout'(controller: 'auth', action: 'ajaxLogout')
        '/api/auth/check'(controller: 'auth', action: 'check')
        '/api/auth/register'(controller: 'auth', action: 'register')

        // SECURITY HELPER
        '/api/session'(controller: 'session', action: 'info')

        '/api/user/exists'(controller: 'user', action: 'exists')
        '/api/user/password/restore'(controller: 'user', action: 'restorePassword')

        // DEV
        if (Environment.current != Environment.PRODUCTION) {
            //
        }


    }
}
