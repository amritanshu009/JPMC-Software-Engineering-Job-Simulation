package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_record")
public class UserRecord {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float balance;

    protected UserRecord() {}

    public UserRecord(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance='%f']", id, name, balance);
    }
}
