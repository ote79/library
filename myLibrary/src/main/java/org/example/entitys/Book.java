package org.example.entitys;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Book {
    String bookName;
    String author;
    int bid;
}
