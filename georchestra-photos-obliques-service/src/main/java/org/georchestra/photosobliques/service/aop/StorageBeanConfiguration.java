package org.georchestra.photosobliques.service.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.georchestra.photosobliques.storage.aop"})
public class StorageBeanConfiguration {

}
