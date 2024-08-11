package com.example.tdddemo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDto {

    private String currentPassword;
    private String newName;
}