package tw.sanjiheart.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Value("${mongo.host}")
  private String host;

  @Value("${mongo.port}")
  private String port;

  @Value("${mongo.dbName}")
  private String dbName;

  @Override
  protected String getDatabaseName() {
    return dbName;
  }

  @Override
  public MongoClient mongoClient() {
    ConnectionString connectStr = new ConnectionString(
        String.format("mongodb://%s:%s/%s", host, port, getDatabaseName()));
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectStr).build();
    return MongoClients.create(mongoClientSettings);
  }

}
