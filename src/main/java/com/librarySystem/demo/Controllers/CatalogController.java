package com.librarySystem.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.librarySystem.demo.Dto.CatalogRequestDTO;
import com.librarySystem.demo.Dto.FinePayBookIdDTO;
import com.librarySystem.demo.Models.Book;
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

    // Get catalog by user id
    @GetMapping("/user/{userId}")
    public List<Catalog> getCatalogByUserId(@PathVariable String userId) {
        return catalogService.getAllCatalogByUserId(userId);
    }

    // Add catalog
    @PostMapping("/add")
    public Catalog addCatalog(@RequestBody CatalogRequestDTO catalog) {
        return catalogService.addCatalog(catalog);
    }

    // Update catalog
    @PutMapping("/update/{id}")
    public Catalog updateCatalog(@PathVariable String id, @RequestBody CatalogRequestDTO catalogDetails) {
        return catalogService.updateCatalog(id, catalogDetails);
    }

    // ruturn back when return by mistake
    @PutMapping("/return-back/{id}")
    public Catalog returnBackCatalog(@PathVariable String id) {
        return catalogService.returnBackCatalog(id);
    }

    // return back when return by mistake with book id
    @PutMapping("/return-back-book/{catalogId}")
    public Catalog returnBackCatalogBook(@PathVariable String catalogId, @RequestBody FinePayBookIdDTO request) {
        return catalogService.returnBackCatalogBook(catalogId, request);
    }

    //pay full catalog fine
    @PutMapping("/pay-catalog-fine/{id}")
    public Catalog payCatalogFine(@PathVariable String id) {
        return catalogService.payCatalogFine(id);
    }

    // Pay single catalog book fine
    @PutMapping("/pay-catalog-book-fine/{id}")
    public Catalog payCatalogBookFine(@PathVariable String id, @RequestBody FinePayBookIdDTO request) {
        return catalogService.payCatalogBookFine(id, request.getCatalogBookId());
    }

    // Delete catalog
    @DeleteMapping("/delete/{id}")
    public String deleteCatalog(@PathVariable String id) {
        catalogService.deleteCatalog(id);
        return "Catalog removed successfully";
    }

}
