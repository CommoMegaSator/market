package com.market.dto;

import java.math.BigDecimal;
import java.util.Map;

public record FixerResponse(boolean success, long timestamp, String base, Map<String, BigDecimal> rates){}
