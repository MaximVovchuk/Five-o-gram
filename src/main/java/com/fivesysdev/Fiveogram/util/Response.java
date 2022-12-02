package com.fivesysdev.Fiveogram.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Response<T> implements Serializable {
    private HttpStatus status = HttpStatus.OK;
    private LocalDateTime createdAt = LocalDateTime.now();
    private T data = null;
    public Response(T data) {
        this.data = data;
    }
}
