package com.currencyconverter.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(schema = "json", name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @Transient
    private String passwordConfirm;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(targetEntity = AuditEntry.class,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema= "json", name = "entry_query",
            joinColumns = @JoinColumn(name = "users_audit_entries"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    public List<AuditEntry> auditEntries;

    @ManyToMany(targetEntity = Role.class,
            cascade = CascadeType.ALL)
    @JoinTable(schema= "json", name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public void addNewAuditEntry(String queryString) {
        AuditEntry newEntry = createNewAuditEntry(queryString);
        if (this.auditEntries.size() == 100)
            this.auditEntries.remove(99);
        this.auditEntries.add(0,newEntry);
    }

    public AuditEntry createNewAuditEntry(String queryString) {
        return new AuditEntry(queryString, new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User(String username) {
    }

    public User() {
    }

    public List<AuditEntry> getAuditEntries() {
        return auditEntries;
    }

    public void setAuditEntries(List<AuditEntry> auditEntries) {
        this.auditEntries = auditEntries;
    }

    public void setAuditEntries(LinkedList<AuditEntry> auditEntries) {
        this.auditEntries = auditEntries;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormattedString() {
        StringBuilder formatted = new StringBuilder();
        getAuditEntries().stream()
                .forEach((p) -> formatted.append(p.getQueryString())
                        .append("<br />"));
        return formatted.toString();

    }
}
