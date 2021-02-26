package cloud.fogbow.accs.core.datastore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ StorageConfiguration.CONFIGURATION_SOURCE })
@EnableJpaRepositories(
    basePackages = OrderStorageConfiguration.ORDER_REPOS_PACKAGE, 
    entityManagerFactoryRef = OrderStorageConfiguration.ORDER_ENTITY_MANAGER, 
    transactionManagerRef = OrderStorageConfiguration.ORDER_TRANSACTION_MANAGER
)
public class OrderStorageConfiguration extends StorageConfiguration {

	// All JPA repository interfaces used to access order data must be placed in this package.
	public static final String ORDER_REPOS_PACKAGE = "cloud.fogbow.accs.core.datastore.orderstorage";
	public static final String ORDER_ENTITY_MANAGER = "orderEntityManager";
	// This string is used along with the "transactional" tag to set the repository to use 
	// the order datasource.
	public static final String ORDER_TRANSACTION_MANAGER = "orderTransactionManager";
	// The order datasource properties names must use this prefix.
	public static final String ORDER_DATASOURCE_CONFIGURATION_PREFIX = "spring.order-datasource";
	// All entities related to Order must be placed in this package.
	private static final String ORDER_ENTITIES_PACKAGE = "cloud.fogbow.accs.core.models.orders";

	@Autowired
    private Environment env;
    
    @Bean
    @ConfigurationProperties(prefix=ORDER_DATASOURCE_CONFIGURATION_PREFIX)
    public DataSource orderDataSource() {
        return DataSourceBuilder.create().build();
    }
  
    @Bean
    public LocalContainerEntityManagerFactoryBean orderEntityManager() {
        DataSource source = orderDataSource();
    	String[] packagesToScan = { ORDER_ENTITIES_PACKAGE };
        return getEntityManager(source, env, packagesToScan);
    }
    
    @Bean
    public PlatformTransactionManager orderTransactionManager() {
    	return getTransactionManager(orderEntityManager());
    }
}