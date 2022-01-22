package com.example.springWebContent.service;

import com.example.springWebContent.domain.*;
import com.example.springWebContent.domain.enums.Role;
import com.example.springWebContent.domain.enums.TypeOfNotification;
import com.example.springWebContent.domain.exceptions.UserDoesNotExistException;
import com.example.springWebContent.repos.InviteRepo;
import com.example.springWebContent.repos.NotificationRepo;
import com.example.springWebContent.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private InviteRepo inviteRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public User authorizeUser(User user) {
        if (user != null) {
            return userRepo.findById(user.getId()).get();
        }
        return null;
    }

    public List<User> findAllUsersExceptUserAndFriends(User user) {

        List<User> users = userRepo.findAll();
        if (user != null) {
            users.remove(userRepo.findByUsername(user.getUsername()));
            users.removeAll(user.getFriends());
        }
        return users;
    }

    public void prepareDescriptionOfUsers(List<User> users) {
        for (int i = 0; i < (long) users.size(); i++) {
            if (users.get(i).getDescription() != null) {
                StringBuilder description = new StringBuilder(users.get(i).getDescription());
                if (description.length() > 100) {
                    description.insert(100, "<span id=\"dots\">...</span><span id=\"more\">");
                    description.append("</span>");
                    users.get(i).setDescription(description.toString());
                }
            }
        }
    }

    public String getFormatterDateOfUser(User user, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(user.getDateOfBirth());
    }

    public void updateProfile(User user, String username, String name, String surname,
                              String description, LocalDate dateOfBirth, MultipartFile image) throws Exception {

        if (!username.isEmpty()) {
            user.setUsername(username);
        }
        if (!name.isEmpty()) {
            user.setName(name);
        }
        if (!surname.isEmpty()) {
            user.setSurname(surname);
        }
        if (!description.isEmpty()) {
            user.setDescription(description);
        }
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth);
        }
        if (!Objects.equals(image.getOriginalFilename(), "")) {
            user.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }
    }

    public void sendInvite(User user, User person) {
        Invite invite = new Invite(user, person);

        inviteRepo.save(invite);
    }

    public void removeInvite(User user, User person) {
        List<Invite> invites = inviteRepo.findAll();
        for (Invite invite :
                invites) {
            if (invite.equals(new Invite(user, person))
                    || invite.equals(new Invite(person, user))) {
                inviteRepo.deleteById(invite.getId());
                System.out.println(user.getUsername() + " REMOVED INVITE " + person.getUsername());
            }
        }
    }

    public void declineInvite(User user, User person) {
        List<Invite> invites = inviteRepo.findAll();
        for (Invite invite :
                invites) {
            if (invite.equals(new Invite(person, user))) {
                inviteRepo.deleteById(invite.getId());
                System.out.println(user.getUsername() + " DECLINED the invite from " + user.getUsername());

                user.getNotifications().removeIf(x -> x.equals(new Notification(TypeOfNotification.FRIEND_REQUEST, person, user)));
                userRepo.save(user);

                List<Notification> notifications = notificationRepo.findAll();
                for (Notification notification :
                        notifications) {
                    if (notification.equals(new Notification(TypeOfNotification.FRIEND_REQUEST, person, user))) {
                        notificationRepo.deleteById(notification.getId());
                        break;
                    }
                }
            }
        }
    }

    public void confirmInvite(User user, User person) {
        List<Invite> invites = inviteRepo.findAll();
        for (Invite invite :
                invites) {
            if (invite.equals(new Invite(person, user))) {
                inviteRepo.deleteById(invite.getId());

                user.getFriends().add(person);
                person.getFriends().add(user);
                userRepo.save(user);
                userRepo.save(person);

                user.getNotifications().removeIf(x -> x.equals(new Notification(TypeOfNotification.FRIEND_REQUEST, person, user)));
                userRepo.save(user);

                List<Notification> notifications = notificationRepo.findAll();
                for (Notification notification :
                        notifications) {
                    if (notification.equals(new Notification(TypeOfNotification.FRIEND_REQUEST, person, user))) {
                        notificationRepo.deleteById(notification.getId());
                        break;
                    }
                }

                System.out.println(user.getUsername() + " CONFIRMED the invite from " + person.getUsername());
                return;
            }
        }
    }

    public void removeFriend(User user, User person) {
        if (user.getFriends().contains(person)) {
            user.getFriends().remove(person);
            person.getFriends().remove(user);
            userRepo.save(user);
            userRepo.save(person);
        }
    }

    public void updateSettings(User user, boolean isMessagingOnlyWithFriends, boolean isPrivateProfile, boolean isGettingNotificationsOnlyFromFriends, boolean isGettingNotificationsFromSite) {
        user.getSettings().setMessagingOnlyWithFriends(isMessagingOnlyWithFriends);
        user.getSettings().setPrivateProfile(isPrivateProfile);
        user.getSettings().setGettingNotificationsOnlyFromFriends(isGettingNotificationsOnlyFromFriends);
        user.getSettings().setGettingNotificationsFromSite(isGettingNotificationsFromSite);
        userRepo.save(user);
    }

    @Transactional
    public boolean isFriend(User user, User person) {
        try {
            return user.getFriends().contains(person) || person.getFriends().contains(user);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean isInviter(User user, User person) {
        List<Invite> invites = inviteRepo.findAll();
        for (Invite invite :
                invites) {
            if (invite.equals(new Invite(user, person))) {
                return true;
            }
        }
        return false;
    }

    public boolean wasInvited(User user, User person) {
        List<Invite> invites = inviteRepo.findAll();
        for (Invite invite :
                invites) {
            if (invite.equals(new Invite(person, user))) {
                return true;
            }
        }
        return false;
    }

    public boolean isAvailableInfo(User user, User person) {
        if (isFriend(user, person)) {
            return true;
        } else {
            return !person.getSettings().isPrivateProfile();
        }
    }

    public boolean isAvailableMessaging(User user, User person) {
        if (user != null) {
            if (isFriend(user, person)) {
                return true;
            } else {
                return !person.getSettings().isMessagingOnlyWithFriends();
            }
        }
        return false;
    }

    public List<User> getByRole(Role role) {
        List<User> users = userRepo.findAll();
        users = users.stream().filter(x -> x.getRole().equals(role)).collect(Collectors.toList());
        return users;
    }

    public User getUserByUsername(String username) throws UserDoesNotExistException {
        User user;
        boolean exist = userRepo.existsByUsername(username);
        if (!exist) {
            throw new UserDoesNotExistException("User with username " + username + " does not exist");
        }

        return userRepo.findByUsername(username);
    }

    public void createAndSaveNewUser(String username, String password, String name, String surname, LocalDate dateOfBirth) {
        User user = new User(username, passwordEncoder.encode(password), name, surname, dateOfBirth, Role.USER);

        user.setActive(true);
        user.setNotifications(new HashSet<>());
        user.setInvites(new HashSet<>());
        user.setSettings(new Settings());
        userRepo.save(user);
    }

    public List<User> getUsersByUsernameLike(String search) {
        return userRepo.getUsersByUsernameLike(search);
    }

    public void setRole(String username, String role) throws UserDoesNotExistException {
        User user;
        if (userRepo.existsByUsername(username)) {
            user = userRepo.findByUsername(username);
            Role newRole = Role.valueOf(role);
            user.setRole(newRole);
            userRepo.save(user);
        } else {
            throw new UserDoesNotExistException();
        }
    }

    public void sortUsersByField(List<User> users, String field, boolean order) {
        switch (field) {
            case "name":
                users.sort(Comparator.comparing(User::getName));
                break;
            case "username":
                users.sort(Comparator.comparing(User::getUsername));
                break;
            case "role":
                users.sort(Comparator.comparing(User::getRole));
                break;
        }
        if (!order) {
            Collections.reverse(users);
        }
    }

    public void sendNotification(TypeOfNotification typeOfNotification, String text, User user, User person, Post post) {
        if (typeOfNotification.equals(TypeOfNotification.NEW_MESSAGE) && person.getSettings().isGettingNotificationsOnlyFromFriends()
                && !person.getFriends().contains(user)) {
            return;
        }

        if (text.length() > 75) {
            text = text.substring(0, 75);
            text += "...";
        }
        Notification newNotification = new Notification(typeOfNotification, text, user, person);
        boolean isNew = true;

        if (typeOfNotification.equals(TypeOfNotification.NEW_MESSAGE)) {
            for (Notification notification :
                    person.getNotifications()) {
                if(notification.getSender().equals(user) && notification.getTypeOfNotification().equals(typeOfNotification)){
                    isNew = false;
                    break;
                }
            }
        } else if (typeOfNotification.equals((TypeOfNotification.FRIEND_REQUEST))) {
            for (Notification notification :
                    person.getNotifications()) {
                if (notification.equals(newNotification)) {
                    isNew = false;
                    break;
                }
            }
        } else if (typeOfNotification.equals(TypeOfNotification.SITE)) {
            List<User> users = userRepo.findAll();
            newNotification.setPost(post);
            for (User dbUser :
                    users) {
                if (dbUser.getSettings().isGettingNotificationsFromSite()) {
                    dbUser = userRepo.getById(dbUser.getId());
                    dbUser.getNotifications().add(newNotification);
                    userRepo.save(dbUser);
                }
            }
            return;
        }

        if (isNew) {
            person.addNotification(newNotification);
            notificationRepo.save(newNotification);
            userRepo.save(person);
        }
    }

    public void removeNotifications(User user, User person) {
        for (Notification notification :
                user.getNotifications()) {
            if (notification.getSender().equals(person) && notification.getTypeOfNotification().equals(TypeOfNotification.NEW_MESSAGE)) {
                user.getNotifications().remove(notification);
                break;
            }
        }
        userRepo.save(user);
    }

    public void filterUsersByUsernameLike(List<User> users, String search, boolean order) {
        users.removeIf(user -> !user.getUsername().contains(search));
        if (!order) {
            Collections.reverse(users);
        }
    }

    public void deleteNotification(User user, String id) {
        Notification deleteNotification = notificationRepo.getById(Long.parseLong(id));
        user.getNotifications().removeIf(notification -> deleteNotification.getId().equals(notification.getId()));
        userRepo.save(user);
    }
}
