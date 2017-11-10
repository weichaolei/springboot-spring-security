package cn.focus.security.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiRouteDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

}
