package com.banyan.mvvmplay.Chat;

/**
 * Wrapper data object passed to each of Chat VMs
 * Only meta data to be added at top level, all the data should go in payload
 */
public class ChatItemSchema {

    public String id;

    public Object payload;
}