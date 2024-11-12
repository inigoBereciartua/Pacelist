package com.ibereciartua.app.domain;

import com.ibereciartua.commons.domain.Song;

import java.util.List;

public record PlaylistProposalResponse(String name, int bpm, int neededDurationInSeconds, List<Song> songs) {}
