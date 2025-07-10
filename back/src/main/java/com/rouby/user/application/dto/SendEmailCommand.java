package com.rouby.user.application.dto;

public record SendEmailCommand(
    String to,
    String subject,
    String text
) {

}
