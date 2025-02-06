package com.example.schedulerv2.dto;

import lombok.Getter;

@Getter
public class SaveScheduleRequestDto {

    private final String email;

    private final String password;

    private final String title;

    private final String contents;

    public SaveScheduleRequestDto(String email, String password, String title, String contents) {
        this.email = email;
        this.password = password;
        this.title = title;
        this.contents = contents;
    }
}
