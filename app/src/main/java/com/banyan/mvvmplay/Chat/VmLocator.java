
package com.banyan.mvvmplay.Chat;

import com.banyan.mvvmplay.ChatItems.VmChatItemMessage;

import java.util.HashMap;

/**
 * This class provides a central app-wide repository of all view-models.
 * All VMs should be initialized through this class.
 * TODO: chthakur - support vm cleanup on sign-out/sign-in
 */
public class VmLocator {

    private final static Object viewModelsLock = new Object();
    private static ViewModels viewModels = new ViewModels();
    private static HashMap<String, VmChatItemMessage> chatItemMap = new HashMap<>();

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

    public static VmChat getVmChat() {
        ViewModels viewModelsCopy = getViewModels();
        synchronized (viewModelsCopy.callLogViewModelLock) {
            if (viewModelsCopy.callLogViewModel == null) {
                viewModelsCopy.callLogViewModel = new VmChat();
            }
            return viewModelsCopy.callLogViewModel;
        }
    }
}
