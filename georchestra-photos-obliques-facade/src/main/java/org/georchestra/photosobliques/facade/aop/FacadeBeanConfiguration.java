package org.georchestra.photosobliques.facade.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy()
@ComponentScan(basePackages = {"org.georchestra.photosobliques.facade.aop"})
public class FacadeBeanConfiguration {


}
