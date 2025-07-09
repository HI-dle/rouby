package com.rouby.user.application;

public record SendEmailCommand(
    String to,
    String subject,
    String text
) {

}
