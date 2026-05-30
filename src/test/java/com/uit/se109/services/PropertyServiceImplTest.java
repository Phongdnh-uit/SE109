package com.uit.se109.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import com.uit.se109.enums.PropertyStatus;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.mappers.PropertyMapper;
import com.uit.se109.repositories.PropertyRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

  @Mock private PropertyRepository propertyRepository;
  @Mock private PropertyMapper propertyMapper;

  @InjectMocks private PropertyServiceImpl propertyService;

  private PropertyRequest propertyRequest;
  private Property property;
  private PropertyResponse propertyResponse;

  @BeforeEach
  void setUp() {
    propertyRequest = new PropertyRequest();
    propertyRequest.setTitle("Test Property");
    propertyRequest.setPrice(BigDecimal.valueOf(1000000));
    propertyRequest.setStatus(PropertyStatus.AVAILABLE);

    property = new Property();
    property.setId(1L);
    property.setTitle("Test Property");
    property.setPrice(BigDecimal.valueOf(1000000));
    property.setStatus(PropertyStatus.AVAILABLE);

    propertyResponse = new PropertyResponse();
    propertyResponse.setId(1L);
    propertyResponse.setTitle("Test Property");
    propertyResponse.setPrice(BigDecimal.valueOf(1000000));
    propertyResponse.setStatus(PropertyStatus.AVAILABLE.name());
  }

  @Test
  void shouldCreatePropertySuccessfully() {
    // Arrange
    when(propertyMapper.requestToEntity(any(PropertyRequest.class))).thenReturn(property);
    when(propertyRepository.save(any(Property.class))).thenReturn(property);
    when(propertyMapper.entityToResponse(any(Property.class))).thenReturn(propertyResponse);

    // Act
    PropertyResponse result = propertyService.create(propertyRequest);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("Test Property");
    verify(propertyRepository, times(1)).save(property);
  }

  @Test
  void shouldFindByIdSuccessfully() {
    // Arrange
    when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
    when(propertyMapper.entityToResponse(property)).thenReturn(propertyResponse);

    // Act
    PropertyResponse result = propertyService.findById(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(propertyRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenPropertyNotFoundById() {
    // Arrange
    when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> propertyService.findById(99L))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESOURCE_NOT_FOUND);
  }

  @Test
  void shouldUpdatePropertySuccessfully() {
    // Arrange
    when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
    doNothing().when(propertyMapper).partialUpdate(propertyRequest, property);
    when(propertyRepository.save(property)).thenReturn(property);
    when(propertyMapper.entityToResponse(property)).thenReturn(propertyResponse);

    // Act
    PropertyResponse result = propertyService.update(1L, propertyRequest);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(propertyRepository, times(1)).save(property);
  }

  @Test
  void shouldDeletePropertySuccessfully() {
    // Arrange
    doNothing().when(propertyRepository).deleteById(1L);

    // Act
    propertyService.delete(1L);

    // Assert
    verify(propertyRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldFindAllSuccessfully() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Specification<Property> spec = null;
    Page<Property> propertyPage = new PageImpl<>(List.of(property), pageable, 1);
    PropertySummary propertySummary =
        PropertySummary.builder().id(1L).title("Test Property").build();

    when(propertyRepository.findAll(eq(spec), eq(pageable))).thenReturn(propertyPage);
    when(propertyMapper.entityToSummaryResponse(property)).thenReturn(propertySummary);

    // Act
    PageResponse<PropertySummary> result = propertyService.findAll(pageable, spec);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  void shouldReturnEmptyListWhenNoPropertiesExist() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    Specification<Property> spec = null;
    Page<Property> emptyPage = new PageImpl<>(List.of(), pageable, 0);

    when(propertyRepository.findAll(eq(spec), eq(pageable))).thenReturn(emptyPage);

    // Act
    PageResponse<PropertySummary> result = propertyService.findAll(pageable, spec);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEmpty();
    assertThat(result.getTotalElements()).isEqualTo(0);
  }
}
