package com.librarySystem.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarySystem.demo.Models.Catalog;
import com.librarySystem.demo.Models.User;
import com.librarySystem.demo.Services.CatalogService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    @Autowired
    private CatalogService catalogService;


    // Get all fines for a user
    @GetMapping("user/{userId}")
    public double getFinesByUserId(@PathVariable String id) {
        return catalogService.getFinesByUserId(id);
    }

}
