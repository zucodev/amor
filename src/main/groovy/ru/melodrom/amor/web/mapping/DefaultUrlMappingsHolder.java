package ru.melodrom.amor.web.mapping;

import grails.web.mapping.UrlMapping;

import java.util.List;

@SuppressWarnings("rawtypes")
public class DefaultUrlMappingsHolder extends org.grails.web.mapping.DefaultUrlMappingsHolder {

    public DefaultUrlMappingsHolder(List<UrlMapping> mappings) {
        super(mappings);
    }

    public DefaultUrlMappingsHolder(List<UrlMapping> mappings, List excludePatterns) {
        super(mappings, excludePatterns);
    }

    public DefaultUrlMappingsHolder(List<UrlMapping> mappings, List excludePatterns, boolean doNotCallInit) {
        super(mappings, excludePatterns, doNotCallInit);
    }

    public void clearCache() {
        initialize();
    }

}
