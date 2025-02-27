package com.logi_manage.auth_user_service.entity;

import com.logi_manage.auth_user_service.constant.Role;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    //이메일
    @Column(nullable = false, unique = true, length = 32)
    private String email;

    //패스워드
    @Column(nullable = false, length = 60)
    private String password;

    //이름
    @Column(nullable = false, length = 5)
    private String name;

    //권한
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Users (SignUpReq signUpReq, BCryptPasswordEncoder passwordEncoder) {
        this.email = signUpReq.email();
        this.password = passwordEncoder.encode(signUpReq.password());
        this.name = signUpReq.name();
        this.role = signUpReq.role();
    }
}
