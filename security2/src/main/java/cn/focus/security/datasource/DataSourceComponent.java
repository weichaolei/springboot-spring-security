package cn.focus.security.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceComponent {

    @Resource
    MasterDataSourceConfig masterDataSourceConfig;

    @Resource
    SlaveDataSourceConfig slaveDataSourceConfig;

    public DataSource masterDataSource() {
        DataSource masterDataSource = new DataSource();
        masterDataSource.setUrl(masterDataSourceConfig.getUrl());
        masterDataSource.setUsername(masterDataSourceConfig.getUsername());
        masterDataSource.setPassword(masterDataSourceConfig.getPassword());
        masterDataSource.setDriverClassName(masterDataSourceConfig.getDriverClassName());
        masterDataSource.setTestOnBorrow(masterDataSourceConfig.getTestOnBorrow());
        masterDataSource.setTestWhileIdle(masterDataSourceConfig.getTestWhileIdle());
        masterDataSource.setValidationQuery(masterDataSourceConfig.getValidationQuery());
        return masterDataSource;
    }

    public DataSource slaveDataSource() {
        DataSource slaveDataSource = new DataSource();
        slaveDataSource.setUrl(slaveDataSourceConfig.getUrl());
        slaveDataSource.setUsername(slaveDataSourceConfig.getUsername());
        slaveDataSource.setPassword(slaveDataSourceConfig.getPassword());
        slaveDataSource.setDriverClassName(slaveDataSourceConfig.getDriverClassName());
        slaveDataSource.setTestOnBorrow(slaveDataSourceConfig.getTestOnBorrow());
        slaveDataSource.setTestWhileIdle(slaveDataSourceConfig.getTestWhileIdle());
        slaveDataSource.setValidationQuery(slaveDataSourceConfig.getValidationQuery());
        return slaveDataSource;
    }

    @Primary
    @Bean(name = "multiDatasource")
    public MultiRouteDataSource multiRouteDataSource() {
        MultiRouteDataSource dataSource = new MultiRouteDataSource();
        DataSource masterDataSource = masterDataSource();
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("master", masterDataSource);
        dsMap.put("slave", slaveDataSource());
        dataSource.setTargetDataSources(dsMap);
        dataSource.setDefaultTargetDataSource(masterDataSource);
        return dataSource;
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(multiRouteDataSource());
        return manager;
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(multiRouteDataSource());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactoryBean.setConfiguration(configuration);
        return sessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }
}
