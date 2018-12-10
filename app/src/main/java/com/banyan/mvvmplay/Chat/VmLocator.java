
package com.banyan.mvvmplay.Chat;

import com.banyan.mvvmplay.ChatItems.VmChatItemMessage;

import java.util.HashMap;

/**
 * This class provides a central app-wide repository of all view-models.
 * All VMs should be initialized through this class.
 */
public class VmLocator {

    private final static Object viewModelsLock = new Object();
    private static ViewModels viewModels = new ViewModels();
    private static HashMap<String, VmChat> vmChatMap = new HashMap<>();

    private static class ViewModels {
        private final Object callLogViewModelLock = new Object();
        private VmChat callLogViewModel;
    }

    private static ViewModels getViewModels() {
        synchronized (viewModelsLock) {
            if (viewModels == null) {
                viewModels = new ViewModels();
            }
            return viewModels;
        }
    }

    // Should move to id based logic
    public static VmChat getVmChat(String chatId) {
        if(!vmChatMap.containsKey(chatId)) {
            vmChatMap.put(chatId, new VmChat(chatId));
        }

        return vmChatMap.get(chatId);
    }
}
