package ru.qwarn.pddexambotclient.bot.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public final class RequestConstants {
    private static final int PORT = 8080;
    private static final String HOST = "telegram-bot-api";
    private static final String SERVER_URL = "http://" + HOST + ":" + PORT;
    public static final String GET_MORE_TICKETS_URI = "?next=true";
    public static final  String START_TASK_URI = SERVER_URL + "/api/%s/set/task";
    public static final String GET_CORRECT_ANSWER_URI = SERVER_URL + "/api/correctAnswer/%s";
    public static final String START_BOT_URI = SERVER_URL + "/api/start/%s";
    public static final String GET_QUESTION_URI = SERVER_URL + "/api/nextQuestion/%s";
    public static final String CHECK_ANSWER_URI = SERVER_URL + "/api/getAnswer/%s?answer=%s";
    public static final String ADD_TO_SELECTED_URI = SERVER_URL + "/api/addToSelected/%s/%s";
    public static final String REMOVE_FROM_SELECTED_URI = SERVER_URL + "/api/removeFromSelected/%s/%s";
    public static final String GET_TICKETS_URI = SERVER_URL + "/api/tickets/%s";

}
