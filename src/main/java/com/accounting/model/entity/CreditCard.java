package com.accounting.model.entity;

import com.accounting.model.enums.CardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "credit_cards")
@EqualsAndHashCode(of = "id")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;

    @Column(name = "last_4_digits", length = 4)
    private String last4Digits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();
} 