package com.example.springWebContent.domain.comparators;

import com.example.springWebContent.domain.Chat;
import com.example.springWebContent.domain.Message;

import java.util.Comparator;

public class ChatByLastMessageComparator<C> implements Comparator<Chat> {
    @Override
    public int compare (Chat c1, Chat c2) {
        Message firstMessage = c1.getLastMessage();
        Message secondMessage = c2.getLastMessage();

        try {
           return firstMessage.getDateOfSending().compareTo(secondMessage.getDateOfSending());
        } catch (NullPointerException e) {
            return 0;
        }
    }
}
