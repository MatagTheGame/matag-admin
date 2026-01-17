package com.matag.admin.exception

import lombok.Data

@Data
data class ErrorResponse(
    var error: String = ""
)
