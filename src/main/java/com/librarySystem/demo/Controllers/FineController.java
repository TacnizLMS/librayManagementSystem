package com.librarySystem.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarySystem.demo.Services.CatalogService;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    @Autowired
    private CatalogService catalogService;

    // Get all fines for a user
    @GetMapping("user/{userId}")
    public double getFinesByUserId(@PathVariable String userId) {
        return catalogService.getFinesByUserId(userId);
    }

}
