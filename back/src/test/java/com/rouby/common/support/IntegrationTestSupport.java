package com.rouby.common.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.container.RedisTestContainerExtension;
import com.rouby.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import({DatabaseCleanUp.class, TestcontainersConfiguration.class})
@ExtendWith(RedisTestContainerExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public abstract class IntegrationTestSupport {

  @Autowired
  protected StringRedisTemplate stringRedisTemplate;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserRepository userRepository;

  @AfterEach
  void tearDown() {
    databaseCleanUp.afterPropertiesSet();
    databaseCleanUp.execute();
    clearRedis();
  }

  private void clearRedis() {
    stringRedisTemplate.delete(stringRedisTemplate.keys("*"));
  }

}
