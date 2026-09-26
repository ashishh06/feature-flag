package com.zyop.featureFlag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_key", unique = true, nullable = false)
    private String key;

    private String name;

    private boolean enabled;

    // JPA requires a no-args constructor
//    public Flag() {}
//
    public Flag(String key, String name, boolean enabled) {
        this.key = key;
        this.name = name;
        this.enabled = enabled;
    }

    // getters and setters — JPA needs these to read/write fields
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getKey() { return key; }
//    public void setKey(String key) { this.key = key; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public boolean isEnabled() { return enabled; }
//    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
