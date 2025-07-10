package com.rouby.notification.application.service;

public record CreateFailedEmailLog(
    String address,
    String content
) {

}
