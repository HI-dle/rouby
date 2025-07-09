package com.rouby.auth.application.dto.request;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
public record LoginCommand(String email,
                           String password) {}
