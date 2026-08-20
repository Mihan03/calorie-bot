package com.caloriebot.userservice.model.entity;

import com.caloriebot.userservice.model.enums.UserStates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_states")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserStateEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private UserStates state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStateEntity other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
