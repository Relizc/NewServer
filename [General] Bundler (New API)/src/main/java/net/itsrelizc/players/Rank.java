package net.itsrelizc.players;

public enum Rank {
	
	NONE(1L, 1L, "§7柚子社员工", false),
	ADMIN(2L, 300L, "§e管理员", true);
	
	
	public final Long permission;
	public final String displayName;
	public final Boolean useop;
	public final Long level;
	
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
