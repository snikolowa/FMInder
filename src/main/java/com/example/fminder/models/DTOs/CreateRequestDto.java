package com.example.fminder.models.DTOs;

import lombok.Data;

@Data
public class CreateRequestDto {

    private int receiverUserId;

    public CreateRequestDto() {
    }

    public CreateRequestDto(int receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

}
