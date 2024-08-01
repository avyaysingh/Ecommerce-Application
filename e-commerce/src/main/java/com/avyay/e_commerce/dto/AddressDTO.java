package com.avyay.e_commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
    @NotNull
    private String name;
    @NotNull
    private String city;
    private String street;
    private String state;

    @NotNull
    private Integer postalCode;
    private String country;

    @NotNull
    private Long phone;
}
