package com.ibereciartua.commons.domain;

import java.time.LocalDateTime;

public record Song(
        String id,
        String title,
        String artist,
        String album,
        String picture,
        int bpm,
        LocalDateTime playedDate,
        int duration
) {}