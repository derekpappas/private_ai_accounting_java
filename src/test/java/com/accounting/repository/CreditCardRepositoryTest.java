package com.accounting.repository;

import com.accounting.model.entity.Company;
import com.accounting.model.entity.CreditCard;
import com.accounting.model.enums.CardType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CreditCardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Test
    void findByCompanyId_ShouldReturnCreditCards() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        CreditCard card1 = new CreditCard();
        card1.setCardNumber("1234567890123456");
        card1.setCardholderName("John Doe");
        card1.setExpirationDate("12/25");
        card1.setCvv("123");
        card1.setCardType(CardType.VISA);
        card1.setLast4Digits("1234");
        card1.setCompany(company);
        entityManager.persist(card1);

        CreditCard card2 = new CreditCard();
        card2.setCardNumber("9876543210987654");
        card2.setCardholderName("Jane Doe");
        card2.setExpirationDate("11/24");
        card2.setCvv("456");
        card2.setCardType(CardType.MASTERCARD);
        card2.setLast4Digits("5678");
        card2.setCompany(company);
        entityManager.persist(card2);

        entityManager.flush();

        // When
        List<CreditCard> found = creditCardRepository.findByCompanyId(company.getId());

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(CreditCard::getCardType)
                        .containsExactlyInAnyOrder(CardType.VISA, CardType.MASTERCARD);
    }

    @Test
    void existsByCompanyIdAndLast4Digits_ShouldReturnTrue_WhenExists() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        CreditCard card = new CreditCard();
        card.setCardNumber("1234567890123456");
        card.setCardholderName("John Doe");
        card.setExpirationDate("12/25");
        card.setCvv("123");
        card.setCardType(CardType.VISA);
        card.setLast4Digits("1234");
        card.setCompany(company);
        entityManager.persist(card);
        entityManager.flush();

        // When
        boolean exists = creditCardRepository.existsByCompanyIdAndLast4Digits(
            company.getId(), "1234");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByCompanyIdAndLast4Digits_ShouldReturnFalse_WhenDoesNotExist() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);
        entityManager.flush();

        // When
        boolean exists = creditCardRepository.existsByCompanyIdAndLast4Digits(
            company.getId(), "1234");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistCreditCard() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        CreditCard card = new CreditCard();
        card.setCardNumber("1234567890123456");
        card.setCardholderName("John Doe");
        card.setExpirationDate("12/25");
        card.setCvv("123");
        card.setCardType(CardType.VISA);
        card.setLast4Digits("1234");
        card.setCompany(company);

        // When
        CreditCard saved = creditCardRepository.save(card);
        entityManager.flush();

        // Then
        CreditCard found = entityManager.find(CreditCard.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getCardType()).isEqualTo(CardType.VISA);
        assertThat(found.getLast4Digits()).isEqualTo("1234");
        assertThat(found.getCompany().getId()).isEqualTo(company.getId());
    }

    @Test
    void findByCardType_WhenExists_ShouldReturnCreditCards() {
        // Given
        CreditCard visa = new CreditCard();
        visa.setCardNumber("1234567890123456");
        visa.setCardholderName("John Doe");
        visa.setExpirationDate("12/25");
        visa.setCvv("123");
        visa.setCardType(CardType.VISA);
        visa.setLast4Digits("1234");
        creditCardRepository.save(visa);

        CreditCard mastercard = new CreditCard();
        mastercard.setCardNumber("9876543210987654");
        mastercard.setCardholderName("Jane Doe");
        mastercard.setExpirationDate("11/24");
        mastercard.setCvv("456");
        mastercard.setCardType(CardType.MASTERCARD);
        mastercard.setLast4Digits("5678");
        creditCardRepository.save(mastercard);

        // When
        List<CreditCard> visaCards = creditCardRepository.findByCardType(CardType.VISA);
        List<CreditCard> mastercardCards = creditCardRepository.findByCardType(CardType.MASTERCARD);

        // Then
        assertThat(visaCards).hasSize(1);
        assertThat(mastercardCards).hasSize(1);
        assertThat(visaCards.get(0).getCardType()).isEqualTo(CardType.VISA);
        assertThat(mastercardCards.get(0).getCardType()).isEqualTo(CardType.MASTERCARD);
    }

    @Test
    void findByCardType_WhenNotExists_ShouldReturnEmptyList() {
        // When
        List<CreditCard> amexCards = creditCardRepository.findByCardType(CardType.AMEX);

        // Then
        assertThat(amexCards).isEmpty();
    }

    @Test
    void findByLast4Digits_WhenExists_ShouldReturnCreditCard() {
        // Given
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCardholderName("John Doe");
        creditCard.setExpirationDate("12/25");
        creditCard.setCvv("123");
        creditCard.setCardType(CardType.VISA);
        creditCard.setLast4Digits("9999");
        creditCardRepository.save(creditCard);

        // When
        List<CreditCard> found = creditCardRepository.findByLast4Digits("9999");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getLast4Digits()).isEqualTo("9999");
    }

    @Test
    void findByLast4Digits_WhenNotExists_ShouldReturnEmptyList() {
        // When
        List<CreditCard> found = creditCardRepository.findByLast4Digits("0000");

        // Then
        assertThat(found).isEmpty();
    }
} 