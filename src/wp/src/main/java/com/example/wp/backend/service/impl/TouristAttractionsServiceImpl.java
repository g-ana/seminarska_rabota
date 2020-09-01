package com.example.wp.backend.service.impl;

import wp.src.main.java.com.example.wp.backend.service.TouristAttractionsService;
import org.springframework.stereotype.Service;
import com.example.wbs.ConsumingLinkedData;

import java.util.List;
import java.util.Map;

@Service
public class TouristAttractionsServiceImpl implements TouristAttractionsService {

    private final ConsumingLinkedData consumingLinkedData;

    public TouristAttractionsServiceImpl(ConsumingLinkedData consumingLinkedData) {
        this.consumingLinkedData=consumingLinkedData;
    }

    public List<Map<String, String>> findAllCategories() {
        return this.consumingLinkedData.findAllCategories();
    }

    public Map<String, String> findCategoryByName(String name) {
        return this.consumingLinkedData.findCategoryByName(name);
    }

    public List<Map<String, String>> findAllBeaches() {
        return this.consumingLinkedData.findAllBeaches();
    }

    public List<Map<String, String>> findAllBeachesByCountry(String country) {
        return this.consumingLinkedData.findAllBeachesByCountry(country);
    }

    public Map<String, String> findBeachByName(String name) {
        return this.consumingLinkedData.findBeachByName(name);
    }

    public List<Map<String, String>> findAllAmusementParks() {
        return this.consumingLinkedData.findAllAmusementParks();
    }

    public List<Map<String, String>> findAllAmusementParksByCountry(String country) {
        return this.consumingLinkedData.findAllAmusementParksByCountry(country);
    }

    public Map<String, String> findAmusementParkByName(String name) {
        return this.consumingLinkedData.findAmusementParkByName(name);
    }

    public List<Map<String, String>> findAllParks() {
        return this.consumingLinkedData.findAllParks();
    }

    public List<Map<String, String>> findAllParksByCountry(String country) {
        return this.consumingLinkedData.findAllParksByCountry(country);
    }

    public Map<String, String> findParkByName(String name) {
        return this.consumingLinkedData.findParkByName(name);
    }

    public List<Map<String, String>> findAllShowCaves() {
        return this.consumingLinkedData.findAllShowCaves();
    }

    public List<Map<String, String>> findAllShowCavesByCountry(String country) {
        return this.consumingLinkedData.findAllShowCavesByCountry(country);
    }

    public Map<String, String> findShowCaveByName(String name) {
        return this.consumingLinkedData.findShowCaveByName(name);
    }

    public List<Map<String, String>> findAllTowers() {
        return this.consumingLinkedData.findAllTowers();
    }

    public List<Map<String, String>> findAllTowersByCountry(String country) {
        return this.consumingLinkedData.findAllTowersByCountry(country);
    }

    public Map<String, String> findTowerByName(String name) {
        return this.consumingLinkedData.findTowerByName(name);
    }

    public List<Map<String, String>> findAllCastles() {
        return this.consumingLinkedData.findAllCastles();
    }

    public List<Map<String, String>> findAllCastlesByCountry(String country) {
        return this.consumingLinkedData.findAllCastlesByCountry(country);
    }

    public Map<String, String> findCastleByName(String name) {
        return this.consumingLinkedData.findCastleByName(name);
    }

    public List<Map<String, String>> findAllPalaces() {
        return this.consumingLinkedData.findAllPalaces();
    }

    public List<Map<String, String>> findAllPalacesByCountry(String country) {
        return this.consumingLinkedData.findAllPalacesByCountry(country);
    }

    public Map<String, String> findPalaceByName(String name) {
        return this.consumingLinkedData.findPalaceByName(name);
    }

    public List<Map<String, String>> findAllRoyalResidences() {
        return this.consumingLinkedData.findAllRoyalResidences();
    }

    public List<Map<String, String>> findAllRoyalResidencesByCountry(String country) {
        return this.consumingLinkedData.findAllRoyalResidencesByCountry(country);
    }

    public Map<String, String> findRoyalResidenceByName(String name) {
        return this.consumingLinkedData.findRoyalResidenceByName(name);
    }

    public List<Map<String, String>> findAllMuseums() {
        return this.consumingLinkedData.findAllMuseums();
    }

    public List<Map<String, String>> findAllMuseumsByCountry(String country) {
        return this.consumingLinkedData.findAllMuseumsByCountry(country);
    }

    public Map<String, String> findMuseumByName(String name) {
        return this.consumingLinkedData.findMuseumByName(name);
    }

    public List<Map<String, String>> findAllMonuments() {
        return this.consumingLinkedData.findAllMonuments();
    }

    public List<Map<String, String>> findAllMonumentsByCountry(String country) {
        return this.consumingLinkedData.findAllMonumentsByCountry(country);
    }

    public Map<String, String> findMonumentByName(String name) {
        return this.consumingLinkedData.findMonumentByName(name);
    }

    public List<Map<String, String>> findAllBuildings() {
        return this.consumingLinkedData.findAllBuildings();
    }

    public List<Map<String, String>> findAllBuildingsByCountry(String country) {
        return this.consumingLinkedData.findAllBuildingsByCountry(country);
    }

    public Map<String, String> findBuildingByName(String name) {
        return this.consumingLinkedData.findBuildingByName(name);
    }

    public List<Map<String, String>> findAllResorts() {
        return this.consumingLinkedData.findAllResorts();
    }

    public List<Map<String, String>> findAllResortsByCountry(String country) {
        return this.consumingLinkedData.findAllResortsByCountry(country);
    }

    public Map<String, String> findResortByName(String name) {
        return this.consumingLinkedData.findResortByName(name);
    }

    public List<Map<String, String>> findAllProtectedAreas() {
        return this.consumingLinkedData.findAllProtectedAreas();
    }

    public List<Map<String, String>> findAllProtectedAreasByCountry(String country) {
        return this.consumingLinkedData.findAllProtectedAreasByCountry(country);
    }

    public Map<String, String> findProtectedAreaByName(String name) {
        return this.consumingLinkedData.findProtectedAreaByName(name);
    }

    public List<Map<String, String>> findAllNatureReserves() {
        return this.consumingLinkedData.findAllNatureReserves();
    }

    public List<Map<String, String>> findAllNatureReservesByCountry(String country) {
        return this.consumingLinkedData.findAllNatureReservesByCountry(country);
    }

    public Map<String, String> findNatureReserveByName(String name) {
        return this.consumingLinkedData.findNatureReserveByName(name);
    }

    public List<Map<String, String>> findAllHistoricDistricts() {
        return this.consumingLinkedData.findAllHistoricDistricts();
    }

    public List<Map<String, String>> findAllHistoricDistrictsByCountry(String country) {
        return this.consumingLinkedData.findAllHistoricDistrictsByCountry(country);
    }

    public Map<String, String> findHistoricDistrictByName(String name) {
        return this.consumingLinkedData.findHistoricDistrictByName(name);
    }

    public List<Map<String, String>> findAllHeraldicSites() {
        return this.consumingLinkedData.findAllHeraldicSites();
    }

    public List<Map<String, String>> findAllHeraldicSitesByCountry(String country) {
        return this.consumingLinkedData.findAllHeraldicSitesByCountry(country);
    }

    public Map<String, String> findHeraldicSiteByName(String name) {
        return this.consumingLinkedData.findHeraldicSiteByName(name);
    }

    public List<Map<String, String>> findAllUndergroundCities() {
        return this.consumingLinkedData.findAllUndergroundCities();
    }

    public List<Map<String, String>> findAllUndergroundCitiesByCountry(String country) {
        return this.consumingLinkedData.findAllUndergroundCitiesByCountry(country);
    }

    public List<Map<String, String>> findAllArchaeologicalSites() {
        return this.consumingLinkedData.findAllArchaeologicalSites();
    }

    public List<Map<String, String>> findAllArchaeologicalSitesByCountry(String country) {
        return this.consumingLinkedData.findAllArchaeologicalSitesByCountry(country);
    }

    public Map<String, String> findArchaeologicalSiteByName(String name) {
        return this.consumingLinkedData.findArchaeologicalSiteByName(name);
    }

    public Map<String, String> findUndergroundCityByName(String name) {
        return this.consumingLinkedData.findUndergroundCityByName(name);
    }

    public List<Map<String, String>> findAllNaturalArches() {
        return this.consumingLinkedData.findAllNaturalArches();
    }

    public List<Map<String, String>> findAllNaturalArchesByCountry(String country) {
        return this.consumingLinkedData.findAllNaturalArchesByCountry(country);
    }

    public Map<String, String> findNaturalArchByName(String name) {
        return this.consumingLinkedData.findNaturalArchByName(name);
    }

    public List<Map<String, String>> findAllTriumphalArches() {
        return this.consumingLinkedData.findAllTriumphalArches();
    }

    public List<Map<String, String>> findAllTriumphalArchesByCountry(String country) {
        return this.consumingLinkedData.findAllTriumphalArchesByCountry(country);
    }

    public Map<String, String> findTriumphalArchByName(String name) {
        return this.consumingLinkedData.findTriumphalArchByName(name);
    }

    public List<Map<String, String>> findAlTownSquares() {
        return this.consumingLinkedData.findAllTownSquares();
    }

    public Map<String, String> findTownSquareByName(String name) {
        return this.consumingLinkedData.findTownSquareByName(name);
    }

    public List<Map<String, String>> findAllWaterfronts() {
        return this.consumingLinkedData.findAllWaterfronts();
    }

    public Map<String, String> findWaterfrontByName(String name) {
        return this.consumingLinkedData.findWaterfrontByName(name);
    }

    public List<Map<String, String>> findAllFountains() {
        return this.consumingLinkedData.findAllFountains();
    }

    public Map<String, String> findFountainByName(String name) {
        return this.consumingLinkedData.findFountainByName(name);
    }

    public List<Map<String, String>> findAllEntertainmentDistricts() {
        return this.consumingLinkedData.findAllEntertainmentDistricts();
    }

    public Map<String, String> findEntertainmentDistrictByName(String name) {
        return this.consumingLinkedData.findEntertainmentDistrictByName(name);
    }

    public List<Map<String, String>> findAllVisitorCenters() {
        return this.consumingLinkedData.findAllVisitorCentres();
    }

    public Map<String, String> findVisitorCenterByName(String name) {
        return this.consumingLinkedData.findVisitorCenterByName(name);
    }

    public List<Map<String, String>> findAllSpaceRelatedVisitorAttractions() {
        return this.consumingLinkedData.findAllSpaceRelatedVisitorAttractions();
    }

    public Map<String, String> findSpaceRelatedVisitorAttractionByName(String name) {
        return this.consumingLinkedData.findSpaceRelatedVisitorAttractionByName(name);
    }
}
