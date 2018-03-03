package ru.melodrom.amor.security

import grails.plugin.dropwizard.metrics.meters.Meterable
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.util.Environment
import org.apache.commons.logging.LogFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.savedrequest.SavedRequest

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareRedirectAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements Meterable {

    private static final log = LogFactory.getLog(this)

	protected RequestCache requestCache

	/** Dependency injection for the Ajax success url, e.g. '/login/ajaxSuccess'. */
	String ajaxSuccessUrl

	@Override
	void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                 Authentication authentication) throws ServletException, IOException {
        if (Environment.current.equals(Environment.PRODUCTION)) {
            try {
                markMeter 'product.action.ajax_login'
            } catch (Exception e) {
                log.error(e.getMessage(), e)
            }
        }
		boolean ajax = SpringSecurityUtils.isAjax(request)
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = ''
        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        }

		if (ajax) {
			requestCache.removeRequest request, response
        }

        try {
            if (ajax) {
                clearAuthenticationAttributes request
                String url = ajaxSuccessUrl + '?targetUrl=' + URLEncoder.encode(targetUrl, 'UTF-8')
                if (logger.debugEnabled) {
                    logger.debug 'Redirecting to Ajax Success Url: ' + url
                }
                redirectStrategy.sendRedirect request, response, url
            } else {
                super.onAuthenticationSuccess request, response, authentication
            }
        }
        finally {
            // always remove the saved request
            requestCache.removeRequest request, response
        }
	}

	@Override
	void setRequestCache(RequestCache cache) {
		super.setRequestCache cache
		requestCache = cache
	}
}
