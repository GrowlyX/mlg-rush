package com.solexgames.mlg.duel;

import com.solexgames.mlg.state.impl.Arena;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DuelRequest {

    public static final long EXPIRATION_MILLI = 60_000L;

    private final UUID id;

    private final UUID issuer;
    private final UUID target;
    private final long issuingTime;

    private final String targetDisplay;
    private final String issuerDisplay;

    private final Arena arena;

}
