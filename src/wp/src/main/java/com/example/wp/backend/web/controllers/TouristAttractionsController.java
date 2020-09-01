package com.example.wp.backend.web.controllers;

import com.example.wp.backend.service.impl.TouristAttractionsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tourist-attractions")
public class TouristAttractionsController {

    private final TouristAttractionsServiceImpl touristAttractionsService;

    public TouristAttractionsController(TouristAttractionsServiceImpl touristAttractionsService) {
        this.touristAttractionsService=touristAttractionsService;
    }

    @GetMapping("/beaches")
    public void findAllBeaches() {
        this.touristAttractionsService.findAllBeaches();
    }

    @GetMapping(value = "/beaches", params = "country")
    public void findAllBeachesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllBeachesByCountry(country);
    }

    @GetMapping("/beaches/{name}")
    public void findBeachByName(@PathVariable String name) {
        this.touristAttractionsService.findBeachByName(name);
    }

    @GetMapping("/amusement-parks")
    public void findAllAmusementParks() {
        this.touristAttractionsService.findAllAmusementParks();
    }

    @GetMapping(value = "/amusement-parks", params = "country")
    public void findAllAmusementParksByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllAmusementParksByCountry(country);
    }

    @GetMapping("/amusement-parks/{name}")
    public void findAmusementParkByName(@PathVariable String name) {
        this.touristAttractionsService.findAmusementParkByName(name);
    }

    @GetMapping("/parks")
    public void findAllParks() {
        this.touristAttractionsService.findAllParks();
    }

    @GetMapping(value ="/parks", params = "country")
    public void findAllParksByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllParksByCountry(country);
    }

    @GetMapping("/parks/{name}")
    public void findParkByName(@PathVariable String name) {
        this.touristAttractionsService.findParkByName(name);
    }

    @GetMapping("/show-caves")
    public void findAllShowCaves() {
        this.touristAttractionsService.findAllShowCaves();
    }

    @GetMapping(value = "/show-caves", params = "country")
    public void findAllShowCavesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllShowCavesByCountry(country);
    }

    @GetMapping("/show-caves/{name}")
    public void findShowCaveByName(@PathVariable String name) {
        this.touristAttractionsService.findShowCaveByName(name);
    }

    @GetMapping("/towers")
    public void findAllTowers() {
        this.touristAttractionsService.findAllTowers();
    }

    @GetMapping(value = "/towers", params = "country")
    public void findAllTowersByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllTowersByCountry(country);
    }

    @GetMapping("/towers/{name}")
    public void findTowerByName(@PathVariable String name) {
        this.touristAttractionsService.findTowerByName(name);
    }

    @GetMapping("/castles")
    public void findAllCastles() {
        this.touristAttractionsService.findAllCastles();
    }

    @GetMapping(value = "/castles", params = "country")
    public void findAllCastlesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllCastlesByCountry(country);
    }

    @GetMapping("/castles/{name}")
    public void findCastleByName(@PathVariable String name) {
        this.touristAttractionsService.findCastleByName(name);
    }

    @GetMapping("/palaces")
    public void findAllPalaces() {
        this.touristAttractionsService.findAllPalaces();
    }

    @GetMapping(value = "/palaces", params = "country")
    public void findAllPalacesByCountry(String country) {
        this.touristAttractionsService.findAllPalacesByCountry(country);
    }

    @GetMapping("/palaces/{name}")
    public void findPalaceByName(@PathVariable String name) {
        this.touristAttractionsService.findPalaceByName(name);
    }

    @GetMapping("/royal-residences")
    public void findAllRoyalResidences() {
        this.touristAttractionsService.findAllRoyalResidences();
    }

    @GetMapping(value = "/royal-residences", params = "country")
    public void findAllRoyalResidencesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllRoyalResidencesByCountry(country);
    }

    @GetMapping("/royal-residences/{name}")
    public void findRoyalResidenceByName(@PathVariable String name) {
        this.touristAttractionsService.findRoyalResidenceByName(name);
    }

    @GetMapping("/museums")
    public void findAllMuseums() {
        this.touristAttractionsService.findAllMuseums();
    }

    @GetMapping(value = "/museums", params = "country")
    public void findAllMuseumsByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllMuseumsByCountry(country);
    }

    @GetMapping("/museums/{name}")
    public void findMuseumByName(@PathVariable String name) {
        this.touristAttractionsService.findMuseumByName(name);
    }

    @GetMapping("/monuments")
    public void findAllMonuments() {
        this.touristAttractionsService.findAllMonuments();
    }

    @GetMapping(value = "/monuments", params = "country")
    public void findAllMonumentsByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllMonumentsByCountry(country);
    }

    @GetMapping("/monuments/{name}")
    public void findMonumentByName(@PathVariable String name) {
        this.touristAttractionsService.findMonumentByName(name);
    }

    @GetMapping("/buildings")
    public void findAllBuildings() {
        this.touristAttractionsService.findAllBuildings();
    }

    @GetMapping(value = "/buildings", params = "country")
    public void findAllBuildingsByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllBuildingsByCountry(country);
    }

    @GetMapping("/buildings/{name}")
    public void findBuildingByName(@PathVariable String name) {
        this.touristAttractionsService.findBuildingByName(name);
    }

    @GetMapping("/resorts")
    public void findAllResorts() {
        this.touristAttractionsService.findAllResorts();
    }

    @GetMapping(value = "/resorts", params = "country")
    public void findAllResortsByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllResortsByCountry(country);
    }

    @GetMapping("/resorts/{name}")
    public void findResortByName(@PathVariable String name) {
        this.touristAttractionsService.findResortByName(name);
    }

    @GetMapping("/protected-areas")
    public void findAllProtectedAreas() {
        this.touristAttractionsService.findAllProtectedAreas();
    }

    @GetMapping(value="/protected-areas", params = "country")
    public void findAllProtectedAreasByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllProtectedAreasByCountry(country);
    }

    @GetMapping("/protected-areas/{name}")
    public void findProtectedAreaByName(@PathVariable String name) {
        this.touristAttractionsService.findProtectedAreaByName(name);
    }

    @GetMapping("/nature-reserves")
    public void findAllNatureReserves() {
        this.touristAttractionsService.findAllNatureReserves();
    }

    @GetMapping(value = "/nature-reserves", params = "country")
    public void findAllNatureReservesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllNatureReservesByCountry(country);
    }

    @GetMapping("/nature-reserves/{name}")
    public void findNatureReserveByName(@PathVariable String name) {
        this.touristAttractionsService.findNatureReserveByName(name);
    }

    @GetMapping("/historic-districts")
    public void findAllHistoricDistricts() {
        this.touristAttractionsService.findAllHistoricDistricts();
    }

    @GetMapping(value = "/historic-districts", params = "country")
    public void findAllHistoricDistrictsByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllHistoricDistrictsByCountry(country);
    }

    @GetMapping("/historic-districts/{name}")
    public void findHistoricDistrictByName(@PathVariable String name) {
        this.touristAttractionsService.findHistoricDistrictByName(name);
    }

    @GetMapping("/heraldic-sites")
    public void findAllHeraldicSites() {
        this.touristAttractionsService.findAllHeraldicSites();
    }

    @GetMapping(value = "/heraldic-sites", params = "country")
    public void findAllHeraldicSitesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllHeraldicSitesByCountry(country);
    }

    @GetMapping("/heraldic-sites/{name}")
    public void findHeraldicSiteByName(@PathVariable String name) {
        this.touristAttractionsService.findHeraldicSiteByName(name);
    }

    @GetMapping("/underground-cities")
    public void findAllUndergroundCities() {
        this.touristAttractionsService.findAllUndergroundCities();
    }

    @GetMapping(value = "/underground-cities", params = "country")
    public void findAllUndergroundCitiesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllUndergroundCitiesByCountry(country);
    }

    @GetMapping("/underground-cities/{name}")
    public void findUndergroundCityByName(@PathVariable String name) {
        this.touristAttractionsService.findUndergroundCityByName(name);
    }

    @GetMapping("/archaeological-sites")
    public void findAllArchaeologicalSites() {
        this.touristAttractionsService.findAllArchaeologicalSites();
    }

    @GetMapping(value = "/archaeological-sites", params = "country")
    public void findAllArchaeologicalSitesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllArchaeologicalSitesByCountry(country);
    }

    @GetMapping("/archaeological-sites/{name}")
    public void findArchaeologicalSiteByName(@PathVariable String name) {
        this.touristAttractionsService.findArchaeologicalSiteByName(name);
    }

    @GetMapping("/natural-arches")
    public void findAllNaturalArches() {
        this.touristAttractionsService.findAllNaturalArches();
    }

    @GetMapping(value = "/natural-arches", params = "country")
    public void findAllNaturalArchesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllNaturalArchesByCountry(country);
    }

    @GetMapping("/natural-arches/{name}")
    public void findNaturalArchByName(@PathVariable String name) {
        this.touristAttractionsService.findNaturalArchByName(name);
    }

    @GetMapping("/triumphal-arches")
    public void findAllTriumphalArches() {
        this.touristAttractionsService.findAllTriumphalArches();
    }

    @GetMapping(value = "/triumphal-arches", params = "country")
    public void findAllTriumphalArchesByCountry(@RequestParam String country) {
        this.touristAttractionsService.findAllTriumphalArchesByCountry(country);
    }

    @GetMapping("/triumphal-arches/{name}")
    public void findTriumphalArchByName(@PathVariable String name) {
        this.touristAttractionsService.findTriumphalArchByName(name);
    }

    @GetMapping("/town-squares")
    public void findAllTownSquares() {
        this.touristAttractionsService.findAlTownSquares();
    }

    @GetMapping("/town-squares/{name}")
    public void findTownSquareByName(@PathVariable String name) {
        this.touristAttractionsService.findTownSquareByName(name);
    }

    @GetMapping("/waterfronts")
    public void findAllWaterfronts() {
        this.touristAttractionsService.findAllWaterfronts();
    }

    @GetMapping("/waterfronts/{name}")
    public void findWaterfrontByName(@PathVariable String name) {
        this.touristAttractionsService.findWaterfrontByName(name);
    }

    @GetMapping("/fountains")
    public void findAllFountains() {
        this.touristAttractionsService.findAllFountains();
    }

    @GetMapping("/fountains/{name}")
    public void findFountainByName(@PathVariable String name) {
        this.touristAttractionsService.findFountainByName(name);
    }

    @GetMapping("/entertainment-districts")
    public void findAllEntertainmentDistricts() {
        this.touristAttractionsService.findAllEntertainmentDistricts();
    }

    @GetMapping("/entertainment-districts/{name}")
    public void findEntertainmentDistrictByName(@PathVariable String name) {
        this.touristAttractionsService.findEntertainmentDistrictByName(name);
    }

    @GetMapping("/visitor-centers")
    public void findAllVisitorCenters() {
        this.touristAttractionsService.findAllVisitorCenters();
    }

    @GetMapping("/visitor-centers/{name}")
    public void findVisitorCenterByName(@PathVariable String name) {
        this.touristAttractionsService.findVisitorCenterByName(name);
    }

    @GetMapping("/space-related-visitor-attractions")
    public void findAllSpaceRelatedVisitorAttractions() {
        this.touristAttractionsService.findAllSpaceRelatedVisitorAttractions();
    }

    @GetMapping("/space-related-visitor-attractions/{name}")
    public void findSpaceRelatedVisitorAttractionByName(@PathVariable String name) {
        this.touristAttractionsService.findSpaceRelatedVisitorAttractionByName(name);
    }
}
