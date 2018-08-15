package com.inno72.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.inno72.common.DataSourceKey;
import com.inno72.common.DynamicRoutingDataSource;
import com.inno72.config.client.DataSourceInno72Properties;
import com.inno72.config.client.DataSourceSAASProperties;
import com.inno72.utils.page.PagePlugin;

/**
 * 数据源配置.
 *
 * @author Houkm
 *
 *         2017年5月5日
 */
@Configuration
@EnableConfigurationProperties({ DataSourceProperties.class, DataSourceInno72Properties.class, DataSourceSAASProperties.class})
@EnableTransactionManagement
public class DBConfig {

	private Logger logger = LoggerFactory.getLogger(DBConfig.class);

	@Resource
	private DataSourceInno72Properties inno72DbProperties;
	@Resource
	private DataSourceSAASProperties saasDbProperties;

	public DataSource dbInno72() {

		DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
		dataSource.setDriverClassName(inno72DbProperties.getDriverClassName());
		dataSource.setUrl(inno72DbProperties.getUrl());
		dataSource.setUsername(inno72DbProperties.getUsername());
		dataSource.setPassword(inno72DbProperties.getPassword());
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setValidationQueryTimeout( 1000 * 10 );
		dataSource.setTimeBetweenEvictionRunsMillis( 5 * 1000 );
		dataSource.setMinEvictableIdleTimeMillis( 5 * 1000 );
		// 过添加滤器
		Slf4jLogFilter logFilter = new Slf4jLogFilter();
		logger.info("inno72 配置DruidDataSource logFilter: statementLogEnabled=false");
		logFilter.setStatementLogEnabled(false);
		logger.info("inno72 配置DruidDataSource logFilter: statementLogErrorEnabled=true");
		logFilter.setStatementLogErrorEnabled(true);
		logger.info("inno72 配置DruidDataSource logFilter: statementExecutableSqlLogEnable=true");
		logFilter.setStatementExecutableSqlLogEnable(true);
		List<Filter> filters = new ArrayList<>();
		filters.add(logFilter);
		dataSource.setProxyFilters(filters);
		logger.info("inno72 DruidDataSource配置完毕");

		return dataSource;
	}

	public DataSource dbSAAS() {
		DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
		dataSource.setDriverClassName(saasDbProperties.getDriverClassName());
		dataSource.setUrl(saasDbProperties.getUrl());
		dataSource.setUsername(saasDbProperties.getUsername());
		dataSource.setPassword(saasDbProperties.getPassword());
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setValidationQueryTimeout( 1000 * 10 );
		dataSource.setTimeBetweenEvictionRunsMillis( 5 * 1000 );
		dataSource.setMinEvictableIdleTimeMillis( 5 * 1000 );
		// 过添加滤器
		Slf4jLogFilter logFilter = new Slf4jLogFilter();
		logger.info("saas配置DruidDataSource logFilter: statementLogEnabled=false");
		logFilter.setStatementLogEnabled(false);
		logger.info("saas配置DruidDataSource logFilter: statementLogErrorEnabled=true");
		logFilter.setStatementLogErrorEnabled(true);
		logger.info("saas配置DruidDataSource logFilter: statementExecutableSqlLogEnable=true");
		logFilter.setStatementExecutableSqlLogEnable(true);
		List<Filter> filters = new ArrayList<>();
		filters.add(logFilter);
		dataSource.setProxyFilters(filters);
		logger.info("saas DruidDataSource配置完毕");
		return dataSource;
	}

	/**
	 * 核心动态数据源
	 *
	 * @return 数据源实例
	 */
	@Bean
	@Primary
	public DataSource dynamicDataSource() {
		DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
		dataSource.setDefaultTargetDataSource(dbInno72());
		Map<Object, Object> dataSourceMap = new HashMap<>(4);
		dataSourceMap.put(DataSourceKey.DB_INNO72SAAS, dbSAAS());
		dataSource.setTargetDataSources(dataSourceMap);
		return dataSource;
	}

//	@Bean
//	@Primary
//	public SqlSessionFactory sqlSessionFactory() throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dynamicDataSource());
//		sqlSessionFactoryBean.setTypeHandlersPackage("org.apache.ibatis.type.LocalDateTimeTypeHandler");
//		Properties props = new Properties();
//		props.setProperty("dialect", "mysql");
//		props.setProperty("pageSqlId", "(ByPage|ForPage)");
//		PagePlugin pagePlugin = new PagePlugin();
//		pagePlugin.setProperties(props);
//
//		OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
//		Properties offsetLimitProps = new Properties();
//		offsetLimitProps.setProperty("dialectClass", "com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect");
//		offsetLimitInterceptor.setProperties(offsetLimitProps);
//
//		sqlSessionFactoryBean.setPlugins(new Interceptor[] { offsetLimitInterceptor, pagePlugin });
//
//		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("com.inno72.**.mapper"));
//		return sqlSessionFactoryBean.getObject();
//	}

//	@Bean
//	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory());
//	}
	/**
	 * 配置Druid数据源.
	 *
	 * @param prop
	 *            数据源配置属性
	 * @return DruidDataSource
	 * @author Houkm
	 */
	//	@Bean
	//	@Primary
	//	public DruidDataSource dataSource(DataSourceProperties prop) {
	//		logger.info("配置DruidDataSource：url({}), username({})", prop.determineUrl(), prop.determineUsername());
	//		DruidDataSource datasource = new DruidDataSource();
	//		datasource.setDriverClassName(prop.determineDriverClassName());
	//		datasource.setUrl(prop.determineUrl());
	//		datasource.setUsername(prop.determineUsername());
	//		datasource.setPassword(prop.determinePassword());
	//		datasource.setValidationQuery("SELECT 1");
	//		datasource.setValidationQueryTimeout( 1000 * 10 );
	//		datasource.setTimeBetweenEvictionRunsMillis( 5 * 1000 );
	//		datasource.setMinEvictableIdleTimeMillis( 5 * 1000 );
	//		// 过添加滤器
	//		Slf4jLogFilter logFilter = new Slf4jLogFilter();
	//		logger.info("配置DruidDataSource logFilter: statementLogEnabled=false");
	//		logFilter.setStatementLogEnabled(false);
	//		logger.info("配置DruidDataSource logFilter: statementLogErrorEnabled=true");
	//		logFilter.setStatementLogErrorEnabled(true);
	//		logger.info("配置DruidDataSource logFilter: statementExecutableSqlLogEnable=true");
	//		logFilter.setStatementExecutableSqlLogEnable(true);
	//		List<Filter> filters = new ArrayList<>();
	//		filters.add(logFilter);
	//		datasource.setProxyFilters(filters);
	//		logger.info("DruidDataSource配置完毕");
	//		return datasource;
	//	}
	//
	/**
	 * 配置事务管理器.
	 *
	 * @param dataSource
	 *            数据源
	 * @return PlatformTransactionManager
	 * @author Houkm
	 */
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		logger.info("为数据源{}配置事务管理器DataSourceTransactionManager", dataSource.getClass().getName());
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		logger.info("配置事务管理器DataSourceTransactionManager完毕");
		return transactionManager;
	}


}
