package cloud.fogbow.accs.core.datastore;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class StorageConfiguration {

	// Property name of the implicit naming strategy.
	private static final String HIBERNATE_IMPLICIT_NAMING_STRATEGY = "hibernate.implicit_naming_strategy";
	// Property name of the physical naming strategy.
	private static final String HIBERNATE_PHYSICAL_NAMING_STRATEGY = "hibernate.physical_naming_strategy";
	// The name of the storage configuration file.
	public static final String CONFIGURATION_SOURCE = "classpath:application.properties";

	// Below properties are required.
	// Normally set to update, to create database tables if necessary.
	private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	// Hibernate dialect, related to type of datasource used.
	private static final String HIBERNATE_DIALECT = "hibernate.dialect";

    protected LocalContainerEntityManagerFactoryBean getEntityManager(DataSource dataSource, 
    		Environment env, String[] packagesToScan) {
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource(dataSource);
		entityManager.setPackagesToScan(packagesToScan);

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManager.setJpaVendorAdapter(vendorAdapter);

		HashMap<String, Object> properties = getHibernateProperties(env);
		entityManager.setJpaPropertyMap(properties);

		return entityManager;
    }
	
	private HashMap<String, Object> getHibernateProperties(Environment env) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put(HIBERNATE_HBM2DDL_AUTO, env.getProperty(HIBERNATE_HBM2DDL_AUTO));
		properties.put(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
		// Explicitly set naming strategies for Spring, otherwise they are not loaded correctly.
		properties.put(HIBERNATE_PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
		properties.put(HIBERNATE_IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
		
		return properties;
	}
	
	protected PlatformTransactionManager getTransactionManager(LocalContainerEntityManagerFactoryBean em) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(em.getObject());
		return transactionManager;
    }
}
