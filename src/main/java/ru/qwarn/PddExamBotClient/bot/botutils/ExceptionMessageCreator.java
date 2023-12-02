package ru.qwarn.PddExamBotClient.bot.botutils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class ExceptionMessageCreator {
    public SendMessage createExceptionMessage(long chatId, Exception e){
        return SendMessage.builder()
                .chatId(chatId)
                .text(e.getMessage().substring(7, e.getMessage().length() - 1))
                .replyMarkup(createExceptionMarkup())
                .build();
    }

    private InlineKeyboardMarkup createExceptionMarkup(){
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
