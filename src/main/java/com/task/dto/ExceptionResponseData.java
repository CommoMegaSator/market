package com.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonPropertyOrder({"errorCode", "errorMessage"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ExceptionResponseData(String errorCode, String errorMessage) {}
