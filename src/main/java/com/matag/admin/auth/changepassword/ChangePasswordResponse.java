package com.matag.admin.auth.changepassword;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangePasswordResponse(@JsonProperty("message")String message) {
}
