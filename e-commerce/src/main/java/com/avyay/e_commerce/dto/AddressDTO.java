package com.avyay.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
    private String city;
    private String street;
    private String firstName;
    private String lastName;
}
