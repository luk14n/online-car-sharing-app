package com.lukian.onlinecarsharing.telegram;

import com.lukian.onlinecarsharing.exception.TelegramBotInitException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final CarSharingBot carSharingBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(carSharingBot);
        } catch (TelegramApiException e) {
            throw new TelegramBotInitException("Failed to initialize your bot.");
        }
    }
}
