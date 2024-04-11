package run.freshr.common.extensions;

import com.redis.testcontainers.RedisContainer;
import java.util.HashMap;
import org.junit.ClassRule;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import run.freshr.TestRunner;
import run.freshr.service.TestService;

@Testcontainers
public class TestExtension extends TestSecurityExtensionAware<TestService, TestRunner> {

  private static final String REDIS_IMAGE = "redis:latest";
  private static final String POSTGRES_IMAGE = "postgres:latest";
  private static final String ELASTICSEARCH_IMAGE = "elasticsearch:8.7.0";

  @ClassRule
  public static final RedisContainer REDIS_CONTAINER;
  @ClassRule
  public static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
  @ClassRule
  public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER;

  static {
    REDIS_CONTAINER = new RedisContainer(DockerImageName.parse(REDIS_IMAGE))
        .withExposedPorts(6379)
        .withCommand("redis-server --requirepass redis-password");

    POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
        .withUsername("postgres")
        .withPassword("postgres")
        .withDatabaseName("postgres");
    POSTGRES_CONTAINER
        .withInitScript("docker/env/postgres/init/schema.sql");

    ELASTICSEARCH_CONTAINER = new ElasticsearchContainer(
        DockerImageName
            .parse(ELASTICSEARCH_IMAGE)
            .asCompatibleSubstituteFor(
                "docker.elastic.co/elasticsearch/elasticsearch")
    ).withExposedPorts(9200, 9300)
        .withClasspathResourceMapping(
            "docker/env/elasticsearch/config/elasticsearch.yml",
            "/usr/share/elasticsearch/config/elasticsearch.yml",
            BindMode.READ_ONLY)
        .withEnv(new HashMap<>() {{
          put("node.name", "elasticsearch");
          put("ELASTIC_PASSWORD", "elasticsearch-password");
          put("discovery.type", "single-node");
        }});

    REDIS_CONTAINER.start();
    POSTGRES_CONTAINER.start();
    ELASTICSEARCH_CONTAINER.start();
  }

  @DynamicPropertySource
  public static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", () ->
        REDIS_CONTAINER.getMappedPort(6379));
    registry.add("spring.data.redis.password", () -> "redis-password");

    registry.add("spring.elasticsearch.uris", () ->
        "http://localhost:" + ELASTICSEARCH_CONTAINER.getMappedPort(9200));
    registry.add("spring.elasticsearch.password", () -> "elasticsearch-password");

    registry.add("spring.datasource.url", () ->
        "jdbc:postgresql://localhost:"
            + POSTGRES_CONTAINER.getMappedPort(5432)
            + "/postgres");
  }

}
