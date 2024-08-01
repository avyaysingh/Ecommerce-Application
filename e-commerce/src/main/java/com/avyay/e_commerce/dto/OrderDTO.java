package com.avyay.e_commerce.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
    private Long orderId;
    // private String user;
    @Builder.Default
    private List<OrderItemDTO> orderItems = new ArrayList<>();
}
