package rotmg.ui;

import java.util.ArrayList;
import java.util.List;

import alde.flash.utils.Vector;
import flash.display.Sprite;
import rotmg.GameSprite;
import rotmg.util.TextKey;

public class Options extends Sprite {

	public static final int Y_POSITION = 550;
	public static final String CHAT_COMMAND = "chatCommand";
	public static final String CHAT = "chat";
	public static final String TELL = "tell";
	public static final String GUILD_CHAT = "guildChat";
	public static final String SCROLL_CHAT_UP = "scrollChatUp";
	public static final String SCROLL_CHAT_DOWN = "scrollChatDown";
	private static final Vector<String> TABS = new Vector<>(TextKey.OPTIONS_CONTROLS, TextKey.OPTIONS_HOTKEYS, TextKey.OPTIONS_CHAT, TextKey.OPTIONS_GRAPHICS, TextKey.OPTIONS_SOUND, TextKey.OPTIONS_FRIEND, TextKey.OPTIONS_MISC);
	private static List<String> registeredCursors = new ArrayList<>(0);

	public Options(GameSprite gs) {
	}

	// Unimplemented
	public static void refreshCursor() {
	}
}
