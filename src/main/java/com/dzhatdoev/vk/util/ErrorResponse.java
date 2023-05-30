package com.dzhatdoev.vk.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private LocalDateTime localDateTime;
}