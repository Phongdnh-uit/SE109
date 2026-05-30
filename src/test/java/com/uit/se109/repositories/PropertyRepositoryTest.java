package com.uit.se109.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.uit.se109.entities.Property;
import com.uit.se109.enums.PropertyStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class PropertyRepositoryTest {

  @Container @ServiceConnection static MySQLContainer mysql = new MySQLContainer("mysql:latest");

  @Autowired private PropertyRepository propertyRepository;

  @BeforeEach
  void setUp() {
    propertyRepository.deleteAll();

    Property p1 = new Property();
    p1.setTitle("Luxury Villa");
    p1.setPrice(new BigDecimal("1000000000"));
    p1.setStatus(PropertyStatus.AVAILABLE);
    p1.setLandArea(new BigDecimal("500"));
    p1.setBedrooms(5);

    Property p2 = new Property();
    p2.setTitle("Modern Apartment");
    p2.setPrice(new BigDecimal("300000000"));
    p2.setStatus(PropertyStatus.AVAILABLE);
    p2.setLandArea(new BigDecimal("100"));
    p2.setBedrooms(2);

    Property p3 = new Property();
    p3.setTitle("Cheap Studio");
    p3.setPrice(new BigDecimal("100000000"));
    p3.setStatus(PropertyStatus.SOLD);
    p3.setLandArea(new BigDecimal("40"));
    p3.setBedrooms(1);

    propertyRepository.saveAll(List.of(p1, p2, p3));
  }

  @Test
  void shouldFindById() {
    List<Property> properties = propertyRepository.findAll();
    Long id = properties.get(0).getId();

    Optional<Property> found = propertyRepository.findById(id);

    assertThat(found).isPresent();
    assertThat(found.get().getId()).isEqualTo(id);
  }

  @Test
  void shouldFindAllWithPaginationAndSorting() {
    PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "price"));

    Page<Property> page = propertyRepository.findAll(pageRequest);

    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getContent()).hasSize(2);
    // Highest price should be first
    assertThat(page.getContent().get(0).getTitle()).isEqualTo("Luxury Villa");
  }

  @Test
  void shouldFilterByTitleSpecification() {
    Specification<Property> spec = (root, query, cb) -> cb.like(root.get("title"), "%Apartment%");

    List<Property> result = propertyRepository.findAll(spec);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Modern Apartment");
  }

  @Test
  void shouldFilterByPriceRangeSpecification() {
    Specification<Property> spec =
        (root, query, cb) ->
            cb.between(root.get("price"), new BigDecimal("150000000"), new BigDecimal("500000000"));

    List<Property> result = propertyRepository.findAll(spec);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Modern Apartment");
  }

  @Test
  void shouldFilterByCombinedSpecification() {
    Specification<Property> priceSpec =
        (root, query, cb) -> cb.greaterThan(root.get("price"), new BigDecimal("200000000"));

    Specification<Property> statusSpec =
        (root, query, cb) -> cb.equal(root.get("status"), PropertyStatus.AVAILABLE);

    List<Property> result =
        propertyRepository.findAll(Specification.where(priceSpec).and(statusSpec));

    assertThat(result).hasSize(2); // Luxury Villa and Modern Apartment
  }
}
