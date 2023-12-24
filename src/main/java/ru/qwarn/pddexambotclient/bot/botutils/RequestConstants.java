package ru.qwarn.pddexambotclient.bot.botutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestConstants {
    public static final String START_URI = "http://localhost:8080/bot/api/start/%s";
    public static final String QUESTION_URI = "http://localhost:8080/bot/api/nextQuestion/%s";
    public static final String TICKET_URI = "http://localhost:8080/bot/api/ticket/%s/%s";
    public static final String SELECTED_URI = "http://localhost:8080/bot/api/selectedQuestions/%s";
    public static final String ANSWER_URI = "http://localhost:8080/bot/api/getAnswer/%s?answer=%s";
    public static final String ADD_TO_SELECTED_URI = "http://localhost:8080/bot/api/addToSelected/%s/%s";
    public static final String REMOVE_FROM_SELECTED_URI = "http://localhost:8080/bot/api/removeFromSelected/%s/%s";
    public static final String TICKETS_URI = "http://localhost:8080/bot/api/tickets/%s";
    public static final String TICKET_NEXT_PARAM = "?next=true";

}
