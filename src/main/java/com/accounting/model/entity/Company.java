package com.accounting.model.entity;

import com.accounting.model.enums.CorporationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
@EqualsAndHashCode(of = "id")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CorporationType corporationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_person_id")
    private ContactPerson contactPerson;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankInfo> bankAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCard> creditCards = new ArrayList<>();
} 