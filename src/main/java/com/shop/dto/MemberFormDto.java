package com.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 16, message = "비밀번호를 8자 이상, 16자 이하로 입력해 주세요.")
    private String password;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;

    @NotBlank(message = "상세 주소를 입력해 주세요.")
    private String address_detail;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String tel;

    @NotNull(message = "필수 이용약관에 동의해주세요.")
    private String[] chk;
}
