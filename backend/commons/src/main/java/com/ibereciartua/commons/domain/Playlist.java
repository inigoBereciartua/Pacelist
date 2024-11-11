package com.ibereciartua.commons.domain;

import java.util.List;

public record Playlist(
        String name,
        List<String> songIds
) {}
