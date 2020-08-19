package com.eyxyt.basement.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cd.wang
 * @create 2020-07-25 11:43
 * @since 1.0.0
 */
@Data
public class UserOnline implements Serializable {
    private String id;
    private Long userId;
    private String username;
    private String ip;
    private String status;
    private Date startTimestamp;
    private Date lastAccessTime;
    private Long timeout;
}
