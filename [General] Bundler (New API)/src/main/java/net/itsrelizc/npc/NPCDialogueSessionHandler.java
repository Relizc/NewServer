package net.itsrelizc.npc;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * Generic handler for NPCDialogueSession and its subclasses.
 * @param <T> the session type extending NPCDialogueSession
 */
public class NPCDialogueSessionHandler<T extends NPCDialogueSession> {

    private final Map<UUID, T> sessions = new HashMap<>();
    private final RelizcNPC npc;
    private final Class<T> sessionClass;

    public NPCDialogueSessionHandler(RelizcNPC npc, Class<T> sessionClass) {
        this.npc = npc;
        this.sessionClass = sessionClass;
    }

    /**
     * Gets or creates a session for the given player.
     * If the current session has expired, it is replaced with a new one.
     *
     * @param player the player to get the session for
     * @param expiresSeconds how long the session lasts
     * @return the active session
     */
    public T getSession(Player player, long expiresSeconds) {
        T session = sessions.get(player.getUniqueId());

        if (session == null || isExpired(session)) {
            session = createNewSession(player, expiresSeconds);
            sessions.put(player.getUniqueId(), session);
        } else {
            session.refreshSession(expiresSeconds);
        }

        return session;
    }

    /**
     * Checks whether the session has expired.
     */
    private boolean isExpired(NPCDialogueSession session) {
        return System.currentTimeMillis() > sessionEndTime(session);
    }

    private long sessionEndTime(NPCDialogueSession session) {
        return session.getEndTime();
    }
    
    /**
     * Returns true if the player has an active (non-expired) session.
     * If the session has expired, it is removed and returns false.
     */
    public boolean hasSession(Player player) {
        T session = sessions.get(player.getUniqueId());
        if (session == null) return false;

        if (isExpired(session)) {
            sessions.remove(player.getUniqueId());
            return false;
        }

        return true;
    }

    /**
     * Creates a new instance of the session type T.
     */
    private T createNewSession(Player player, long expiresSeconds) {
        try {
            return sessionClass
                .getConstructor(Player.class, RelizcNPC.class, long.class)
                .newInstance(player, npc, expiresSeconds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create session instance for " + sessionClass.getName(), e);
        }
    }

    /**
     * Removes the session for a player.
     */
    public void removeSession(Player player) {
        sessions.remove(player.getUniqueId());
    }

    /**
     * Gets all active sessions.
     */
    public Collection<T> getActiveSessions() {
        return sessions.values();
    }

    /**
     * Clears all sessions.
     */
    public void clear() {
        sessions.clear();
    }
}

