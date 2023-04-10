package com.mysite.sbb.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자"),
    SNS("ROLE_SNS", "소셜 로그인"),
    USER("ROLE_USER", "일반 로그인");

    private final String value;
    private final String detail;
}
