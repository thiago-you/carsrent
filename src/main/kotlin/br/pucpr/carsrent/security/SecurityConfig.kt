package br.pucpr.carsrent.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableMethodSecurity
@SecurityScheme(name = "AuthServer", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class SecurityConfig(private val jwtTokenFilter: JwtTokenFilter) {
    @Bean
    fun mvc(introspector: HandlerMappingIntrospector) = MvcRequestMatcher.Builder(introspector)

    @Bean
    fun corsFilter() = CorsConfiguration().apply {
        addAllowedHeader("*")
        addAllowedOrigin("*")
        addAllowedMethod("*")
    }.let {
        UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", it) }
    }.let {
        CorsFilter(it)
    }

    @Bean
    fun filterChain(security: HttpSecurity, mvc: MvcRequestMatcher.Builder): DefaultSecurityFilterChain = security
        .cors(Customizer.withDefaults())
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .headers { it.frameOptions { fo -> fo.disable() } }
        .exceptionHandling { handler -> handler.configureHandler() }
        .authorizeHttpRequests { requests -> requests.configRequest(mvc) }
        .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
        .build()

    private fun ExceptionHandlingConfigurer<*>.configureHandler() {
        authenticationEntryPoint { _, res, ex ->
            res.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                if (ex.message.isNullOrBlank()) "UNAUTHORIZED" else ex.message
            )
        }
    }

    private fun AuthorizeHttpRequestsConfigurer<*>.AuthorizationManagerRequestMatcherRegistry.configRequest(
        mvc: MvcRequestMatcher.Builder
    ) {
        requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET)).permitAll()
        requestMatchers(mvc.pattern(HttpMethod.POST, "/users")).permitAll()
        requestMatchers(mvc.pattern(HttpMethod.POST, "/users/login")).permitAll()
        requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
        anyRequest().authenticated()
    }
}