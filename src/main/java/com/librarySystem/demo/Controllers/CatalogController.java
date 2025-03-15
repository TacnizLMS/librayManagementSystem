package com.librarySystem.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.librarySystem.demo.Models.Catalog;
import com.librarySystem.demo.Services.CatalogService;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    // Get all catalogs
    @GetMapping("/all")
    public List<Catalog> getAllCatalogs() {
        return catalogService.getAllCatalogs();
    }

    // Get catalog by id
    @GetMapping("/id/{id}")
    public Optional<Catalog> getCatalogById(@PathVariable String id) {
        return catalogService.getCatalogById(id);
    }


    // Add catalog
    @PostMapping("/add")
    public Catalog addCatalog(@RequestBody Catalog catalog) {
        return catalogService.addCatalog(catalog);
    }

    // Update catalog
    @PutMapping("/update/{id}")
    public Catalog updateCatalog(@PathVariable String id, @RequestBody Catalog catalogDetails) {
        return catalogService.updateCatalog(id, catalogDetails);
    }

    // Delete catalog
    @DeleteMapping("/delete/{id}")
    public void deleteCatalog(@PathVariable String id) {
        catalogService.deleteCatalog(id);
    }
}
