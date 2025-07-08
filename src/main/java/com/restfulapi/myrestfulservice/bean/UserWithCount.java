package com.restfulapi.myrestfulservice.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserWithCount {
    private int count = 0;
    private List<User> users = new ArrayList<>();
}
