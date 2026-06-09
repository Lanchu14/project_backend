package com.paytrack.expense.model;

import lombok.Data;

@Data
public class NotificationRequest {

    private String username;
    private String message;
}