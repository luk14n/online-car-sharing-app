package com.lukian.onlinecarsharing.telegram;

import com.lukian.onlinecarsharing.exception.TelegramBotSendMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class CarSharingBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.chat}")
    private String chatId;

    @Override
    public void onUpdateReceived(Update update) {
        chatId = String.valueOf(update.getMessage().getChatId());
        System.out.println("Your chat id: " + chatId);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (messageText.equals("/start")) {
                sendOnStartCommand(update.getMessage().getChat().getFirstName());
            }
        }
    }

    private void sendOnStartCommand(String name) {
        String message = """
                Hi there, %s, what's good!
                Welcome to CarDash
                                
                Now you will be provided with info messages as:
                - created rentals
                - successful payments
                - overdue rentals
                - overdue warnings
                                
                Drive safe <3
                """.formatted(name);
        sendMessage(message);
    }

    public void sendMessage(String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramBotSendMessageException(
                    "Failed to send the message: " + text, e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
