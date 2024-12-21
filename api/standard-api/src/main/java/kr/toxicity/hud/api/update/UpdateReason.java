package kr.toxicity.hud.api.update;

/**
 * Returns update reason.
 */
public enum UpdateReason {
    /**
     * None
     */
    EMPTY,
    /**
     * For Bukkit
     */
    BUKKIT_EVENT,
    /**
     * For Velocity
     */
    VELOCITY_EVENT,
    /**
     * For Fabric
     */
    FABRIC_EVENT,
    /**
     * Unknown
     */
    OTHER
}
