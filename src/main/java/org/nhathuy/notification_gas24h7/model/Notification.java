package org.nhathuy.notification_gas24h7.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Notification {
    private String id;
    private String title;
    private String body;
    private String imageUrl;
    private Date createAt;
    private String createBy;
}
