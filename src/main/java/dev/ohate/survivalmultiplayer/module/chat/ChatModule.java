package dev.ohate.survivalmultiplayer.module.chat;

import dev.jorel.commandapi.CommandAPI;
import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.chat.command.MessageCommand;
import dev.ohate.survivalmultiplayer.module.chat.command.ReplyCommand;
import dev.ohate.survivalmultiplayer.module.chat.handler.MessageHandler;
import dev.ohate.survivalmultiplayer.module.chat.listener.ChatListener;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.List;

public class ChatModule extends Module {

    @Getter
    private static ChatModule instance;

    public ChatModule() {
        instance = this;

        List.of(
                "message",
                "msg",
                "whisper",
                "w",
                "tell"
        ).forEach(command -> CommandAPI.unregister(command, true));
    }

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Chat";
    }

    @Override
    public String getConfigFileName() {
        return "chat";
    }

    @Override
    public List<Handler> getHandlers() {
        return List.of(new MessageHandler());
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(new ChatListener());
    }

    @Override
    public List<Class<?>> getCommands() {
        return List.of(MessageCommand.class, ReplyCommand.class);
    }

}
