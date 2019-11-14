package com.target.demomultischemas.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories (basePackages = "com.target.demomultischemas.repository.commerce",
        entityManagerFactoryRef = "commerceEntityManagerFactory",
        transactionManagerRef = "commerceTransactionManager")
public class CommerceDBConfiguration {

    @Bean
    @ConfigurationProperties ("app.datasource.commerce")
    public DataSourceProperties commerceDataSourceProperties () {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties ("app.datasource.commerce.configuration")
    public DataSource commerceDataSource () {
        return commerceDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean ("commerceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commerceEntityManagerFactory (
            EntityManagerFactoryBuilder builder) {

        LocalContainerEntityManagerFactoryBean entityManagerBeanFactory = builder
                .dataSource(commerceDataSource())
                .packages("com.target.demomultischemas.entity.commerce")
                .build();

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerBeanFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
//        hibernateJpaVendorAdapter.setGenerateDdl(true); // Same as hibernate.hbm2ddl.auto
//        hibernateJpaVendorAdapter.setShowSql(true); //Same as hibernate.show_sql
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL); //Same as hibernate.dialect_resolvers
//        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect"); //Same as hibernate.dialect

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        entityManagerBeanFactory.setJpaPropertyMap(properties);

        return entityManagerBeanFactory;
    }

    @Bean ("commerceTransactionManager")
    public PlatformTransactionManager commerceTransactionManager (
            final @Qualifier ("commerceEntityManagerFactory")
                    LocalContainerEntityManagerFactoryBean commerceEntityManagerFactory) {
        return new JpaTransactionManager(commerceEntityManagerFactory.getObject());
    }
}
