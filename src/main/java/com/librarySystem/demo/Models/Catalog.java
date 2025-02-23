package com.librarySystem.demo.Models;

import java.util.List;
import java.util.Date;

public class Catalog {
    private String id;
    private String userId;
    private List<String> bookIds;
    private Date borrowDate;
    private Date expiredDate;
}
