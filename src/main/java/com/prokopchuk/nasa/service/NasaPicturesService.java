package com.prokopchuk.nasa.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NasaPicturesService {
    private static final String PICTURES_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";

    @SneakyThrows
    @Cacheable(value = "largestPictureCache", key = "#sol")
    public String findLargestPictureUrl(String apiKey, int sol) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(PICTURES_URL)
                .queryParam("sol", sol)
                .queryParam("api_key", apiKey)
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();

        return Optional.ofNullable(restTemplate.getForObject(uri, Photos.class))
                .orElseThrow()
                .photos()
                .parallelStream()
                .map(photo -> Pair.of(photo.url,
                        Long.parseLong(Objects.requireNonNull(restTemplate.headForHeaders(
                                Objects.requireNonNull(restTemplate.headForHeaders(photo.url).getFirst(HttpHeaders.LOCATION)))
                                .getFirst(HttpHeaders.CONTENT_LENGTH)))))
                .max(Comparator.comparing(Pair::getRight))
                .map(Pair::getLeft)
                .orElseThrow();
    }

    record Photo(@JsonProperty("img_src") String url) {
    }

    record Photos(List<Photo> photos) {
    }
}
