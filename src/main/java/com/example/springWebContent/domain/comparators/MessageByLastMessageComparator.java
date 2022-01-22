package com.example.springWebContent.domain.comparators;

import com.example.springWebContent.domain.Message;

import java.util.Comparator;

public class MessageByLastMessageComparator<C> implements Comparator<Message> {


    @Override
    public int compare(Message m1, Message m2) {
        try{
            if(m1.getDateOfSending().isAfter(m2.getDateOfSending())){
                return 1;
            }
            if(m1.getDateOfSending().isBefore(m2.getDateOfSending())){
                return -1;
            }
        } catch (Exception e){
            return 0;
        }
        return 0;
    }
}
