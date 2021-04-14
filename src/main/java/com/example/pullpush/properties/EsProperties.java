package com.example.pullpush.properties;

import com.example.pullpush.enums.ReadModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 10:53
 * description: es配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.es")
public class EsProperties {

    private Integer pageSize;

    private String filePath;

    private String version;

    private String analysis;

    private ReadModel readModel;

    private EsInfo es5;


    @Getter
    @Setter
    public static class EsInfo {
        private String key;
        private String host;
        private String appId;
        private String fullQuery;
    }

}

