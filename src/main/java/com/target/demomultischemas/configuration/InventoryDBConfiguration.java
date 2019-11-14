package com.target.demomultischemas.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableJpaRepositories (basePackages = "com.target.demomultischemas.repository.inventory",
    entityManagerFactoryRef = "inventoryEntityManagerFactory",
    transactionManagerRef = "inventoryTransactionManager")
public class InventoryDBConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties ("app.datasource.inventory")
    public DataSourceProperties inventoryDataSourceProperties () {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties ("app.datasource.inventory.configuration")
    public DataSource inventoryDataSource () {
        return inventoryDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean ("inventoryEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean inventoryEntityManagerFactory (
            EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerBeanFactory = builder
                .dataSource(inventoryDataSource())
                .packages("com.target.demomultischemas.entity.inventory")
                .build();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerBeanFactory.setJpaVendorAdapter(vendorAdapter);
//        vendorAdapter.setGenerateDdl(true); // Same as hibernate.hbm2ddl.auto
//        vendorAdapter.setShowSql(true); //Same as hibernate.show_sql
        vendorAdapter.setDatabase(Database.MYSQL); //Same as hibernate.dialect_resolvers
//        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect"); //Same as hibernate.dialect

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        entityManagerBeanFactory.setJpaPropertyMap(properties);
        return entityManagerBeanFactory;
    }

    @Bean ("inventoryTransactionManager")
    @Primary
    public PlatformTransactionManager inventoryTransactionManager (
            final @Qualifier("inventoryEntityManagerFactory")
                    LocalContainerEntityManagerFactoryBean inventoryEntityManagerFactory) {
        return new JpaTransactionManager(inventoryEntityManagerFactory.getObject());
    }
}
