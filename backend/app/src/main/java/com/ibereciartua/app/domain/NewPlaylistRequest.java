package com.ibereciartua.app.domain;

import java.util.List;

public record NewPlaylistRequest(String name, List<String> songIds) {}
