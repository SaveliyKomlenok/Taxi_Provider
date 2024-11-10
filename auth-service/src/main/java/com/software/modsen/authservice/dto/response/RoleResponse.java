package com.software.modsen.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
    private Map<String, String> attributes;
}
