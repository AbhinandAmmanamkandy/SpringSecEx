package com.abhinand.SpringSecEx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhinand.SpringSecEx.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByDeletedFalse();

    Optional<Asset> findByIdAndDeletedFalse(Long id);
}
