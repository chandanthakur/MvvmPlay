package com.banyan.mvvmplay.Chat;

/**
 * Type of chat items
 */
public enum ChatItemType {

    Message(1000),

    Album(1001),

    News(1002),

    Attendance(1003),

    Survey(1003);

    private int value;

    private ChatItemType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

