package ru.melodrom.amor.web.mapping;

import grails.config.Config;
import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import grails.core.GrailsControllerClass;
import grails.core.GrailsUrlMappingsClass;
import grails.core.events.ArtefactAdditionEvent;
import grails.core.support.GrailsApplicationAware;
import grails.plugins.GrailsPluginManager;
import grails.plugins.PluginManagerAware;
import grails.web.UrlConverter;
import grails.web.mapping.UrlMappings;
import groovy.lang.Script;
import org.grails.core.artefact.UrlMappingsArtefactHandler;
import org.grails.web.mapping.DefaultUrlMappingEvaluator;
import org.grails.web.mapping.DefaultUrlMappings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class UrlMappingsHolderFactoryBean implements FactoryBean<UrlMappings>, InitializingBean, ApplicationContextAware, GrailsApplicationAware, PluginManagerAware {
    private static final String URL_MAPPING_CACHE_MAX_SIZE = "grails.urlmapping.cache.maxsize";
    private static final String URL_CREATOR_CACHE_MAX_SIZE = "grails.urlcreator.cache.maxsize";
    private GrailsApplication grailsApplication;
    private UrlMappings urlMappingsHolder;
    private GrailsPluginManager pluginManager;
    private ApplicationContext applicationContext;

    public UrlMappings getObject() throws Exception {
        return urlMappingsHolder;
    }

    public Class<UrlMappings> getObjectType() {
        return UrlMappings.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(applicationContext != null, "Property [applicationContext] must be set!");
        Assert.state(grailsApplication != null, "Property [grailsApplication] must be set!");

        List urlMappings = new ArrayList();
        List excludePatterns = new ArrayList();

        GrailsClass[] mappings = grailsApplication.getArtefacts(UrlMappingsArtefactHandler.TYPE);
        final DefaultUrlMappingEvaluator mappingEvaluator = new DefaultUrlMappingEvaluator(applicationContext);
        mappingEvaluator.setPluginManager(pluginManager);

        if (mappings.length == 0) {
            urlMappings.addAll(mappingEvaluator.evaluateMappings(DefaultUrlMappings.getMappings()));
        } else {
            for (GrailsClass mapping : mappings) {
                GrailsUrlMappingsClass mappingClass = (GrailsUrlMappingsClass) mapping;
                List grailsClassMappings;
                if (Script.class.isAssignableFrom(mappingClass.getClazz())) {
                    grailsClassMappings = mappingEvaluator.evaluateMappings(mappingClass.getClazz());
                } else {
                    grailsClassMappings = mappingEvaluator.evaluateMappings(mappingClass.getMappingsClosure());
                }

                urlMappings.addAll(grailsClassMappings);
                if (mappingClass.getExcludePatterns() != null) {
                    excludePatterns.addAll(mappingClass.getExcludePatterns());
                }
            }
        }

        urlMappings.add(new AmorUrlMapping());

        DefaultUrlMappingsHolder defaultUrlMappingsHolder = new DefaultUrlMappingsHolder(urlMappings, excludePatterns, true);

        Config config = grailsApplication.getConfig();
        Integer cacheSize = config.getProperty(URL_MAPPING_CACHE_MAX_SIZE, Integer.class, null);
        if (cacheSize != null) {
            defaultUrlMappingsHolder.setMaxWeightedCacheCapacity(cacheSize);
        }
        Integer urlCreatorCacheSize = config.getProperty(URL_CREATOR_CACHE_MAX_SIZE, Integer.class, null);
        if (urlCreatorCacheSize != null) {
            defaultUrlMappingsHolder.setUrlCreatorMaxWeightedCacheCapacity(urlCreatorCacheSize);
        }
        // call initialize() after settings are in place
        defaultUrlMappingsHolder.initialize();
        UrlConverter urlConverter = applicationContext.containsBean(UrlConverter.BEAN_NAME) ? applicationContext.getBean(UrlConverter.BEAN_NAME, UrlConverter.class) : null;
        final org.grails.web.mapping.mvc.GrailsControllerUrlMappings grailsControllerUrlMappings = new org.grails.web.mapping.mvc.GrailsControllerUrlMappings(grailsApplication, defaultUrlMappingsHolder, urlConverter);
        ((ConfigurableApplicationContext) applicationContext).addApplicationListener(new ApplicationListener<ArtefactAdditionEvent>() {
            @Override
            public void onApplicationEvent(ArtefactAdditionEvent event) {
                GrailsClass artefact = event.getArtefact();
                if (artefact instanceof GrailsControllerClass) {
                    grailsControllerUrlMappings.registerController((GrailsControllerClass) artefact);
                }
            }
        });
        urlMappingsHolder = grailsControllerUrlMappings;
    }


    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }


    public void setPluginManager(GrailsPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link org.springframework.context.ResourceLoaderAware#setResourceLoader},
     * {@link org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link org.springframework.context.MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws org.springframework.context.ApplicationContextException in case of context initialization errors
     * @throws org.springframework.beans.BeansException                if thrown by application context methods
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        setGrailsApplication(applicationContext.getBean(GrailsApplication.APPLICATION_ID, GrailsApplication.class));
        setPluginManager(applicationContext.containsBean(GrailsPluginManager.BEAN_NAME) ? applicationContext.getBean(GrailsPluginManager.BEAN_NAME, GrailsPluginManager.class) : null);
    }

}
