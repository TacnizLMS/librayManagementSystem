package com.librarySystem.demo.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.librarySystem.demo.Models.Catalog;
import com.librarySystem.demo.Repository.CatalogRepository;




@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> getCatalogById(String id) {
        return catalogRepository.findById(id);
    }
    
    public Catalog addCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
        }

        public Catalog updateCatalog(String id, Catalog catalogDetails) {
        return catalogRepository.findById(id).map(catalog -> {
            catalog.setUserId(catalogDetails.getUserId());
            catalog.setBookIds(catalogDetails.getBookIds());
            catalog.setQuantity(catalogDetails.getQuantity());
            catalog.setBorrowDate(catalogDetails.getBorrowDate());
            catalog.setExpiredDate(catalogDetails.getExpiredDate());
            return catalogRepository.save(catalog);
        }).orElseThrow(() -> new RuntimeException("Catalog not found"));
        }

        public void deleteCatalog(String id) {
        catalogRepository.deleteById(id);
    }
}
