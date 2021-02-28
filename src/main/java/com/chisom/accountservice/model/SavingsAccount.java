package com.chisom.accountservice.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 *@author Chisom.Iwowo
 */
@Entity
@Getter
@Setter
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private int accountNumber;

    @Column(unique = true, nullable = false)
    private Long userId;

    /**
     * user username is the email
     */
    @Email(message = "please enter a valid email")
    @Column(unique = true)
    private String username;

    private BigDecimal accountBalance;

    private boolean block;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
