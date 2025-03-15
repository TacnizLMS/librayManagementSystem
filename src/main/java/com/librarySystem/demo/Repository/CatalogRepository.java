package com.librarySystem.demo.Repository;

import com.librarySystem.demo.Models.Catalog;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogRepository extends MongoRepository<Catalog, String> {
    public abstract List<Catalog> findByBookIds(String bookId);

}
