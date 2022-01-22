package com.example.springWebContent.domain;

import com.example.springWebContent.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private boolean isActive;

    private String name;

    private String surname;

    private LocalDate dateOfBirth;

    @Lob
    private String description;

    public User(String username, String password, String name, String surname, LocalDate dateOfBirth, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public int getAge() {
        return (Period.between(dateOfBirth, LocalDate.now())).getYears();
    }

    public boolean isUser() {return role == Role.USER; }

    public boolean isAdmin(){
        return role == Role.ADMIN;
    }

    public boolean isOwner() {return  role == Role.OWNER; }

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Lob
    private String image;

    public User(){
        setRole(Role.USER);
    }

    @ElementCollection
    private Set<Invite> invites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet<>();

    @OneToOne(cascade = {CascadeType.ALL})
    private Settings settings;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> friends = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Invite> getInvites() {
        return invites;
    }

    public void setInvites(Set<Invite> invites) {
        this.invites = invites;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(description, user.description) && role == user.role && Objects.equals(image, user.image) && Objects.equals(invites, user.invites) && Objects.equals(notifications, user.notifications) && Objects.equals(settings, user.settings) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isActive, name, surname, dateOfBirth, description, role, image, invites, notifications, settings, friends);
    }
}
