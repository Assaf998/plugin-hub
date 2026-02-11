package com.alisound;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.Client;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

@Slf4j
@PluginDescriptor(
        name = "Ali Sound",
        description = "Plays a sound when talking to any NPC named Ali",
        tags = {"ali", "sound"}
)
public class AliSoundPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private AliSoundConfig config;

    private long lastPlayed = 0;

    @Provides
    AliSoundConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AliSoundConfig.class);
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event)
    {
        if (!config.enabled())
        {
            return;
        }

        if (event.getScriptId() != 2316)
        {
            return;
        }

        Widget nameWidget = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);

        if (nameWidget == null)
        {
            return;
        }

        String npcName = nameWidget.getText();

        if (npcName == null)
        {
            return;
        }

        if (npcName.toLowerCase().contains("ali"))
        {
            playSound();
        }
    }

    private void playSound()
    {
        if (System.currentTimeMillis() - lastPlayed < config.cooldown())
        {
            return;
        }

        lastPlayed = System.currentTimeMillis();

        try
        {
            InputStream in = getClass().getResourceAsStream("/ali.wav");
            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(new BufferedInputStream(in));

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }
        catch (Exception e)
        {
            log.error("Error playing Ali sound", e);
        }
    }
}
