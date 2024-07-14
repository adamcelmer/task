package com.magnoliacms.domain.user;

import com.magnoliacms.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "user_account", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends AuditableEntity {

    private static final long serialVersionUID = 34636324L;

    @Id
    private String id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;
}
