package ru.melodrom.amor.web.mapping

import grails.validation.ConstrainedProperty
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappingData
import grails.web.mapping.UrlMappingInfo
import org.grails.web.mapping.DefaultUrlMappingData

class AmorUrlMapping implements UrlMapping {

    @Override
    UrlMappingInfo match(String uri) {
        if (uri.endsWith('/'))
            uri = uri.substring(0, uri.length() - 1)

        uri = uri.toLowerCase()

        return null
    }

    @Override
    UrlMappingData getUrlData() {
        new DefaultUrlMappingData('/(**)')
    }

    @Override
    ConstrainedProperty[] getConstraints() {
        return new ConstrainedProperty[0]
    }

    @Override
    Object getControllerName() {
        return null
    }

    @Override
    Object getActionName() {
        return null
    }

    @Override
    Object getPluginName() {
        return null
    }

    @Override
    Object getNamespace() {
        return null
    }

    @Override
    Object getViewName() {
        return null
    }

    @Override
    String getHttpMethod() {
        return null
    }

    @Override
    String getVersion() {
        return null
    }

    @Override
    void setParameterValues(Map parameterValues) {

    }

    @Override
    void setParseRequest(boolean shouldParse) {

    }

    @Override
    String getMappingName() {
        return null
    }

    @Override
    void setMappingName(String name) {

    }

    @Override
    boolean hasRuntimeVariable(String name) {
        return false
    }

    @Override
    Object getRedirectInfo() {
        return null
    }

    @Override
    int compareTo(Object o) {
        return 0
    }

    @Override
    String createURL(Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createURL(Map parameterValues, String encoding, String fragment) {
        return null
    }

    @Override
    String createURL(String controller, String action, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createURL(String controller, String action, String pluginName, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createURL(String controller, String action, String namespace, String pluginName, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createRelativeURL(String controller, String action, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createRelativeURL(String controller, String action, String pluginName, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createRelativeURL(String controller, String action, String namespace, String pluginName, Map parameterValues, String encoding) {
        return null
    }

    @Override
    String createRelativeURL(String controller, String action, Map parameterValues, String encoding, String fragment) {
        return null
    }

    @Override
    String createRelativeURL(String controller, String action, String namespace, String pluginName, Map parameterValues, String encoding, String fragment) {
        return null
    }

    @Override
    String createURL(String controller, String action, Map parameterValues, String encoding, String fragment) {
        return null
    }

    @Override
    String createURL(String controller, String action, String namespace, String pluginName, Map parameterValues, String encoding, String fragment) {
        return null
    }

}
