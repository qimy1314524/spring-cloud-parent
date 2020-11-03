package com.smart.aiplatformauth.config;

import com.smart.aiplatformauth.Enum.DataSourceEnum;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;


/**
 * @desc: MybatisPlus配置
 * @author chengjz
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisConfig {

	@Autowired
	private Environment environment;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private Interceptor[] interceptors;

	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;


	private static Logger log = LoggerFactory.getLogger(MybatisConfig.class);

	/**
	 * @desc: SQL执行效率插件
	 */
	public PerformanceInterceptor performanceInterceptor() {
	    return new PerformanceInterceptor();
    }

    /**
     * @desc: 配置druidDataSource
     */
    /*@Bean
    public DataSource druidDataSource() {
        log.info("=========================== 注入druid =============================");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(environment.getProperty("spring.datasource.url"));
        druidDataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        druidDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        druidDataSource.setPassword(environment.getProperty("spring.datasource.password"));
        druidDataSource.setInitialSize(Integer.valueOf(environment.getProperty("spring.datasource.initial-size")));
        druidDataSource.setMinIdle(Integer.valueOf(environment.getProperty("spring.datasource.min-idle")));
        druidDataSource.setMaxWait(Long.valueOf(environment.getProperty("spring.datasource.max-wait")));
        druidDataSource.setMaxActive(Integer.valueOf(environment.getProperty("spring.datasource.max-active")));
        druidDataSource.setMinEvictableIdleTimeMillis(Long.valueOf(environment.getProperty("spring.datasource.min-evictable-idle-time-millis")));
        druidDataSource.setValidationQuery(environment.getProperty("spring.datasource.validationQuery"));
        try {
            druidDataSource.setFilters(environment.getProperty("spring.datasource.filters"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }*/

	/**
	 * @desc: mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		return page;
	}

	/**
	 * @desc: 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定 配置文件和mybatis-boot的配置文件同步
	 */
	/*@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(dataSource);
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		mybatisPlus.setConfiguration(properties.getConfiguration());
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			mybatisPlus.setPlugins(this.interceptors);
		}
		// MP 全局配置，更多内容进入类看注释
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.name());
		// ID 策略 AUTO->`0`("数据库ID自增")
		// INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID") UUID->`3`("全局唯一ID")
		globalConfig.setIdType(3);
		mybatisPlus.setGlobalConfig(globalConfig);
		MybatisConfiguration mc = new MybatisConfiguration();
		mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		mc.setMapUnderscoreToCamelCase(false);
		mc.setUseColumnLabel(true);
		mybatisPlus.setConfiguration(mc);
		if (this.databaseIdProvider != null) {
			mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return mybatisPlus;
	}*/

	/**
	 * @desc: 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定 配置文件和mybatis-boot的配置文件同步
	 */
	@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("multiDataSource") DataSource multiDataSource) {
		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(multiDataSource);
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		mybatisPlus.setConfiguration(properties.getConfiguration());
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			mybatisPlus.setPlugins(this.interceptors);
		}
		/*// MP 全局配置，更多内容进入类看注释
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.name());
		// ID 策略 AUTO->`0`("数据库ID自增")
		// INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID") UUID->`3`("全局唯一ID")
		globalConfig.setIdType(3);*/
		mybatisPlus.setGlobalConfig(globalConfiguration());
		MybatisConfiguration mc = new MybatisConfiguration();
		mc.setJdbcTypeForNull(JdbcType.NULL);
		mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		mc.setMapUnderscoreToCamelCase(false);//驼峰命名转换配置注意
		mc.setUseColumnLabel(true);//开启column字段注解
		//mc.setCacheEnabled(true);//后期研究下缓存开启的必要性
		mybatisPlus.setConfiguration(mc);
		if (this.databaseIdProvider != null) {
			mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
		}

		return mybatisPlus;
	}

	/*@Bean
	public SqlSessionFactory sqlSessionFactory(PaginationInterceptor paginationInterceptor,
			@Qualifier("multiDataSource") DataSource multiDataSource) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(multiDataSource);
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCacheEnabled(false);
		sqlSessionFactory.setConfiguration(configuration);
		sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor});
		sqlSessionFactory.setMapperLocations(this.properties.resolveMapperLocations());//配置mapper位置
		sqlSessionFactory.setGlobalConfig(globalConfiguration());
		sqlSessionFactory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());//配置实体类位置
		return sqlSessionFactory.getObject();
	}*/

	@Bean
	public GlobalConfiguration globalConfiguration() {
		GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
		conf.setLogicDeleteValue("-1");
		conf.setLogicNotDeleteValue("1");
		conf.setIdType(0);
		//conf.setMetaObjectHandler(new MyMetaObjectHandler());
		conf.setDbColumnUnderline(true);
		conf.setRefresh(true);
		return conf;
	}

	/**
	 * 参数校验功能
	 * @return
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}

	/**
	 * postgresql数据源
	 */
	@Bean(name = "pgDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.pg")
	public DataSource pgDataSource() {
		//return DataSourceBuilder.create().build();
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setUrl(environment.getProperty("spring.datasource.mysql.url"));
		druidDataSource.setDriverClassName(environment.getProperty("spring.datasource.mysql.driver-class-name"));
		druidDataSource.setUsername(environment.getProperty("spring.datasource.mysql.username"));
		druidDataSource.setPassword(environment.getProperty("spring.datasource.mysql.password"));
		return druidDataSource;
	}

	/**
	 * mysql数据源
	 */
	@Bean(name = "mysqlDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource mysqlDataSource() {
		//return DataSourceBuilder.create().build();
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setUrl(environment.getProperty("spring.datasource.mysql.url"));
		druidDataSource.setDriverClassName(environment.getProperty("spring.datasource.mysql.driver-class-name"));
		druidDataSource.setUsername(environment.getProperty("spring.datasource.mysql.username"));
		druidDataSource.setPassword(environment.getProperty("spring.datasource.mysql.password"));
		druidDataSource.setInitialSize(Integer.valueOf(environment.getProperty("spring.datasource.mysql.initial-size")));
		druidDataSource.setMinIdle(Integer.valueOf(environment.getProperty("spring.datasource.mysql.min-idle")));
		druidDataSource.setMaxWait(Long.valueOf(environment.getProperty("spring.datasource.mysql.max-wait")));
		druidDataSource.setMaxActive(Integer.valueOf(environment.getProperty("spring.datasource.mysql.max-active")));
		druidDataSource.setMinEvictableIdleTimeMillis(Long.valueOf(environment.getProperty("spring.datasource.mysql.min-evictable-idle-time-millis")));
		druidDataSource.setValidationQuery(environment.getProperty("spring.datasource.mysql.validationQuery"));
		try {
			druidDataSource.setFilters(environment.getProperty("spring.datasource.mysql.filters"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return druidDataSource;
	}

	/**
	 * 多数据源动态切换
	 */
	@Bean
	public DataSource multiDataSource(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
		Map<Object, Object> target = new HashMap<>();
		target.put(DataSourceEnum.PG, pgDataSource());
		target.put(DataSourceEnum.MYSQL, mysqlDataSource);

		AbstractRoutingDataSource dataSource = new DynamicDataSource();
		dataSource.setDefaultTargetDataSource(mysqlDataSource);
		dataSource.setTargetDataSources(target);
		return dataSource;
	}

}
