package com.alisound;

import net.runelite.client.config.*;

@ConfigGroup("alisound")
public interface AliSoundConfig extends Config
{
    @ConfigItem(
            keyName = "enabled",
            name = "Enable Plugin",
            description = "Enable or disable the Ali sound effect"
    )
    default boolean enabled()
    {
        return true;
    }

    @ConfigItem(
            keyName = "cooldown",
            name = "Cooldown (ms)",
            description = "Minimum time between sound plays"
    )
    default int cooldown()
    {
        return 3000;
    }
}
