package ru.qwarn.pddexambotclient.bot.botutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.List;

import static ru.qwarn.pddexambotclient.bot.constants.CallbackConstants.*;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageCreator {
    public static SendMessage createExceptionMessage(long chatId, HttpClientErrorException e) {
        String text = StringUtils.substringBetween(e.getMessage(), "message\":\"", "\",\"path");

        if (e.getStatusCode().value() == 404) {
            return createExceptionMessage(chatId, text, createInlineKeyboard("К билетам", GET_TICKETS));
        }
        return createExceptionMessage(chatId, text, null);
    }

    private static SendMessage createExceptionMessage(long chatId, String text, ReplyKeyboard markup){
        return SendMessage.builder().chatId(chatId).text(text).replyMarkup(markup).build();
    }

    private static InlineKeyboardMarkup createInlineKeyboard(String text, String callback){
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder().text(text).callbackData(callback).build()))
                .build();
    }


}
