package com.restdude.config;

import com.restdude.domain.users.model.User;
import com.restdude.mdd.util.EntityUtil;
import com.restdude.util.audit.AuditorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"**.restdude", "**.calipso"},
        repositoryFactoryBeanClass = com.restdude.domain.base.repository.ModelRepositoryFactoryBean.class,
        repositoryBaseClass = com.restdude.domain.base.repository.BaseRepositoryImpl.class
)
@EnableJpaAuditing
public class PersistenceJPAConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceJPAConfig.class);
    public static final String[] BASE_PACKAGES = {"**.restdude", "**.calipso"};

    public static String getPropertyAsString(Properties prop) {
        StringWriter writer = new StringWriter();
        try {
            prop.store(writer, "");
        } catch (IOException e) {
            LOGGER.warn("Failed listing properties", e);
        }
        return writer.getBuffer().toString();
    }

    @Value("${calipso.ds.driverClass}")
    private String dsDriverClass;

    @Value("${calipso.ds.jdbcUrl}")
    private String dsJdbcUrl;

    @Value("${calipso.ds.username}")
    private String dsUsername;

    @Value("${calipso.ds.password}")
    private String dsPassword;

    @Value("${calipso.persistenceUnit.packagesToScan}")
    private String emPackagesToScan;

    @Value("${calipso.hibernate.dialect}")
    private String emHbmDialect;

    @Value("${calipso.hibernate.show_sql}")
    private String emHbmShowSql;

    @Value("${calipso.hibernate.format_sql}")
    private String emHbmFormatSql;

    @Value("${calipso.hibernate.hbm2ddl.auto}")
    private String emHbm2ddlAuto;

    @Value("${calipso.hibernate.cache.use_second_level_cache}")
    private String emHbmCacheUseSecondLevelCache;

    @Value("${calipso.hibernate.cache.provider_class}")
    private String emHbmCacheProviderClass;

    @Value("${calipso.hibernate.id.new_generator_mappings}")
    private String emHbmIdNewGeneratorMappings;

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        // expand package patterns because
        // org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.setPackagesToScan()
        // does not handle wildcards
        Set<String> entityPackageNames = EntityUtil.findEntityPackageNames(BASE_PACKAGES);
        String[] entityPackages = entityPackageNames.toArray(new String[entityPackageNames.size()]);
        LOGGER.debug("entityManagerFactory setPackagesToScan: {}", Arrays.toString(entityPackages));
        em.setPackagesToScan(entityPackages);

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        em.afterPropertiesSet();

        return (EntityManagerFactory) em.getObject();
    }

    @Bean("dataSource")
    public DataSource dataSource() {
        LOGGER.debug("dataSource setDriverClassName: {}", this.dsDriverClass);
        LOGGER.debug("dataSource setUrl: {}", this.dsJdbcUrl);
        LOGGER.debug("dataSource setUsername: {}", this.dsUsername);
        LOGGER.debug("dataSource setPassword: {}", this.dsPassword);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.dsDriverClass);
        dataSource.setUrl(this.dsJdbcUrl);
        dataSource.setUsername(this.dsUsername);
        dataSource.setPassword(this.dsPassword);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        LOGGER.debug("exceptionTranslation");
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        LOGGER.debug("additionalProperties: ");
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", this.emHbmDialect);
        properties.setProperty("hibernate.show_sql", this.emHbmShowSql);
        properties.setProperty("hibernate.format_sql", this.emHbmFormatSql);
        properties.setProperty("hibernate.hbm2ddl.auto", this.emHbm2ddlAuto);
        properties.setProperty("hibernate.cache.use_second_level_cache", this.emHbmCacheUseSecondLevelCache);
        properties.setProperty("hibernate.cache.provider_class", this.emHbmCacheProviderClass);
        properties.setProperty("hibernate.id.new_generator_mappings", this.emHbmIdNewGeneratorMappings);
        properties.setProperty("javax.persistence.validation.mode", "none");

        properties.setProperty("jadira.usertype.autoRegisterUserTypes", Boolean.TRUE.toString());

        if (LOGGER.isDebugEnabled()) {
            for (Object key : properties.keySet()) {
                LOGGER.debug("{}={}", key, properties.getProperty(key.toString()));
            }
        }
        return properties;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorBean();
    }
}