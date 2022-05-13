package com.ssafy.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.core.common.ProviderType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPk;

    // 회원아이디(일반:아이디, 소셜회원가입:발급번호)
    @Column(nullable = false, length = 100)
    private String userId;

    @Column(length = 10)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Nullable
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    @Nullable
    private String fcmToken;

    @Nullable
    private String refreshToken;

    @Column(nullable = false, length = 20)
    private String authority;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @PrePersist
    public void prePersist() {
        this.providerType = this.providerType == null ? ProviderType.LOCAL : this.providerType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!Objects.equals(userPk, user.userPk)) return false;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        int result = userPk != null ? userPk.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    /**
     * 소셜 로그인 구동을 위해서는 name, password입력 없이도 만들어져야 함 -> 전체 builder쓰자...
     * userPk는 DB에서 AI로 입력되는 값임 -> build로 입력되면 안됨.
     * 하지만 Authentication에 UserPk 정보가 담기고,
     * test로직을 돌리기 위해서는 Authentication이 필요함.
     * Authentication을 test에서 사용하기위해 임의로 만드는 방법을 찾았으나
     * 찾지 못하여 builder에 userPk를 넣는 방법을 선택하였음.

    Builder
    public User(@Nullable Long userPk, String userId, String name, String password,
                ProviderType providerType, @Nullable LocalDate birthday, @Nullable String fcmToken) {
        Assert.hasText(userId, "userId must be not null");
        Assert.hasText(name, "name must be not null");
        Assert.hasText(password, "password must be not null");

        this.userPk = userPk;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.providerType = providerType;

        this.birthday = birthday;
        this.fcmToken = fcmToken;
    }
     */

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
