package net.impleri.mobskills.restrictions;

public enum EntitySpawnMode {
    /**
     * Allow the spawn always.
     */
    ALLOW_ALWAYS,

    /**
     * Allow the spawn if any player matches the predicate.
     * If 1 matches, yes === If none match, no
     */
    ALLOW_IF_ANY_MATCH,

    /**
     * Allow the spawn if all players match the predicate.
     * If all match, yes === if 1 doesn't match, no
     */
    ALLOW_IF_ALL_MATCH,

    /**
     * Allow the spawn if any player doesn't match the predicate.
     * if 1 doesn't match, yes === if all match, no
     */
    ALLOW_UNLESS_ANY_MATCH,

    /**
     * Allow the spawn if all players match the predicate.
     * if all doesn't match, yes === if 1 matches, no
     */
    ALLOW_UNLESS_ALL_MATCH,

    /**
     * Deny the spawn if any player match the predicate.
     * if 1 matches, no === If all doesn't match, yes
     */
    DENY_IF_ANY_MATCH,

    /**
     * Deny the spawn if all players match the predicate.
     * if all match, no === if 1 doesn't match, yes
     */
    DENY_IF_ALL_MATCH,

    /**
     * Deny the spawn if no player match the predicate.
     * if none match, no === If 1 matches, yes
     */
    DENY_UNLESS_ANY_MATCH,

    /**
     * Deny the spawn if any player does not match the predicate.
     * if 1 doesn't match, no === If all matches, yes
     */
    DENY_UNLESS_ALL_MATCH,

    /**
     * Deny the spawn always.
     */
    DENY_ALWAYS,
}
