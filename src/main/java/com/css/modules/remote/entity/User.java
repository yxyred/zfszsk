package com.css.modules.remote.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class User {

    @JsonProperty("user_id")
    private Long userId;
    private String name;
    private String phone;
}
