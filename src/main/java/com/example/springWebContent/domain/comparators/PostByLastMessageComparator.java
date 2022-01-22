package com.example.springWebContent.domain.comparators;

import com.example.springWebContent.domain.Post;

import java.util.Comparator;

public class PostByLastMessageComparator implements Comparator<Post> {
    @Override
    public int compare(Post o1, Post o2) {
        try {
            return -(o1.getDateOfPosting().compareTo(o2.getDateOfPosting()));
        } catch (Exception e){
            return 0;
        }
    }
}
