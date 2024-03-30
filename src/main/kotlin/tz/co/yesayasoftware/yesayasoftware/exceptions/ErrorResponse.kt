package tz.co.yesayasoftware.yesayasoftware.exceptions

data class ErrorResponse(
    val errors: Map<String, List<String>>
)