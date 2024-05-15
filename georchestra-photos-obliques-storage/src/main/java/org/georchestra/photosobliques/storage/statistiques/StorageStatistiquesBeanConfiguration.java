package org.georchestra.photosobliques.storage.statistiques;


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
        entityManagerFactoryRef = "statsEntityManagerFactory",
        transactionManagerRef = "statsTransactionManager",
        basePackages = "org.georchestra.photosobliques.storage.statistiques"
)
@Configuration
public class StorageStatistiquesBeanConfiguration {

    @Value("${spring.stats.datasource.hibernate.show_sql:false}")
    private String hibernateShowSql;

    @Value("${spring.stats.datasource.hibernate.format_sql:false}")
    private String hibernateFormatSql;

    @Value("${spring.stats.datasource.hibernate.hbm2ddl.auto:none}")
    private String hibernateHbm2ddlAuto;

    @Value("${spring.stats.datasource.hibernate.dialect:org.hibernate.dialect.PostgreSQLDialect}")
    private String hibernateDialect;

    @Bean(name = "statsDataSource")
    @ConfigurationProperties(prefix = "spring.stats.datasource")
    public DataSource statsDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean(name = "statsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean statistiquesEntityManagerFactory(@Qualifier("statsDataSource") DataSource dataSource, @Qualifier("entityManagerstatsFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(hibernateProperties())
                .packages("org.georchestra.photosobliques.storage.statistiques.entity")
                .persistenceUnit("statsPU")
                .build();
    }

    @Bean(name = "statsTransactionManager")
    public PlatformTransactionManager statsTransactionManager(@Qualifier("statsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "entityManagerstatsFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "statsEntityManager")
    public EntityManager entityManager(@Qualifier("statsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    private Map<String, String> hibernateProperties() {

        HashMap<String, String> hibernateProperties = new HashMap<>();

        hibernateProperties.put("hibernate.show_sql", hibernateFormatSql);
        hibernateProperties.put("hibernate.format_sql", hibernateShowSql);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        hibernateProperties.put("hibernate.dialect", hibernateDialect);

        return hibernateProperties;
    }

}



