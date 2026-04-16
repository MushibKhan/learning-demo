package com.example.demo.utility;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;




@Service
public class ApiResponseService {

    // ------------------ SUCCESS ------------------
    public <T> ApiResponseDTO<T> success(T data, String message, String path) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .meta(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "path", path
                ))
                .build();
    }

    public <T> ApiResponseDTO<T> success(String message, String path) {
        return success(null, message, path);
    }

    // ------------------ FAILURE ------------------
    public <T> ApiResponseDTO<T> failure(String message, String errorCode, String errorMsg,
    		Map<String, String> fieldErrors, String path ) {

    	return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(
                        ErrorResponseDTO.builder()
                                .code(errorCode)
                                .message(errorMsg)
                                .fields(fieldErrors)
                                .build()
                )
                .meta(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "path", path
                ))
                .build();
    }

    public <T> ApiResponseDTO<T> failure(String errorMsg, String errorCode, String details, String path) {
        return failure(errorMsg, errorCode, details, null, path);
    }
}
