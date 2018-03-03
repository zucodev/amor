package ru.melodrom.amor.security

import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.savedrequest.RequestCache

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxSuccessAwareRedirectStrategy  extends DefaultRedirectStrategy {

    private RequestCache requestCache
    private String _ajaxSuccessUrl

    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (SpringSecurityUtils.isAjax(request)) {
            super.sendRedirect(request, response, url)
        } else {
            super.sendRedirect(request, response, url)
        }
    }


    public void setAjaxSuccessUrl(final String ajaxSuccessUrl) {
        _ajaxSuccessUrl = ajaxSuccessUrl
    }


}
