package com.software.modsen.authservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
    @GetMapping
    @PreAuthorize("hasRole('client_passenger')")
    public String hello(){
        return "Hello we do this";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('client_driver')")
    public String helloAdmin(){
        return "Hello admin we do this";
    }
}
