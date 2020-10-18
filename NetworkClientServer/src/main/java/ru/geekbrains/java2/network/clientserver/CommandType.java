package ru.geekbrains.java2.network.clientserver;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    AUTH_TIMEOUT,
    PRIVATE_MESSAGE,
    PUBLIC_MESSAGE,
    INFO_MESSAGE,
    ERROR,
    END,
    UPDATE_USER_LIST
}
