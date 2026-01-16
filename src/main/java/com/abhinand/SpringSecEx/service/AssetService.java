package com.abhinand.SpringSecEx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abhinand.SpringSecEx.model.Asset;
import com.abhinand.SpringSecEx.repository.AssetRepository;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findByDeletedFalse();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public Asset updateAsset(Long id, Asset updatedAsset) {
        Asset asset = getAssetById(id);

        if (updatedAsset.getName() != null) {
            asset.setName(updatedAsset.getName());
        }

        if (updatedAsset.getDescription() != null) {
            asset.setDescription(updatedAsset.getDescription());;
        }

        return assetRepository.save(asset);
    }

    public void softDeleteAsset(Long id) {
        Asset asset = getAssetById(id);
        asset.setDeleted(true);
        assetRepository.save(asset);
    }

}
