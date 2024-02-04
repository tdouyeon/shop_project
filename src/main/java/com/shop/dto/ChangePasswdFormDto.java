package com.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswdFormDto {

    private String email;
    @NotNull(message = "변경하실 비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 16, message = "비밀번호를 8자 이상, 16자 이하로 입력해 주세요.")
    private String password;
}
