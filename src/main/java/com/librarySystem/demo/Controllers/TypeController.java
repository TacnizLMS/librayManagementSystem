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

import com.librarySystem.demo.Models.Type;
import com.librarySystem.demo.Services.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypeController {
    @Autowired
    private TypeService typeService;

    // Get all types
    @GetMapping("/all")
    public List<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    // Get type by id
    @GetMapping("/id/{id}")
    public Optional<Type> getTypeById(@PathVariable String id) {
        return typeService.getTypeById(id);
    }
    // Add type
    @PostMapping("/add")
    public Type addType(@RequestBody Type type) {
        return typeService.addType(type);
    }
    // Update type
    @PutMapping("/update/{id}")
    public Type updateType(@PathVariable String id, @RequestBody Type typeDetails) {
        return typeService.updateType(id, typeDetails);
    }
    // Delete type
    @DeleteMapping("/delete/{id}")
    public String deleteType(@PathVariable String id) {
        typeService.deleteType(id);
        return "Type removed successfully";
    }

}
