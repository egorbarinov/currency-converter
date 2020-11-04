package com.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "valcurs", name = "users")
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(targetEntity = AuditEntry.class,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema= "valcurs", name = "entry_query",
            joinColumns = @JoinColumn(name = "users_audit_entries"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    public List<AuditEntry> auditEntries;

    public void addNewAuditEntry(String queryString) {
        AuditEntry newEntry = createNewAuditEntry(queryString);
        if (this.auditEntries.size() == 100)
            this.auditEntries.remove(99);
        this.auditEntries.add(0,newEntry);
    }

    public AuditEntry createNewAuditEntry(String queryString) {
        return new AuditEntry(queryString, new Date());
    }

    public User(String username) {
        this.username = username;
    }


    public String getFormattedString() {
        StringBuilder formatted = new StringBuilder();
        getAuditEntries().stream()
                .forEach((p) -> formatted.append(p.getQueryString())
                        .append("<br />"));
        return formatted.toString();

    }
}
