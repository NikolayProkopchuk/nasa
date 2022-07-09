package com.prokopchuk.nasa.controller;

import com.prokopchuk.nasa.service.NasaPicturesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/pictures")
@AllArgsConstructor
public class Controller {

    private final NasaPicturesService nasaPicturesService;

    @GetMapping("/{sol}/largest")
    public HttpEntity<Void> redirectToLargestPicture(@PathVariable int sol) {
        String largestPictureUrl = nasaPicturesService
                .findLargestPictureUrl("DEMO_KEY", sol);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(largestPictureUrl))
                .build();
    }
}
