package kr.zb.nengtul.crawling.recipe.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticSearchHostUri;

    @Value("${spring.elasticsearch.username}")
    private String userName;

    @Value("${spring.elasticsearch.password}")
    private String password;


    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(elasticSearchHostUri)
                .withBasicAuth(userName, password)
                .build();

        return RestClients.create(configuration).rest();
    }

}
