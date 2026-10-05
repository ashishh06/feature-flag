package com.zyop.featureFlag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flag_rules")
@Data
@NoArgsConstructor
public class FlagRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_id", nullable = false)
    private Long flagId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType type;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Integer priority;

    public FlagRule(Long flagId, RuleType type, String value, Integer priority) {
        this.flagId = flagId;
        this.type = type;
        this.value = value;
        this.priority = priority;
    }

    public enum RuleType {
        PERCENTAGE,    // value = "50" means 50% of users
        USER_ID,       // value = "user123" means specific user
        GROUP          // value = "beta-testers" means group of users
    }
}
