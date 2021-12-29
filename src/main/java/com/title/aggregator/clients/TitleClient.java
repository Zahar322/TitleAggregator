package com.title.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "titles", url = "${feign.client.config.titles.url}")
public interface TitleClient {

    @GetMapping("/titles")
    String getTitles();

    @PostMapping("titles")
    void sendTitles(Object object);
}
