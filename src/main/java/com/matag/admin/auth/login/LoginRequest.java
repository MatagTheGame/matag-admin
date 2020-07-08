package com.matag.admin.auth.login;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record LoginRequest(String emailOrUsername, String password) {
}
