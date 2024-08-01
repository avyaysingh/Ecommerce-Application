package com.avyay.e_commerce.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String name;
    private String city;
    private String street;
    private String state;
    private Integer postalCode;
    private String country;
    private Long phone;
}
