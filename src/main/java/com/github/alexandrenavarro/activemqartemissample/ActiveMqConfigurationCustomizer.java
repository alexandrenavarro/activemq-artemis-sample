package com.github.alexandrenavarro.activemqartemissample;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.core.config.Configuration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;

/**
 * Created by anavarro on 18/04/17.
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class ActiveMqConfigurationCustomizer implements ArtemisConfigurationCustomizer {

    @Override
    public void customize(Configuration configuration) {
        log.info("configuration:{}", configuration);
    }
}
