package com.environment.environment.entity;


import com.environment.environment.dto.AuthDto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username; // Principal
    private String password; // Credential
    private String name;
    private String nickname;
    private Date birthdate;
    private String address;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();

    // == 생성 메서드 == //
    public static User registerUser(AuthDto.SignupDto signupDto) {
        User user = new User();

        user.username = signupDto.getUsername();
        user.password = signupDto.getPassword();
        user.name = signupDto.getName();
        user.nickname = signupDto.getNickname();
        user.phone = signupDto.getPhone();
        user.role = Role.USER;

        return user;
    }
    public void addBoard(Board board) {
        this.boards.add(board);
        board.setUser(this); // 보드의 사용자 정보를 설정합니다.
    }


}
