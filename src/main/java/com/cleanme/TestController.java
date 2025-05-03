package com.cleanme;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String testCors() {
        return "CORS radi!";
    }
}
