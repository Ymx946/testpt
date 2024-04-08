package com.mz.config.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration
public class MybatisPlusDataSource {
    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = dynamicDataSourceProperties.getDatasource();
        return new YmlDynamicDataSourceProvider(datasourceMap);
    }

    @Bean
    public DynamicDataSourceStrategy dynamicDataSourceStrategy() throws Exception {
        return dynamicDataSourceProperties.getStrategy().newInstance();
    }

    @Bean(name = "activitiDataSource")
    public DataSource activitiDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
        dataSource.setStrict(dynamicDataSourceProperties.getStrict());
        dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
        dataSource.setSeata(dynamicDataSourceProperties.getSeata());
        return dataSource;
    }
}
