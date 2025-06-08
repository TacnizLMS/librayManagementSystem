package com.librarySystem.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.librarySystem.demo.Exception.AlreadyExistsException;
import com.librarySystem.demo.Models.Type;
import com.librarySystem.demo.Repository.TypeRepository;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Optional<Type> getTypeById(String id) {
        return typeRepository.findById(id);
    }

    public Type addType(Type type) {
        Optional<Type> existingType = typeRepository.findByName(type.getName());
        if (existingType.isPresent()) {
            throw new AlreadyExistsException("Type with Name '" + type.getName() + "' already exists.");
        }
        return typeRepository.save(type);
    }
    public Type updateType(String id, Type typeDetails) {
        return typeRepository.findById(id).map(type -> {
            type.setName(typeDetails.getName());
            return typeRepository.save(type);
        }).orElseThrow(() -> new RuntimeException("Type not found"));
    }
    public void deleteType(String id) {
        typeRepository.deleteById(id);
    }
}
