package com.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFindFormDto {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    private String tel;
    private String email;
    //인증 방법
    private String way;
}
