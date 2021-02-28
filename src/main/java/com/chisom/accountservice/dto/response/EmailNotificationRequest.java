package com.chisom.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailNotificationRequest {
    @NotBlank(message = "please enter to email")
    private String to;

    @NotBlank(message = "please enter the subject")
    private String subject;

    @NotBlank(message = "please enter the narration")
    private String narration;
}
