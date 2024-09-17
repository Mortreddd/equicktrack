package com.it43.equicktrack.dto.response;

import java.util.Map;

public record ApiResponse(int code, String message, Map<String, Object> data) {
}
