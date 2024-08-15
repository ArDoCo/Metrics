package edu.kit.kastel.mcse.ardoco.metrics.rest.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun redirectToSwagger(): String {
        return "redirect:/swagger-ui/index.html"
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }
}
