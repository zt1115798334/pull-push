package com.example.pullpush.properties;

import com.example.pullpush.enums.JobType;
import com.example.pullpush.enums.TimeType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.quartz")
public class QuartzProperties {
    private String corn;
    private TimeType timeType;
    private Integer timeRange;
    private JobType jobType;
}
