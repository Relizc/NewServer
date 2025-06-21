package net.itsrelizc.players;

public enum Rank {
	
	NONE(1L, 1L, "§7玩家", false),
	NONE_ADMIN(4L, 1L, "§7柚子社员工", true),
	MOD(2L, 1L, "§a先知", false),
	ADMIN(3L, 300L, "§e管理员", true);
	
	
	public final Long permission;
	public final String displayName;
	public final Boolean useop;
	public final Long level;
	
	/**
	 * Defines a rank.
	 * @param permission The IDENTICAL permission of a player.
	 * @param level The level of a player. This is used for determining a player's power. Generally this value is greater or equal to the permission of a rank.
	 * @return
	 */
	private Rank(Long permission, Long level, String displayName, Boolean canUseOpCommands) {
		this.displayName = displayName;
		this.permission = permission;
		this.useop = canUseOpCommands;
		this.level = level;
	}
	
	public String rankColor() {
		return this.displayName.substring(0, 2);
	}
	
	public static Rank findByPermission(Long value){
	    for (Rank r : values()) {
	        if (r.permission == value) {
	            return r;
	        }
	    }
	    return null;
	}
}
