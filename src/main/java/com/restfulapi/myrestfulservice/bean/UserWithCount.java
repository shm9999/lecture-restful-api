package com.restfulapi.myrestfulservice.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserWithCount {
    private int count;
    private List<User> users;
}
