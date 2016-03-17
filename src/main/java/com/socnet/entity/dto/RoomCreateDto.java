package com.socnet.entity.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class RoomCreateDto {

    @NotNull
    @NotEmpty
    private Set<String> usersId;

    @Size(max = 200)
    private String title;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 1000)
    private String message;
}
