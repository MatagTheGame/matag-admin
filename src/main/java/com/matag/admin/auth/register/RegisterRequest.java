package com.matag.admin.auth.register;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record RegisterRequest(String email, String username, String password) {
}
