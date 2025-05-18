package com.main.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    int uid;
    String name;
    String password;
}
