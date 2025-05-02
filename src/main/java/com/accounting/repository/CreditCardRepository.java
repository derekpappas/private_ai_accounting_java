package com.accounting.repository;

import com.accounting.model.entity.CreditCard;
import com.accounting.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findByCompanyId(Long companyId);
    boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits);
    List<CreditCard> findByCardType(CardType cardType);
    List<CreditCard> findByLast4Digits(String last4Digits);
} 