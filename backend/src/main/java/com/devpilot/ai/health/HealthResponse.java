package com.devpilot.ai.health;

import java.time.Instant;

public record HealthResponse(String status, Instant checkedAt) {
}

