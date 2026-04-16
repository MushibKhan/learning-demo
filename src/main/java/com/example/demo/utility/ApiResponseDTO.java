package com.example.demo.utility;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    //	private String responseCode;
    private T data;

    private Map<String, Object> meta;
    // time stamp
    // path

    private ErrorResponseDTO errors;

}