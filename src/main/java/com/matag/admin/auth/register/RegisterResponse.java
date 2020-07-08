package com.matag.admin.auth.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponse(@JsonProperty("message")String message) {
}
