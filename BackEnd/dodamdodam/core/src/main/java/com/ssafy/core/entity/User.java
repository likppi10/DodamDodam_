package com.ssafy.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysema.commons.lang.Assert;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPk;

    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    @Column(nullable = false, length = 10)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 읽기 X
    @Column(nullable = false)
    private String password;

    @Nullable
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Nullable
    private String fcmToken;

    @Nullable
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String authority;

    @PrePersist
    public void setAuthority() {
        this.authority = "ROLE_USE";
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * userPk는 DB에서 AI로 입력되는 값임 -> build로 입력되면 안됨.
     * 하지만 Authentication에 UserPk 정보가 담기고,
     * test로직을 돌리기 위해서는 Authentication이 필요함.
     * Authentication을 test에서 사용하기위해 임의로 만드는 방법을 찾았으나
     * 찾지 못하여 builder에 userPk를 넣는 방법을 선택하였음.
     */
    @Builder
    public User(@Nullable Long userPk, String userId, String name, String password,
                @Nullable LocalDate birthday, @Nullable String fcmToken) {
        Assert.hasText(userId, "userId must be not null");
        Assert.hasText(name, "name must be not null");
        Assert.hasText(password, "password must be not null");

        this.userPk = userPk;
        this.userId = userId;
        this.name = name;
        this.password = password;

        this.birthday = birthday;
        this.fcmToken = fcmToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.userPk);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
