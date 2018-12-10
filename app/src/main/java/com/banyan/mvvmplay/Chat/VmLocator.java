
package com.banyan.mvvmplay.Chat;

import java.util.HashMap;

/**
 * This class provides a central app-wide repository of all view-models.
 */
public class VmLocator {

    // chthakur: This can be converted to LinkedHashMap with LRU scheme
    private static HashMap<String, VmChat> vmChatMap = new HashMap<>();

    public static VmChat getVmChat(String chatId) {
        if (!vmChatMap.containsKey(chatId)) {
            vmChatMap.put(chatId, new VmChat(chatId));
        }

        return vmChatMap.get(chatId);
    }
}
