package cloud.fogbow.accs.core.datastore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ StorageConfiguration.CONFIGURATION_SOURCE })
@EnableJpaRepositories(
    basePackages = AccountingStorageConfiguration.ACCOUNTING_REPOS_PACKAGE, 
    entityManagerFactoryRef = AccountingStorageConfiguration.ACCOUNTING_ENTITY_MANAGER, 
    transactionManagerRef = AccountingStorageConfiguration.ACCOUNTING_TRANSACTION_MANAGER
)
public class AccountingStorageConfiguration extends StorageConfiguration {
	
	// All JPA repository interfaces used to access accounting data must be placed in this package.
	public static final String ACCOUNTING_REPOS_PACKAGE = "cloud.fogbow.accs.core.datastore.accountingstorage";
	public static final String ACCOUNTING_ENTITY_MANAGER = "accountingEntityManager";
	// This string is used along with the "transactional" tag to set the repository to use 
	// the accounting datasource.
	public static final String ACCOUNTING_TRANSACTION_MANAGER = "accountingTransactionManager";
	// The accounting datasource properties names must use this prefix.
	public static final String ACCOUNTING_DATASOURCE_CONFIGURATION_PREFIX = "spring.accounting-datasource";
	// All entities related to accounting must be placed in this package.
    private static final String ACCOUNTING_ENTITIES_PACKAGE = "cloud.fogbow.accs.core.models";
    
	@Autowired
    private Environment env;
    
    @Primary
    @Bean
    @ConfigurationProperties(prefix=ACCOUNTING_DATASOURCE_CONFIGURATION_PREFIX)
    public DataSource accountingDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean accountingEntityManager() {
    	DataSource source = accountingDataSource();
    	String[] packagesToScan = { ACCOUNTING_ENTITIES_PACKAGE };
        return getEntityManager(source, env, packagesToScan);
    }

    @Primary
    @Bean
    public PlatformTransactionManager accountingTransactionManager() {
        return getTransactionManager(accountingEntityManager());
    }
}
