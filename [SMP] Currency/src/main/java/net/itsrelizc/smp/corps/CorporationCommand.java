package net.itsrelizc.smp.corps;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.string.StringUtils;

public class CorporationCommand extends RelizcCommand {
	
	public CorporationCommand() {
		super("corporations", "corps");
		this.setAliases(StringUtils.fromArgs("corps", "组织"));
	}

}
