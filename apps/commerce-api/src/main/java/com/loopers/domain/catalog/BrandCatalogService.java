package com.loopers.domain.catalog;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandCatalogService {
    private final BrandRepository brandRepository;

    public BrandCatalogService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Optional<Brand> find(Long id) {
        return brandRepository.find(id);
    }

    public List<Brand>  findAllById(Iterable<Long> ids){
        return brandRepository.findAllById(ids);
    }
}
