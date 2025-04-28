package com.codingshuttle.project.airBnbApp.dto;

import com.codingshuttle.project.airBnbApp.entity.User;
import com.codingshuttle.project.airBnbApp.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDTO {

    private Long id;

    private User user;

    private String name;

    private Gender gender;

    private Integer age;
}
