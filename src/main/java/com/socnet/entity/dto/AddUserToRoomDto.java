package com.socnet.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
//todo remove this dto
public class AddUserToRoomDto {
    private Set<String> usersIds;
}
