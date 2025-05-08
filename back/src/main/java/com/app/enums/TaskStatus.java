package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskStatus {
    WAITING("Ожидание"),
    WORK("В работе"),
    DONE("Выполнено"),
    ;

    private final String name;
}

