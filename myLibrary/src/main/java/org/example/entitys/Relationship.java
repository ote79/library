package org.example.entitys;

import lombok.Data;

@Data
public class Relationship {
    int bid;
    int uid;
    String bookName;
    String name;
    String author;
    boolean available;
}
