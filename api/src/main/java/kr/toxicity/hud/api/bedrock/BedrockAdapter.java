package kr.toxicity.hud.api.bedrock;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Checks some player is bedrock player.
 */
public interface BedrockAdapter {
    /**
     * Returns whether given uuid's player is from bedrock.
     * @param uuid player's uuid
     * @return whether this player is from bedrock client
     */
    boolean isBedrockPlayer(@NotNull UUID uuid);
}
