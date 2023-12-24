package ru.qwarn.pddexambotclient.bot.botutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageCreator {
    public static SendMessage createExceptionMessage(long chatId, HttpClientErrorException e) {

        ReplyKeyboard keyboard = null;

        if (e.getStatusCode().equals(HttpStatusCode.valueOf(404))) {
            keyboard = createBackToTicketsMarkup();
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(e.getMessage().substring(7, e.getMessage().length() - 1))
                .replyMarkup(keyboard)
                .build();
    }

    private static InlineKeyboardMarkup createBackToTicketsMarkup() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton selectedButton = new InlineKeyboardButton();
        selectedButton.setText("К билетам.");
        selectedButton.setCallbackData("backToTickets");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(selectedButton));
        markup.setKeyboard(buttons);
        return markup;
    }

}
