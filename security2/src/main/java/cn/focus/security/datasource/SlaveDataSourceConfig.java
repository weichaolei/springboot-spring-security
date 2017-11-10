package cn.focus.security.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "slave.datasource")
public class SlaveDataSourceConfig extends BaseDataSourceConfig {

}
