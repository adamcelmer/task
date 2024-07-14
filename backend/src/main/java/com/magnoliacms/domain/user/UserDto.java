package com.magnoliacms.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.jboss.resteasy.reactive.RestForm;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @RestForm
    @NotBlank
    @Email
    private String email;

    @RestForm
    @Length(min = 8, max = 64)
    @NotBlank
    private String password;
}

