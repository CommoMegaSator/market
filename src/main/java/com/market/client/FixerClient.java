package com.market.client;

import com.market.dto.FixerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fixerClient", url = "http://data.fixer.io/api")
public interface FixerClient {

    @GetMapping("/latest")
    FixerResponse getLatestCurrency(
            @RequestParam("access_key") String accessKey);
}
