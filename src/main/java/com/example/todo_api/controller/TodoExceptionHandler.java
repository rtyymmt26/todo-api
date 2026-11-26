package com.example.todo_api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.todo_api.exception.TodoNotFoundException;

/**
 * Controller から漏れ出た例外を、HTTP ステータスに変換します。
 *
 * <p>{@code @RestControllerAdvice} は、すべての Controller に共通のエラー処理です。
 * Service は「見つからない」「title が空」とだけ伝え、
 * それを 404 や 400 にするのはここ（Web の層）の仕事です。
 */
@RestControllerAdvice
public class TodoExceptionHandler {

    /** 指定 ID が無いとき。404 Not Found */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(TodoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    /** title が空のときなど、リクエストの内容が不正なとき。400 Bad Request */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }
}
