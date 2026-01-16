package com.abhinand.SpringSecEx.controller;

import com.abhinand.SpringSecEx.model.Asset;
import com.abhinand.SpringSecEx.repository.AssetRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assets")
public class AssetsController {

    private final AssetRepository assetRepository;
    private final Path uploadDir = Paths.get("uploads");

    public AssetsController(AssetRepository assetRepository) throws Exception {
        this.assetRepository = assetRepository;
        Files.createDirectories(uploadDir);
    }

    // (A) CREATE ASSET
    @PostMapping
    public Asset uploadAsset(@RequestParam MultipartFile file) throws Exception {

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(storedName);
        Files.copy(file.getInputStream(), target);

        Asset asset = new Asset();
        asset.setOriginalName(file.getOriginalFilename());
        asset.setStoredName(storedName);
        asset.setContentType(file.getContentType());
        asset.setSize(file.getSize());

        return assetRepository.save(asset);
    }

    // (B) GET ALL ASSETS (non-deleted)
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetRepository.findByDeletedFalse();
    }

    // (C) GET ASSET BY ID (download)
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getAsset(@PathVariable Long id) throws Exception {

        Asset asset = assetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        Path file = uploadDir.resolve(asset.getStoredName());
        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(asset.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + asset.getOriginalName() + "\"")
                .body(resource);
    }

    // (D) UPDATE ASSET (PATCH)
    @PatchMapping("/{id}")
    public Asset updateAsset(
            @PathVariable Long id,
            @RequestParam(required = false) String originalName
    ) {
        Asset asset = assetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (originalName != null) {
            asset.setOriginalName(originalName);
        }

        return assetRepository.save(asset);
    }

    // (E) SOFT DELETE ASSET
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {

        Asset asset = assetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setDeleted(true);
        assetRepository.save(asset);

        return ResponseEntity.noContent().build();
    }
}