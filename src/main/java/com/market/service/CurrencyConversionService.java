package com.market.service;


import com.market.client.FixerClient;
import com.market.dto.FixerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final FixerClient fixerClient;

    @Value("${fixer.apiKey}")
    private String fixerApiKey;

    public BigDecimal convertCurrency(String from, String to, BigDecimal amount) {
        FixerResponse fixerResponse = fixerClient.getLatestCurrency(fixerApiKey);
        BigDecimal rate = fixerResponse.rates().get(from);
        return amount.divide(rate, 6, RoundingMode.HALF_UP);
    }
}

