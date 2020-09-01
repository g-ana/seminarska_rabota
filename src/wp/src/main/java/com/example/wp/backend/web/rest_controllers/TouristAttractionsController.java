package com.example.wp.backend.web.rest_controllers;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import com.example.wp.backend.service.impl.TouristAttractionsServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/tourist-attractions", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class TouristAttractionsController {

    private final TouristAttractionsServiceImpl touristAttractionsService;

    public TouristAttractionsController(TouristAttractionsServiceImpl touristAttractionsService) {
        this.touristAttractionsService = touristAttractionsService;
    }

    @GetMapping("/beaches")
    public List<Map<String, String>> findAllBeaches() {
        return this.touristAttractionsService.findAllBeaches();
    }

    @GetMapping(value = "/beaches", params = "country")
    public List<Map<String, String>> findAllBeachesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllBeachesByCountry(country);
    }

    @GetMapping("/beaches/{name}")
    public Map<String, String> findBeachByName(@PathVariable String name) {
        return this.touristAttractionsService.findBeachByName(name);
    }

    @GetMapping("/amusement-parks")
    public List<Map<String, String>> findAllAmusementParks() {
        return this.touristAttractionsService.findAllAmusementParks();
    }

    @GetMapping(value = "/amusement-parks", params = "country")
    public List<Map<String, String>> findAllAmusementParksByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllAmusementParksByCountry(country);
    }

    @GetMapping("/amusement-parks/{name}")
    public Map<String, String> findAmusementParkByName(@PathVariable String name) {
        return this.touristAttractionsService.findAmusementParkByName(name);
    }

    @GetMapping("/parks")
    public List<Map<String, String>> findAllParks() {
        return this.touristAttractionsService.findAllParks();
    }

    @GetMapping(value = "/parks", params = "country")
    public List<Map<String, String>> findAllParksByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllParksByCountry(country);
    }

    @GetMapping("/parks/{name}")
    public Map<String, String> findParkByName(@PathVariable String name) {
        return this.touristAttractionsService.findParkByName(name);
    }

    @GetMapping("/show-caves")
    public List<Map<String, String>> findAllShowCaves() {
        return this.touristAttractionsService.findAllShowCaves();
    }

    @GetMapping(value = "/show-caves", params = "country")
    public List<Map<String, String>> findAllShowCavesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllShowCavesByCountry(country);
    }

    @GetMapping("/show-caves/{name}")
    public Map<String, String> findShowCaveByName(@PathVariable String name) {
        return this.touristAttractionsService.findShowCaveByName(name);
    }

    @GetMapping("/towers")
    public List<Map<String, String>> findAllTowers() {
        return this.touristAttractionsService.findAllTowers();
    }

    @GetMapping(value = "/towers", params = "country")
    public List<Map<String, String>> findAllTowersByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllTowersByCountry(country);
    }

    @GetMapping("/towers/{name}")
    public Map<String, String> findTowerByName(@PathVariable String name) {
        return this.touristAttractionsService.findTowerByName(name);
    }

    @GetMapping("/castles")
    public List<Map<String, String>> findAllCastles() {
        return this.touristAttractionsService.findAllCastles();
    }

    @GetMapping(value = "/castles", params = "country")
    public List<Map<String, String>> findAllCastlesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllCastlesByCountry(country);
    }

    @GetMapping("/castles/{name}")
    public Map<String, String> findCastleByName(@PathVariable String name) {
        return this.touristAttractionsService.findCastleByName(name);
    }

    @GetMapping("/palaces")
    public List<Map<String, String>> findAllPalaces() {
        return this.touristAttractionsService.findAllPalaces();
    }

    @GetMapping(value = "/palaces", params = "country")
    public List<Map<String, String>> findAllPalacesByCountry(String country) {
        return this.touristAttractionsService.findAllPalacesByCountry(country);
    }

    @GetMapping("/palaces/{name}")
    public Map<String, String> findPalaceByName(@PathVariable String name) {
        return this.touristAttractionsService.findPalaceByName(name);
    }

    @GetMapping("/royal-residences")
    public List<Map<String, String>> findAllRoyalResidences() {
        return this.touristAttractionsService.findAllRoyalResidences();
    }

    @GetMapping(value = "/royal-residences", params = "country")
    public List<Map<String, String>> findAllRoyalResidencesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllRoyalResidencesByCountry(country);
    }

    @GetMapping("/royal-residences/{name}")
    public Map<String, String> findRoyalResidenceByName(@PathVariable String name) {
        return this.touristAttractionsService.findRoyalResidenceByName(name);
    }

    @GetMapping("/museums")
    public List<Map<String, String>> findAllMuseums() {
        return this.touristAttractionsService.findAllMuseums();
    }

    @GetMapping(value = "/museums", params = "country")
    public List<Map<String, String>> findAllMuseumsByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllMuseumsByCountry(country);
    }

    @GetMapping("/museums/{name}")
    public Map<String, String> findMuseumByName(@PathVariable String name) {
        return this.touristAttractionsService.findMuseumByName(name);
    }

    @GetMapping("/monuments")
    public List<Map<String, String>> findAllMonuments() {
        return this.touristAttractionsService.findAllMonuments();
    }

    @GetMapping(value = "/monuments", params = "country")
    public List<Map<String, String>> findAllMonumentsByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllMonumentsByCountry(country);
    }

    @GetMapping("/monuments/{name}")
    public Map<String, String> findMonumentByName(@PathVariable String name) {
        return this.touristAttractionsService.findMonumentByName(name);
    }

    @GetMapping("/buildings")
    public List<Map<String, String>> findAllBuildings() {
        return this.touristAttractionsService.findAllBuildings();
    }

    @GetMapping(value = "/buildings", params = "country")
    public List<Map<String, String>> findAllBuildingsByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllBuildingsByCountry(country);
    }

    @GetMapping("/buildings/{name}")
    public Map<String, String> findBuildingByName(@PathVariable String name) {
        return this.touristAttractionsService.findBuildingByName(name);
    }

    @GetMapping("/resorts")
    public List<Map<String, String>> findAllResorts() {
        return this.touristAttractionsService.findAllResorts();
    }

    @GetMapping(value = "/resorts", params = "country")
    public List<Map<String, String>> findAllResortsByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllResortsByCountry(country);
    }

    @GetMapping("/resorts/{name}")
    public Map<String, String> findResortByName(@PathVariable String name) {
        return this.touristAttractionsService.findResortByName(name);
    }

    @GetMapping("/protected-areas")
    public List<Map<String, String>> findAllProtectedAreas() {
        return this.touristAttractionsService.findAllProtectedAreas();
    }

    @GetMapping(value = "/protected-areas", params = "country")
    public List<Map<String, String>> findAllProtectedAreasByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllProtectedAreasByCountry(country);
    }

    @GetMapping("/protected-areas/{name}")
    public Map<String, String> findProtectedAreaByName(@PathVariable String name) {
        return this.touristAttractionsService.findProtectedAreaByName(name);
    }

    @GetMapping("/nature-reserves")
    public List<Map<String, String>> findAllNatureReserves() {
        return this.touristAttractionsService.findAllNatureReserves();
    }

    @GetMapping(value = "/nature-reserves", params = "country")
    public List<Map<String, String>> findAllNatureReservesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllNatureReservesByCountry(country);
    }

    @GetMapping("/nature-reserves/{name}")
    public Map<String, String> findNatureReserveByName(@PathVariable String name) {
        return this.touristAttractionsService.findNatureReserveByName(name);
    }

    @GetMapping("/historic-districts")
    public List<Map<String, String>> findAllHistoricDistricts() {
        return this.touristAttractionsService.findAllHistoricDistricts();
    }

    @GetMapping(value = "/historic-districts", params = "country")
    public List<Map<String, String>> findAllHistoricDistrictsByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllHistoricDistrictsByCountry(country);
    }

    @GetMapping("/historic-districts/{name}")
    public Map<String, String> findHistoricDistrictByName(@PathVariable String name) {
        return this.touristAttractionsService.findHistoricDistrictByName(name);
    }

    @GetMapping("/heraldic-sites")
    public List<Map<String, String>> findAllHeraldicSites() {
        return this.touristAttractionsService.findAllHeraldicSites();
    }

    @GetMapping(value = "/heraldic-sites", params = "country")
    public List<Map<String, String>> findAllHeraldicSitesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllHeraldicSitesByCountry(country);
    }

    @GetMapping("/heraldic-sites/{name}")
    public Map<String, String> findHeraldicSiteByName(@PathVariable String name) {
        return this.touristAttractionsService.findHeraldicSiteByName(name);
    }

    @GetMapping("/underground-cities")
    public List<Map<String, String>> findAllUndergroundCities() {
        return this.touristAttractionsService.findAllUndergroundCities();
    }

    @GetMapping(value = "/underground-cities", params = "country")
    public List<Map<String, String>> findAllUndergroundCitiesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllUndergroundCitiesByCountry(country);
    }

    @GetMapping("/underground-cities/{name}")
    public Map<String, String> findUndergroundCityByName(@PathVariable String name) {
        return this.touristAttractionsService.findUndergroundCityByName(name);
    }

    @GetMapping("/archaeological-sites")
    public List<Map<String, String>> findAllArchaeologicalSites() {
        return this.touristAttractionsService.findAllArchaeologicalSites();
    }

    @GetMapping(value = "/archaeological-sites", params = "country")
    public List<Map<String, String>> findAllArchaeologicalSitesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllArchaeologicalSitesByCountry(country);
    }

    @GetMapping("/archaeological-sites/{name}")
    public Map<String, String> findArchaeologicalSiteByName(@PathVariable String name) {
        return this.touristAttractionsService.findArchaeologicalSiteByName(name);
    }

    @GetMapping("/natural-arches")
    public List<Map<String, String>> findAllNaturalArches() {
        return this.touristAttractionsService.findAllNaturalArches();
    }

    @GetMapping(value = "/natural-arches", params = "country")
    public List<Map<String, String>> findAllNaturalArchesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllNaturalArchesByCountry(country);
    }

    @GetMapping("/natural-arches/{name}")
    public Map<String, String> findNaturalArchByName(@PathVariable String name) {
        return this.touristAttractionsService.findNaturalArchByName(name);
    }

    @GetMapping("/triumphal-arches")
    public List<Map<String, String>> findAllTriumphalArches() {
        return this.touristAttractionsService.findAllTriumphalArches();
    }

    @GetMapping(value = "/triumphal-arches", params = "country")
    public List<Map<String, String>> findAllTriumphalArchesByCountry(@RequestParam String country) {
        return this.touristAttractionsService.findAllTriumphalArchesByCountry(country);
    }

    @GetMapping("/triumphal-arches/{name}")
    public Map<String, String> findTriumphalArchByName(@PathVariable String name) {
        return this.touristAttractionsService.findTriumphalArchByName(name);
    }

    @GetMapping("/town-squares")
    public List<Map<String, String>> findAllTownSquares() {
        return this.touristAttractionsService.findAlTownSquares();
    }

    @GetMapping(value = "/town-squares/{name}")
    public Map<String, String> findTownSquareByName(@PathVariable String name) {
        return this.touristAttractionsService.findTownSquareByName(name);
    }

    @GetMapping("/waterfronts")
    public List<Map<String, String>> findAllWaterfronts() {
        return this.touristAttractionsService.findAllWaterfronts();
    }

    @GetMapping("/waterfronts/{name}")
    public Map<String, String> findWaterfrontByName(@PathVariable String name) {
        return this.touristAttractionsService.findWaterfrontByName(name);
    }

    @GetMapping("/fountains")
    public List<Map<String, String>> findAllFountains() {
        return this.touristAttractionsService.findAllFountains();
    }

    @GetMapping("/fountains/{name}")
    public Map<String, String> findFountainByName(@PathVariable String name) {
        return this.touristAttractionsService.findFountainByName(name);
    }

    @GetMapping("/entertainment-districts")
    public List<Map<String, String>> findAllEntertainmentDistricts() {
        return this.touristAttractionsService.findAllEntertainmentDistricts();
    }

    @GetMapping("/entertainment-districts/{name}")
    public Map<String, String> findEntertainmentDistrictByName(@PathVariable String name) {
        return this.touristAttractionsService.findEntertainmentDistrictByName(name);
    }

    @GetMapping("/visitor-centers")
    public List<Map<String, String>> findAllVisitorCenters() {
        return this.touristAttractionsService.findAllVisitorCenters();
    }

    @GetMapping("/visitor-centers/{name}")
    public Map<String, String> findVisitorCenterByName(@PathVariable String name) {
        return this.touristAttractionsService.findVisitorCenterByName(name);
    }

    @GetMapping("/space-related-visitor-attractions")
    public List<Map<String, String>> findAllSpaceRelatedVisitorAttractions() {
        return this.touristAttractionsService.findAllSpaceRelatedVisitorAttractions();
    }

    @GetMapping("/space-related-visitor-attractions/{name}")
    public Map<String, String> findSpaceRelatedVisitorAttractionByName(@PathVariable String name) {
        return this.touristAttractionsService.findSpaceRelatedVisitorAttractionByName(name);
    }
}
