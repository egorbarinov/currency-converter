package com.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "jxmlparse_valcurs", name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Transient
    private String passwordConfirm;

    @Column(name = "enabled")
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(targetEntity = AuditEntry.class,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema= "jxmlparse_valcurs", name = "entry_query",
            joinColumns = @JoinColumn(name = "users_audit_entries"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    public List<AuditEntry> auditEntries;

    @Column(name = "activate_code")
    private String activateCode;

    public User(String username) {
        this.username = username;
    }


    public String getFormattedString() {
        StringBuilder formatted = new StringBuilder();
        getAuditEntries().forEach(p -> formatted.append(p.getQueryString())
                .append("<br />"));
        return formatted.toString();
    }
}
