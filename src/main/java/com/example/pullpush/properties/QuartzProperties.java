package com.example.pullpush.properties;

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
	private String time;
}
