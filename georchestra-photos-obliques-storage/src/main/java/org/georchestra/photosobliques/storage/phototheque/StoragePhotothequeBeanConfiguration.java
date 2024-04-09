package org.georchestra.photosobliques.storage.phototheque;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "photothequeEntityManagerFactory",
        transactionManagerRef = "photothequeTransactionManager",
        basePackages = "org.georchestra.photosobliques.storage.phototheque"
)
@Configuration
public class StoragePhotothequeBeanConfiguration {

    @Value("${spring.phototheque.datasource.hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${spring.phototheque.datasource.hibernate.format_sql}")
    private String hibernateFormatSql;

//    @Value("${spring.phototheque.datasource.hibernate.hbm2ddl.auto}")
//    private String hibernateHbm2ddlAuto;

    @Value("${spring.phototheque.datasource.hibernate.dialect}")
    private String hibernateDialect;

    @Bean(name = "photothequeDataSource")
    @ConfigurationProperties(prefix = "spring.phototheque.datasource")
    public DataSource photothequeDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean(name = "photothequeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean photothequeEntityManagerFactory(@Qualifier("photothequeDataSource") DataSource dataSource, @Qualifier("entityManagerphotothequeFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(hibernateProperties())
                .packages("org.georchestra.photosobliques.storage.phototheque.entity")
                .persistenceUnit("photothequePU")
                .build();
    }


    @Primary
    @Bean(name = "photothequeTransactionManager")
    public PlatformTransactionManager photothequeTransactionManager(@Qualifier("photothequeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "entityManagerphotothequeFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "photothequeEntityManager")
    public EntityManager entityManager(@Qualifier("photothequeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    private Map<String, String> hibernateProperties() {

        HashMap<String, String> hibernateProperties = new HashMap<>();

        hibernateProperties.put("hibernate.show_sql", hibernateFormatSql);
        hibernateProperties.put("hibernate.format_sql", hibernateShowSql);
        hibernateProperties.put("hibernate.dialect", hibernateDialect);

        return hibernateProperties;
    }

}



