package com.kamesuta.mc.signpic.information;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandDownloadLatest extends CommandBase {
	private static final boolean ENABLED = true;

	@Override
	public String getCommandName() {
		return "signpic-download-latest";
	}

	@Override
	public String getCommandUsage(final ICommandSender var1) {
		return "/signpic-download-latest";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public void processCommand(final ICommandSender var1, final String[] var2) {
		if(!ENABLED)
			var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		else
			if (InformationChecker.doneChecking && InformationChecker.onlineVersionRemote!=null && !InformationChecker.onlineVersionRemote.isEmpty())
			{
				if(InformationChecker.downloadedFile)
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadedAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else if(InformationChecker.startedDownload)
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadingAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else new ThreadDownloadMod();
			}
	}
}