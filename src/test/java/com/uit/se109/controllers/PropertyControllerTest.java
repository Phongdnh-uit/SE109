package com.uit.se109.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se109.configs.AppProperties;
import com.uit.se109.configs.CorsConfig;
import com.uit.se109.configs.SecurityConfig;
import com.uit.se109.configs.SwaggerConfig;
import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import com.uit.se109.enums.PropertyStatus;
import com.uit.se109.securities.filter.PrometheusSecurityFilter;
import com.uit.se109.securities.jwt.CustomJwtConverter;
import com.uit.se109.services.CrudService;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = PropertyController.class, // Đổi tên ở đây
    excludeAutoConfiguration = {SecurityAutoConfiguration.class},
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
              SecurityConfig.class,
              CorsConfig.class,
              SwaggerConfig.class,
              AppProperties.class,
              PrometheusSecurityFilter.class,
              CustomJwtConverter.class
            }))
@AutoConfigureMockMvc(addFilters = false)
class PropertyControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  private CrudService<
          Property, Long, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary>
      propertyService;

  private PropertyRequest propertyRequest;
  private PropertyResponse propertyResponse;
  private PropertySummary propertySummary;

  @BeforeEach
  void setUp() {
    propertyRequest = new PropertyRequest();
    propertyRequest.setTitle("Beautiful Apartment");
    propertyRequest.setPurpose("Residential");
    propertyRequest.setType("Apartment");
    propertyRequest.setPrice(new BigDecimal("500000000"));
    propertyRequest.setLineAddress("123 Main Street");
    propertyRequest.setWardId("ward123");
    propertyRequest.setLandArea(new BigDecimal("100"));
    propertyRequest.setFloorArea(new BigDecimal("80"));
    propertyRequest.setFloors(5);
    propertyRequest.setFloorNumber(3);
    propertyRequest.setBedrooms(3);
    propertyRequest.setBathrooms(2);
    propertyRequest.setEntranceRoadWidth(5.0);
    propertyRequest.setBalconyDirection("East");
    propertyRequest.setDirection("North");
    propertyRequest.setInterior("Full");
    propertyRequest.setDescription("A beautiful apartment in the city center");
    propertyRequest.setStatus(PropertyStatus.AVAILABLE);

    propertyResponse = new PropertyResponse();
    propertyResponse.setId(1L);
    propertyResponse.setTitle("Beautiful Apartment");
    propertyResponse.setPrice(new BigDecimal("500000000"));
    propertyResponse.setStatus("AVAILABLE");

    propertySummary =
        PropertySummary.builder()
            .id(1L)
            .title("Beautiful Apartment")
            .price(new BigDecimal("500000000"))
            .type("Apartment")
            .lineAddress("123 Main Street")
            .landArea(new BigDecimal("100"))
            .bedrooms(3)
            .bathrooms(2)
            .status("AVAILABLE")
            .build();
  }

  @Test
  @DisplayName("GET /api/v1/properties - Find All Properties")
  void testFindAllProperties() throws Exception {
    PageResponse<PropertySummary> pageResponse = new PageResponse<>();
    pageResponse.setPage(0);
    pageResponse.setSize(10);
    pageResponse.setTotalElements(1L);
    pageResponse.setTotalPages(1);
    pageResponse.setNumberOfElements(1);
    pageResponse.setContent(Arrays.asList(propertySummary));

    when(propertyService.findAll(any(), any())).thenReturn(pageResponse);

    mockMvc
        .perform(get("/api/v1/properties").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.totalElements").value(1))
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content[0].title").value("Beautiful Apartment"));

    verify(propertyService, times(1)).findAll(any(), any());
  }

  @Test
  @DisplayName("GET /api/v1/properties - Find All Properties with Filter")
  void testFindAllPropertiesWithFilter() throws Exception {
    PageResponse<PropertySummary> pageResponse = new PageResponse<>();
    pageResponse.setPage(0);
    pageResponse.setSize(10);
    pageResponse.setTotalElements(1L);
    pageResponse.setTotalPages(1);
    pageResponse.setNumberOfElements(1);
    pageResponse.setContent(Arrays.asList(propertySummary));

    when(propertyService.findAll(any(), any())).thenReturn(pageResponse);

    mockMvc
        .perform(
            get("/api/v1/properties?filter=status==ACTIVE").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.content").isArray());

    verify(propertyService, times(1)).findAll(any(), any());
  }

  @Test
  @DisplayName("GET /api/v1/properties - Find All Properties with Pagination")
  void testFindAllPropertiesWithPagination() throws Exception {
    PageResponse<PropertySummary> pageResponse = new PageResponse<>();
    pageResponse.setPage(0);
    pageResponse.setSize(5);
    pageResponse.setTotalElements(20L);
    pageResponse.setTotalPages(4);
    pageResponse.setNumberOfElements(5);
    pageResponse.setContent(Arrays.asList(propertySummary));

    when(propertyService.findAll(any(), any())).thenReturn(pageResponse);

    mockMvc
        .perform(get("/api/v1/properties?page=0&size=5").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.size").value(5))
        .andExpect(jsonPath("$.data.totalPages").value(4));

    verify(propertyService, times(1)).findAll(any(), any());
  }

  @Test
  @DisplayName("GET /api/v1/properties/{id} - Find Property By ID")
  void testFindPropertyById() throws Exception {
    when(propertyService.findById(1L)).thenReturn(propertyResponse);

    mockMvc
        .perform(get("/api/v1/properties/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.title").value("Beautiful Apartment"))
        .andExpect(jsonPath("$.data.status").value("AVAILABLE"));

    verify(propertyService, times(1)).findById(1L);
  }

  @Test
  @DisplayName("POST /api/v1/properties - Create Property")
  void testCreateProperty() throws Exception {
    when(propertyService.create(any(PropertyRequest.class))).thenReturn(propertyResponse);

    mockMvc
        .perform(
            post("/api/v1/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.title").value("Beautiful Apartment"))
        .andExpect(jsonPath("$.data.status").value("AVAILABLE"));

    verify(propertyService, times(1)).create(any(PropertyRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/properties - Create Property with Invalid Title")
  void testCreatePropertyInvalidTitle() throws Exception {
    PropertyRequest invalidRequest = new PropertyRequest();
    invalidRequest.setTitle("");
    invalidRequest.setPrice(new BigDecimal("500000000"));
    invalidRequest.setStatus(PropertyStatus.AVAILABLE);

    mockMvc
        .perform(
            post("/api/v1/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(propertyService, never()).create(any(PropertyRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/properties - Create Property with Invalid Price")
  void testCreatePropertyInvalidPrice() throws Exception {
    PropertyRequest invalidRequest = new PropertyRequest();
    invalidRequest.setTitle("Beautiful Apartment");
    invalidRequest.setPrice(new BigDecimal("-100"));
    invalidRequest.setStatus(PropertyStatus.AVAILABLE);

    mockMvc
        .perform(
            post("/api/v1/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(propertyService, never()).create(any(PropertyRequest.class));
  }

  @Test
  @DisplayName("PUT /api/v1/properties/{id} - Update Property")
  void testUpdateProperty() throws Exception {
    when(propertyService.update(anyLong(), any(PropertyRequest.class)))
        .thenReturn(propertyResponse);

    mockMvc
        .perform(
            put("/api/v1/properties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.title").value("Beautiful Apartment"));

    verify(propertyService, times(1)).update(anyLong(), any(PropertyRequest.class));
  }

  @Test
  @DisplayName("DELETE /api/v1/properties/{id} - Delete Property")
  void testDeleteProperty() throws Exception {
    doNothing().when(propertyService).delete(1L);

    mockMvc
        .perform(delete("/api/v1/properties/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.message").value("success"));

    verify(propertyService, times(1)).delete(1L);
  }

  @Test
  @DisplayName("DELETE /api/v1/properties - Delete Multiple Properties")
  void testDeleteMultipleProperties() throws Exception {
    doNothing().when(propertyService).deleteAll(Arrays.asList(1L, 2L, 3L));

    mockMvc
        .perform(
            delete("/api/v1/properties?ids=1&ids=2&ids=3").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000));

    verify(propertyService, times(1)).deleteAll(Arrays.asList(1L, 2L, 3L));
  }
}
