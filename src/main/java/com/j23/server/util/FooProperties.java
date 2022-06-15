package com.j23.server.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.foo")
@Getter
@Setter
public class FooProperties {

  /**
   * IP of foo service used to blah.
   */
  private final String IP = "127.0.0.1";

  // getter & setter
}
