package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate restTemplate;
    private HttpHeaders headers;
    private final String serverUrl;

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<List<ShortStatDto>> getStats(LocalDateTime start, LocalDateTime end,
                                                       String[] uris, boolean unique) {
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {}, start.format(FORMATTER), end.format(FORMATTER), uris, unique);
    }

    public ResponseEntity<StatDto> sendPost(String app, String uri, String ip, LocalDateTime timestamp) {
        StatDto statDto = StatDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();
        HttpEntity<StatDto> entity = new HttpEntity(statDto, headers);
        return restTemplate.exchange("/hit", HttpMethod.POST, entity, StatDto.class);
    }
}
