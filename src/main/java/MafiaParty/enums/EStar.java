package MafiaParty.enums;

import org.bukkit.ChatColor;

public enum EStar
{
    Blue(ChatColor.AQUA),
    Red(ChatColor.RED),
    Green(ChatColor.GREEN),
    Yellow(ChatColor.YELLOW);

    private final ChatColor chatColor;

    EStar(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public ChatColor getColor() {return this.chatColor;}
}
