package com.jin.miracle.miracle_like;

import com.jin.miracle.miracle.Miracle;
import com.jin.miracle.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MiracleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Miracle miracle;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    public MiracleLike(Miracle miracle, Users user) {
        this.miracle = miracle;
        this.user = user;
    }
}
