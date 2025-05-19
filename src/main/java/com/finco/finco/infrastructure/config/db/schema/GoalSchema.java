package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Goals")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GoalSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserSchema user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_amount", precision = 10, scale = 2, nullable = false)
    private Long targetAmount;

    @Column(name = "deadline")
    private LocalDate deadLine;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(name = "saved_amount", 
            precision = 10, scale = 2, 
            columnDefinition = "DEFAULT 0.00", 
            nullable = false)
    private Long savedAmount;

    @OneToMany(mappedBy = "goal")
    private List<TransactionSchema> transactions;

}
