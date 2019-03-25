package cn.kuroneko.demo.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Person {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String longitude;
    private String latitude;
}
