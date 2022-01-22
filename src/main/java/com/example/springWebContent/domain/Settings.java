package com.example.springWebContent.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isMessagingOnlyWithFriends;
    private boolean isPrivateProfile;
    private boolean isGettingNotificationsOnlyFromFriends;
    private boolean isGettingNotificationsFromSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMessagingOnlyWithFriends() {
        return isMessagingOnlyWithFriends;
    }

    public void setMessagingOnlyWithFriends(boolean messagingOnlyWithFriends) {
        isMessagingOnlyWithFriends = messagingOnlyWithFriends;
    }

    public boolean isPrivateProfile() {
        return isPrivateProfile;
    }

    public void setPrivateProfile(boolean privateProfile) {
        isPrivateProfile = privateProfile;
    }

    public boolean isGettingNotificationsOnlyFromFriends() {
        return isGettingNotificationsOnlyFromFriends;
    }

    public void setGettingNotificationsOnlyFromFriends(boolean gettingNotificationsOnlyFromFriends) {
        isGettingNotificationsOnlyFromFriends = gettingNotificationsOnlyFromFriends;
    }

    public boolean isGettingNotificationsFromSite() {
        return isGettingNotificationsFromSite;
    }

    public void setGettingNotificationsFromSite(boolean gettingNotificationsFromSite) {
        isGettingNotificationsFromSite = gettingNotificationsFromSite;
    }
}
