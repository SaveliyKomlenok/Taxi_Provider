package com.software.modsen.passengerservice.converter

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.lang.NonNull
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component

@Component
class JwtAuthConverter : Converter<Jwt, AbstractAuthenticationToken> {

    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    @Value("\${jwt.auth.converter.principal-attribute}")
    private val principalAttribute: String? = null

    @Value("\${jwt.auth.converter.resource-id}")
    private val resourceId: String? = null

    @Value("\${jwt.auth.converter.client-resource-id}")
    private val clientResourceId: String? = null

    @NonNull
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities: Collection<GrantedAuthority> = jwtGrantedAuthoritiesConverter.convert(jwt).toSet() + extractResourceRoles(jwt)
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        val claimName = principalAttribute ?: JwtClaimNames.SUB
        return jwt.getClaim(claimName)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess: Map<String, Any> = jwt.getClaim<Map<String, Any>>("resource_access") ?: return emptySet()

        val resource = when {
            resourceAccess[resourceId] != null -> resourceAccess[resourceId] as Map<String, Any>
            resourceAccess[clientResourceId] != null -> resourceAccess[clientResourceId] as Map<String, Any>
            else -> return emptySet()
        }

        val resourceRoles = resource["roles"] as List<String>

        return resourceRoles.map { role -> SimpleGrantedAuthority("ROLE_$role") }.toSet()
    }
}