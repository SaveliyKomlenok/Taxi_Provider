package com.software.modsen.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private Map<String, List<String>> attributes;
    private Long createdTimestamp;
    private boolean enabled;
    private boolean totp;
    private List<String> disableableCredentialTypes;
    private List<String> requiredActions;
    private Long notBefore;
    private Access access;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Access {
        private boolean manageGroupMembership;
        private boolean view;
        private boolean mapRoles;
        private boolean impersonate;
        private boolean manage;
    }
}
