package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_record")
public class TransactionRecord {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;  // NEW FIELD

    protected TransactionRecord() {} // JPA needs this

    // ← Put THIS constructor here
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    // Getters (add setters only if you need them)
    public Long getId() { return id; }
    public UserRecord getSender() { return sender; }
    public UserRecord getRecipient() { return recipient; }
    public float getAmount() { return amount; }
    public float getIncentive() { return incentive; }
}
