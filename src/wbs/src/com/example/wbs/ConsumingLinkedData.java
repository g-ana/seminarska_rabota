package com.example.wbs;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumingLinkedData {

    private static String SPARQLEndpoint = "http://dbpedia.org/sparql";
//    private static String URL = "";
//    private Model model = ModelFactory.createDefaultModel();
    private static String resource = "<http://dbpedia.org/resource/>";
    private static String ontology = "<http://dbpedia.org/ontology/>";
    private static String category = "<http://dbpedia.org/resource/Category:>";
    private static String terms = "<http://purl.org/dc/terms/>";
    private static String property = "<http://dbpedia.org/property/>";
    private static String skos = "<http://www.w3.org/2004/02/skos/core#>";
//    private int page = 0;
//    private int pageSize = 2;

    private static List<Map<String, String>> findAll(String queryString) {
        Query query = QueryFactory.create(queryString);
        List<Map<String, String>> all = new ArrayList<Map<String, String>>();
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                System.out.println(solution);
                String url = solution.get("category").toString();
                Model model = ModelFactory.createDefaultModel().read(url);
                Map<String, String> result = new HashMap<String, String>();
                StmtIterator iterator = model.listStatements();
                String label = "";
                while (iterator.hasNext()) {
                    Statement nextStatement = iterator.nextStatement();
                    String predicate = nextStatement.getPredicate().toString();
                    if (predicate.contains("label")) {
                        label = nextStatement.getObject().asLiteral().toString();
                        label = label.substring(0, label.length() - 3);
                        result.put("label", label);
                        //    System.out.println(label);
                        //    break;
                    }
                }
                all.add(result);
            }
            return all;
        }
    }

    private static List<Map<String, String>> findAllPaged(String queryString, int page, int pageSize) {
        List<Map<String, String>> paged = findAll(queryString);
        int pageStart = page * pageSize;
        int pageEnd = (page + 1) * pageSize;
        int contentEnd = paged.size() - 1;
        if (paged.size() < pageEnd) {
            return paged.subList(pageStart, contentEnd);
        }
        return paged.subList(pageStart, pageEnd);
    }

    private static Model findByName(String name) throws QueryExecException {
        if (name.contains(" ")) {
            name = name.replace(" ", "_");
        }
        String queryString = "PREFIX dbr: " + resource + "DESCRIBE dbr:" + name;
        Query query = QueryFactory.create(queryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            return queryExecution.execDescribe();
        }
    }

    public List<Map<String, String>> findAllCategories() {
        String queryString = "PREFIX skos: " + skos + "PREFIX dbc: " + category + "SELECT ?category WHERE { ?category skos:broader dbc:Tourist_attractions . }";
        return findAll(queryString);
    }

    public Map<String, String> findCategoryByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        String label = "";
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("label") && nextStatement.getLanguage().equals("en")) {
                label = nextStatement.getObject().asLiteral().toString();
                label = label.substring(0, label.length()-3);
                result.put("label", label);
            }
        }
        String resourceUrl = label.endsWith("ies") ? resource.substring(0, resource.length()-1) +
                label.substring(0, label.length()-3) + "y>" :
                label.endsWith("ches") ? resource.substring(0, resource.length()-1)
                + label.substring(0, label.length()-2) + ">" :
                label.endsWith("s") ? resource.substring(0, resource.length()-1)
                + label.substring(0, label.length()-1) + ">"
                : resource.substring(0, resource.length()-1) + label + ">";
        //    System.out.println(resource.substring(0, resource.length()-1) + label.substring(0, label.length()-2) + ">");
        //    System.out.println(resourceUrl);
        Model model2 = ModelFactory.createDefaultModel().read(resourceUrl.substring(1, resourceUrl.length()-1));
        StmtIterator statements = model2.listStatements();
        while (statements.hasNext()) {
            Statement nextStatement = statements.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract") && nextStatement.getLanguage().equals("en")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                comment = comment.substring(0, comment.length()-3);
                result.put("abstract", comment);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            }
        }
        return result;
    }

    public List<Map<String, String>> findCategoriesPaged(int page, int pageSize) {
        String queryString = "PREFIX skos: " + skos + "SELECT ?category WHERE { ?category skos:broader dbc:Tourist_attractions . }";
        return findAllPaged(queryString, page, pageSize);
    }

    public List<Map<String, String>> findAllLandmarks() {
        List<Map<String, String>> result = new ArrayList<>();
        result.add(this.findLandmarkByName("Kozjak_Hydro_Power_Plant"));
        result.add(this.findLandmarkByName("Skopje_Fortress"));
        result.add(this.findLandmarkByName("Skopje_Zoo"));
        result.add(this.findLandmarkByName("Millennium_Cross"));
        result.add(this.findLandmarkByName("Galichica"));
        result.add(this.findLandmarkByName("Memorial_House_of_Mother_Teresa"));
        result.add(this.findLandmarkByName("Church_of_St._Panteleimon_(Gorno_Nerezi)"));
        result.add(this.findLandmarkByName("Stone_Bridge_(Skopje)"));
        result.add(this.findLandmarkByName("Vodno"));
        result.add(this.findLandmarkByName("Art_Bridge"));
        result.add(this.findLandmarkByName("Skopje_Aqueduct"));
        result.add(this.findLandmarkByName("Museum_of_the_Macedonian_Struggle_(Skopje)"));
        result.add(this.findLandmarkByName("Porta_Macedonia"));
        result.add(this.findLandmarkByName("Church_of_St._Clement_of_Ohrid"));
        result.add(this.findLandmarkByName("Pella_Square"));
        result.add(this.findLandmarkByName("Skanderbeg_Square_(Skopje)"));
        result.add(this.findLandmarkByName("Macedonia_Square,_Skopje"));
        result.add(this.findLandmarkByName("Cerje,_Skopje"));
        result.add(this.findLandmarkByName("Old_Bazaar,_Skopje"));
        result.add(this.findLandmarkByName("Kral_Kızı_Mausoleum"));
        result.add(this.findLandmarkByName("Church_of_the_Ascension_of_Jesus,_Skopje"));
        result.add(this.findLandmarkByName("Museum_of_the_City_of_Skopje"));
        result.add(this.findLandmarkByName("Contemporary_Art_Museum_of_Macedonia"));
        result.add(this.findLandmarkByName("Museum_of_Macedonia"));
        result.add(this.findLandmarkByName("National_Gallery_of_Macedonia"));
        result.add(this.findLandmarkByName("Čifte_Hammam"));
        result.add(this.findLandmarkByName("Cathedral_of_the_Sacred_Heart_of_Jesus_(Skopje)"));
        result.add(this.findLandmarkByName("Mustafa_Pasha_Mosque"));
        result.add(this.findLandmarkByName("Sultan_Murad_Mosque"));
        result.add(this.findLandmarkByName("Kapan_Han"));
        result.add(this.findLandmarkByName("Suli_An"));
        result.add(this.findLandmarkByName("Kuršumli_An"));
        result.add(this.findLandmarkByName("Holocaust_Memorial_Center_for_the_Jews_of_Macedonia"));
        result.add(this.findLandmarkByName("Rezhanovce_Museum_of_Folk_Costumes"));
        result.add(this.findLandmarkByName("National_Institution_Museum,_Kumanovo"));
        result.add(this.findLandmarkByName("Pelister_National_Park"));
        result.add(this.findLandmarkByName("Mavrovo_National_Park"));
        result.add(this.findLandmarkByName("Magnolia_Square"));
        result.add(this.findLandmarkByName("Zebrnjak"));
        result.add(this.findLandmarkByName("Chetiri_Bandere_Monument"));
        result.add(this.findLandmarkByName("Ilinden_(memorial)"));
        result.add(this.findLandmarkByName("Josip_Broz_Tito_Monument,_Kumanovo"));
        result.add(this.findLandmarkByName("Philip_II_Statue"));
        result.add(this.findLandmarkByName("Memorial_Ossuary_Kumanovo"));
        result.add(this.findLandmarkByName("Mound_of_the_Unbeaten"));
        result.add(this.findLandmarkByName("ASNOM_Memorial_Center"));
        result.add(this.findLandmarkByName("Bitola_Zoo"));
        result.add(this.findLandmarkByName("Markovi_Kuli"));
        result.add(this.findLandmarkByName("Samuel's_Fortress,_Ohrid"));
        result.add(this.findLandmarkByName("Viničko_Kale"));
        result.add(this.findLandmarkByName("Strumica_Fortress"));
        result.add(this.findLandmarkByName("Doiran_Lake"));
        result.add(this.findLandmarkByName("Lake_Prespa"));
        result.add(this.findLandmarkByName("Berovo_Lake"));
        result.add(this.findLandmarkByName("Mavrovo_Lake"));
        result.add(this.findLandmarkByName("Lake_Ohrid"));
        result.add(this.findLandmarkByName("Ohrid-Prespa_Transboundary_Biosphere_Reserve"));
        result.add(this.findLandmarkByName("Golem_Grad"));
        result.add(this.findLandmarkByName("Matka_Canyon"));
        result.add(this.findLandmarkByName("Kolešino_Falls"));
        result.add(this.findLandmarkByName("Smolare_Falls"));
        result.add(this.findLandmarkByName("Korab_Falls"));
        result.add(this.findLandmarkByName("Bogomila_Falls"));
        result.add(this.findLandmarkByName("Koprišnica_Falls"));
        result.add(this.findLandmarkByName("Duf_Falls"));
        result.add(this.findLandmarkByName("Aqua_Park_Macedonia"));
        result.add(this.findLandmarkByName("Stone_town_of_Kuklica"));
        result.add(this.findLandmarkByName("Lešok_Monastery"));
        result.add(this.findLandmarkByName("Cocev_Kamen"));
        return result;
    }

    public Map<String, String> findLandmarkByName(String name) {
        String queryString = "DESCRIBE " + resource.substring(0, resource.length()-1) + name + ">";
        Query query = QueryFactory.create(queryString);
        Map<String, String> result = new HashMap<String, String>();
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            Model model = queryExecution.execDescribe();
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Statement nextStatement = stmtIterator.nextStatement();
                String predicate = nextStatement.getPredicate().toString();
                if (predicate.contains("label") && nextStatement.getLanguage().equals("en")) {
                    String label = nextStatement.getObject().asLiteral().toString();
                    result.put("label", label);
                } else if (predicate.contains("abstract") && nextStatement.getLanguage().equals("en")) {
                    String comment = nextStatement.getObject().asLiteral().toString();
                    result.put("abstract", comment);
                } else if (predicate.contains("thumbnail")) {
                    String thumbnail = nextStatement.getObject().asResource().getURI();
                    result.put("thumbnail", thumbnail);
                } else if (predicate.contains("depiction")) {
                    String depiction = nextStatement.getObject().asResource().getURI();
                    result.put("depiction", depiction);
                } else if (predicate.contains("caption") || predicate.contains("Caption")) {
                    String caption = nextStatement.getObject().asLiteral().toString();
                    result.put("caption", caption);
                } else if (predicate.contains("placeType")) {
                    String placeType = nextStatement.getObject().asResource().getURI();
                    result.put("placeTypeUri", placeType);
                } else if (predicate.contains("deathPlace")) {
                    String deathPlaceUri = nextStatement.getObject().asResource().getURI();
                    result.put("deathPlaceUri", deathPlaceUri);
                    String deathPlace = nextStatement.getObject().asResource().getLocalName();
                    result.put("deathPlace", deathPlace);
                } else if (predicate.contains("place")) {
                    String placeUri = nextStatement.getObject().asResource().getURI();
                    result.put("placeUri", placeUri);
                    String place = nextStatement.getObject().asResource().getLocalName();
                    result.put("place", place);
                } else if (predicate.contains("address")) {
                    String address = nextStatement.getObject().asLiteral().toString();
                    result.put("address", address);
                } else if (predicate.contains("bridgeCarries")) {
                    String bridgeCarries = nextStatement.getObject().asLiteral().toString();
                    result.put("bridgeCarries", bridgeCarries);
                } else if (predicate.contains("cost")) {
                    String cost = nextStatement.getObject().asLiteral().toString();
                    result.put("cost", cost);
                } else if (predicate.contains("crosses")) {
                    String crossesUri = nextStatement.getObject().asResource().getURI();
                    result.put("crossesUri", crossesUri);
                    String crosses = nextStatement.getObject().asResource().getLocalName();
                    result.put("crosses", crosses);
                } else if (predicate.contains("designer")) {
                    String designer = nextStatement.getObject().asLiteral().toString();
                    result.put("designer", designer);
                } else if (predicate.contains("width")) {
                    String width = nextStatement.getObject().asLiteral().toString();
                    result.put("width", width);
                } else if (predicate.contains("height")) {
                    String height = nextStatement.getObject().asLiteral().toString();
                    result.put("height", height);
                } else if (predicate.contains("highestLocation")) {
                    String highestLocation = nextStatement.getObject().asLiteral().toString();
                    result.put("highestLocation", highestLocation);
                } else if (predicate.contains("highest")) {
                    String highestUri = nextStatement.getObject().asResource().getURI();
                    result.put("highestUri", highestUri);
                    String highest = nextStatement.getObject().asResource().getLocalName();
                    result.put("highest", highest);
                } else if (predicate.contains("locationCountry")) {
                    String locationCountryUri = nextStatement.getObject().asResource().getURI();
                    result.put("locationCountryUri", locationCountryUri);
                    String locationCountry = nextStatement.getObject().asResource().getLocalName();
                    result.put("locationCountry", locationCountry);
                } else if (predicate.contains("locationTown")) {
                    String locationTownUri = nextStatement.getObject().asResource().getURI();
                    result.put("locationTownUri", locationTownUri);
                    String locationTown = nextStatement.getObject().asResource().getLocalName();
                    result.put("locationTown", locationTown);
                } else if (predicate.contains("locatedInArea")) {
                    String locatedInAreaUri = nextStatement.getObject().asResource().getURI();
                    result.put("locatedInAreaUri", locatedInAreaUri);
                    String locatedInArea = nextStatement.getObject().asResource().getLocalName();
                    result.put("locatedInArea", locatedInArea);
                } else if (predicate.contains("sourceCountry")) {
                    String sourceCountryUri = nextStatement.getObject().asResource().getURI();
                    result.put("sourceCountryUri", sourceCountryUri);
                    String sourceCountry = nextStatement.getObject().asResource().getLocalName();
                    result.put("sourceCountry", sourceCountry);
                } else if (predicate.contains("country")) {
                    String countryUri = nextStatement.getObject().asResource().getURI();
                    result.put("countryUri", countryUri);
                    String country = nextStatement.getObject().asResource().getLocalName();
                    result.put("country", country);
                } else if (predicate.contains("location")) {
                    String locationUri = nextStatement.getObject().asResource().getURI();
                    result.put("locationUri", locationUri);
                    String location = nextStatement.getObject().asResource().getLocalName();
                    result.put("location", location);
                } else if (predicate.contains("zooName")) {
                    String zooName = nextStatement.getObject().asLiteral().toString();
                    result.put("zooName", zooName);
                } else if (predicate.contains("numAnimals")) {
                    String numAnimals = nextStatement.getObject().asLiteral().toString();
                    result.put("numAnimals", numAnimals);
                } else if (predicate.contains("numSpecies")) {
                    String numSpecies = nextStatement.getObject().asLiteral().toString();
                    result.put("numSpecies", numSpecies);
                } else if (predicate.contains("dateOpened")) {
                    String dateOpened = nextStatement.getObject().asLiteral().toString();
                    result.put("dateOpened", dateOpened);
                } else if (predicate.contains("website")) {
                    String websiteUri = nextStatement.getObject().asResource().getURI();
                    result.put("websiteUri", websiteUri);
                    String website = nextStatement.getObject().asResource().getLocalName();
                    result.put("website", website);
                } else if (predicate.contains("buildingEndDate")) {
                    String buildingEndDate = nextStatement.getObject().asLiteral().toString();
                    result.put("buildingEndDate", buildingEndDate);
                } else if (predicate.contains("material")) {
                    String material = nextStatement.getObject().asLiteral().toString();
                    result.put("material", material);
                } else if (predicate.contains("regionCode")) {
                    String regionCode = nextStatement.getObject().asLiteral().toString();
                    result.put("regionCode", regionCode);
                } else if (predicate.contains("region")) {
                    String regionUri = nextStatement.getObject().asResource().getURI();
                    result.put("regionUri", regionUri);
                    String region = nextStatement.getObject().asResource().getLocalName();
                    result.put("region", region);
                } else if (predicate.contains("elevation")) {
                    String elevation = nextStatement.getObject().asLiteral().toString();
                    result.put("elevation", elevation);
                } else if (predicate.contains("architecturalStyle")) {
                    String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                    result.put("architecturalStyleUri", architecturalStyleUri);
                    String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                    result.put("architecturalStyle", architecturalStyle);
                } else if (predicate.contains("architectureStyle")) {
                    String architectureStyleUri = nextStatement.getObject().asResource().getURI();
                    result.put("architectureStyleUri", architectureStyleUri);
                    String architectureStyle = nextStatement.getObject().asResource().getLocalName();
                    result.put("architectureStyle", architectureStyle);
                } else if (predicate.contains("architectureType")) {
                    String architectureType = nextStatement.getObject().asLiteral().toString();
                    result.put("architectureType", architectureType);
                } else if (predicate.contains("architect")) {
                    String architect = nextStatement.getObject().asLiteral().toString();
                    result.put("architect", architect);
                } else if (predicate.contains("lakeType")) {
                    String lakeType = nextStatement.getObject().asLiteral().toString();
                    result.put("lakeType", lakeType);
                } else if (predicate.contains("stateType")) {
                    String stateType = nextStatement.getObject().asLiteral().toString();
                    result.put("stateType", stateType);
                } else if (predicate.contains("type")) {
                    String type = nextStatement.getObject().asLiteral().toString();
                    result.put("type", type);
                } else if (predicate.contains("client")) {
                    String clientUri = nextStatement.getObject().asResource().getURI();
                    result.put("clientUri", clientUri);
                    String client = nextStatement.getObject().asResource().getLocalName();
                    result.put("client", client);
                } else if (predicate.contains("mainContractor")) {
                    String mainContractorUri = nextStatement.getObject().asResource().getURI();
                    result.put("mainContractorUri", mainContractorUri);
                    String mainContractor = nextStatement.getObject().asResource().getLocalName();
                    result.put("mainContractor", mainContractor);
                } else if (predicate.contains("alternateNames")) {
                    String alternateNames = nextStatement.getObject().asLiteral().toString();
                    result.put("alternateNames", alternateNames);
                } else if (predicate.contains("significantBuilding")) {
                    String significantBuildingUri = nextStatement.getObject().asResource().getURI();
                    result.put("significantBuildingUri", significantBuildingUri);
                    String significantBuilding = nextStatement.getObject().asResource().getLocalName();
                    result.put("significantBuilding", significantBuilding);
                } else if (predicate.contains("religiousAffiliation")) {
                    String religiousAffiliationUri = nextStatement.getObject().asResource().getURI();
                    result.put("religiousAffiliationUri", religiousAffiliationUri);
                    String religiousAffiliation = nextStatement.getObject().asResource().getLocalName();
                    result.put("religiousAffiliation", religiousAffiliation);
                } else if (predicate.contains("district")) {
                    String districtUri = nextStatement.getObject().asResource().getURI();
                    result.put("districtUri", districtUri);
                    String district = nextStatement.getObject().asResource().getLocalName();
                    result.put("district", district);
                } else if (predicate.contains("yearCompleted")) {
                    String yearCompleted = nextStatement.getObject().asLiteral().toString();
                    result.put("yearCompleted", yearCompleted);
                } else if (predicate.contains("domeQuantity")) {
                    String domeQuantity = nextStatement.getObject().asLiteral().toString();
                    result.put("domeQuantity", domeQuantity);
                } else if (predicate.contains("minaretQuantity")) {
                    String minaretQuantity = nextStatement.getObject().asLiteral().toString();
                    result.put("minaretQuantity", minaretQuantity);
                } else if (predicate.contains("engineer")) {
                    String engineer = nextStatement.getObject().asLiteral().toString();
                    result.put("engineer", engineer);
                } else if (predicate.contains("formerName")) {
                    String formerName = nextStatement.getObject().asLiteral().toString();
                    result.put("formerName", formerName);
                } else if (predicate.contains("postalCode")) {
                    String postalCode = nextStatement.getObject().asLiteral().toString();
                    result.put("postalCode", postalCode);
                } else if (predicate.contains("builder")) {
                    String builderUri = nextStatement.getObject().asResource().getURI();
                    result.put("builderUri", builderUri);
                    String builder = nextStatement.getObject().asResource().getLocalName();
                    result.put("builder", builder);
                } else if (predicate.contains("controlledby")) {
                    String controlledbyUri = nextStatement.getObject().asResource().getURI();
                    result.put("controlledbyUri", controlledbyUri);
                    String controlledby = nextStatement.getObject().asResource().getLocalName();
                    result.put("controlledby", controlledby);
                } else if (predicate.contains("buildingStartDate")) {
                    String buildingStartDate = nextStatement.getObject().asLiteral().toString();
                    result.put("buildingStartDate", buildingStartDate);
                } else if (predicate.contains("routeStart")) {
                    String routeStartUri = nextStatement.getObject().asResource().getURI();
                    result.put("routeStartUri", routeStartUri);
                    String routeStart = nextStatement.getObject().asResource().getLocalName();
                    result.put("routeStart", routeStart);
                } else if (predicate.contains("easiestRoute")) {
                    String easiestRoute = nextStatement.getObject().asLiteral().toString();
                    result.put("easiestRoute", easiestRoute);
                } else if (predicate.contains("terminusB")) {
                    String terminusB = nextStatement.getObject().asLiteral().toString();
                    result.put("terminusB", terminusB);
                } else if (predicate.contains("openToPublic")) {
                    String openToPublic = nextStatement.getObject().asLiteral().toString();
                    result.put("openToPublic", openToPublic);
                } else if (predicate.contains("established")) {
                    String established = nextStatement.getObject().asLiteral().toString();
                    result.put("established", established);
                } else if (predicate.contains("director")) {
                    String director = nextStatement.getObject().asLiteral().toString();
                    result.put("director", director);
                } else if (predicate.contains("maximumDepth")) {
                    String maximumDepth = nextStatement.getObject().asLiteral().toString();
                    result.put("maximumDepth", maximumDepth);
                } else if (predicate.contains("averageDepth")) {
                    String averageDepth = nextStatement.getObject().asLiteral().toString();
                    result.put("averageDepth", averageDepth);
                } else if (predicate.contains("shoreLength")) {
                    String shoreLength = nextStatement.getObject().asLiteral().toString();
                    result.put("shoreLength", shoreLength);
                } else if (predicate.contains("length")) {
                    String length = nextStatement.getObject().asLiteral().toString();
                    result.put("length", length);
                } else if (predicate.contains("areaOfCatchment")) {
                    String areaOfCatchment = nextStatement.getObject().asLiteral().toString();
                    result.put("areaOfCatchment", areaOfCatchment);
                } else if (predicate.contains("areaTotal")) {
                    String areaTotal = nextStatement.getObject().asLiteral().toString();
                    result.put("areaTotal", areaTotal);
                } else if (predicate.contains("volume")) {
                    String volume = nextStatement.getObject().asLiteral().toString();
                    result.put("volume", volume);
                } else if (predicate.contains("nearest")) {
                    String nearestUri = nextStatement.getObject().asResource().getURI();
                    result.put("nearestUri", nearestUri);
                    String nearest = nextStatement.getObject().asResource().getLocalName();
                    result.put("nearest", nearest);
                } else if (predicate.contains("city")) {
                    String cityUri = nextStatement.getObject().asResource().getURI();
                    result.put("cityUri", cityUri);
                    String city = nextStatement.getObject().asResource().getLocalName();
                    result.put("city", city);
                } else if (predicate.contains("inflow")) {
                    String inflowUri = nextStatement.getObject().asResource().getURI();
                    result.put("inflowUri", inflowUri);
                    String inflow = nextStatement.getObject().asResource().getLocalName();
                    result.put("inflow", inflow);
                } else if (predicate.contains("outflow")) {
                    String outflowUri = nextStatement.getObject().asResource().getURI();
                    result.put("outflowUri", outflowUri);
                    String outflow = nextStatement.getObject().asResource().getLocalName();
                    result.put("outflow", outflow);
                } else if (predicate.contains("islands")) {
                    String islands = nextStatement.getObject().asLiteral().toString();
                    result.put("islands", islands);
                } else if (predicate.contains("island")) {
                    String islandUri = nextStatement.getObject().asResource().getURI();
                    result.put("islandUri", islandUri);
                    String island = nextStatement.getObject().asResource().toString();
                    result.put("island", island);
                } else if (predicate.contains("description")) {
                    String description = nextStatement.getObject().asLiteral().toString();
                    result.put("description", description);
                } else if (predicate.contains("openingYear")) {
                    String openingYear = nextStatement.getObject().asLiteral().toString();
                    result.put("openingYear", openingYear);
                } else if (predicate.contains("purpose")) {
                    String purpose = nextStatement.getObject().asLiteral().toString();
                    result.put("purpose", purpose);
                } else if (predicate.contains("status")) {
                    String status = nextStatement.getObject().asLiteral().toString();
                    result.put("status", status);
                } else if (predicate.contains("lake")) {
                    String lakeUri = nextStatement.getObject().asResource().getURI();
                    result.put("lakeUri", lakeUri);
                    String lake = nextStatement.getObject().asResource().getLocalName();
                    result.put("lake", lake);
                } else if (predicate.contains("part")) {
                    String partUri = nextStatement.getObject().asResource().getURI();
                    result.put("partUri", partUri);
                    String part = nextStatement.getObject().asResource().getLocalName();
                    result.put("part", part);
                } else if (predicate.contains("river")) {
                    String riverUri = nextStatement.getObject().asResource().getURI();
                    result.put("riverUri", riverUri);
                    String river = nextStatement.getObject().asResource().getLocalName();
                    result.put("river", river);
                } else if (predicate.contains("otherTitle")) {
                    String otherTitle = nextStatement.getObject().asLiteral().toString();
                    result.put("otherTitle", otherTitle);
                } else if (predicate.contains("nativeName") || predicate.contains("nameNative")) {
                    String nativeName = nextStatement.getObject().asLiteral().toString();
                    result.put("nativeName", nativeName);
                } else if (predicate.contains("author")) {
                    String authorUri = nextStatement.getObject().asResource().getURI();
                    result.put("authorUri", authorUri);
                    String author = nextStatement.getObject().asResource().getLocalName();
                    result.put("author", author);
                } else if (predicate.contains("dedicatedTo")) {
                    String dedicatedTo = nextStatement.getObject().asLiteral().toString();
                    result.put("dedicatedTo", dedicatedTo);
                } else if (predicate.contains("complete")) {
                    String complete = nextStatement.getObject().asLiteral().toString();
                    result.put("complete", complete);
                } else if (predicate.contains("designer")) {
                    String designerUri = nextStatement.getObject().asResource().getURI();
                    result.put("designerUri", designerUri);
                    String designer = nextStatement.getObject().asResource().getLocalName();
                    result.put("designer", designer);
                } else if (predicate.contains("cathedral")) {
                    String cathedralUri = nextStatement.getObject().asResource().getURI();
                    result.put("cathedralUri", cathedralUri);
                    String cathedral = nextStatement.getObject().asResource().getLocalName();
                    result.put("cathedral", cathedral);
                } else if (predicate.contains("created")) {
                    String created = nextStatement.getObject().asLiteral().toString();
                    result.put("created", created);
                } else if (predicate.contains("population")) {
                    String population = nextStatement.getObject().asLiteral().toString();
                    result.put("population", population);
                } else if (predicate.contains("countryAdminDevisionsTitle")) {
                    String countryAdminDevisionsTitleUri = nextStatement.getObject().asResource().getURI();
                    result.put("countryAdminDevisionsTitleUri", countryAdminDevisionsTitleUri);
                    String countryAdminDevisionsTitle = nextStatement.getObject().asResource().getLocalName();
                    result.put("countryAdminDevisionsTitle", countryAdminDevisionsTitle);
                } else if (predicate.contains("churches")) {
                    String churches = nextStatement.getObject().asLiteral().toString();
                    result.put("churches", churches);
                } else if (predicate.contains("dedication")) {
                    String dedication = nextStatement.getObject().asLiteral().toString();
                    result.put("dedication", dedication);
                } else if (predicate.contains("diocese")) {
                    String diocese = nextStatement.getObject().asLiteral().toString();
                    result.put("diocese", diocese);
                } else if (predicate.contains("abbot")) {
                    String abbot = nextStatement.getObject().asLiteral().toString();
                    result.put("abbot", abbot);
                } else if (predicate.contains("order")) {
                    String orderUri = nextStatement.getObject().asResource().getURI();
                    result.put("orderUri", orderUri);
                    String order = nextStatement.getObject().asResource().getLocalName();
                    result.put("order", order);
                } else if (predicate.contains("prior")) {
                    String priorUri = nextStatement.getObject().asResource().getURI();
                    result.put("priorUri", priorUri);
                    String prior = nextStatement.getObject().asResource().getLocalName();
                    result.put("prior", prior);
                } else if (predicate.contains("publicAccess")) {
                    String publicAccess = nextStatement.getObject().asLiteral().toString();
                    result.put("publicAccess", publicAccess);
                } else if (predicate.contains("relief")) {
                    String relief = nextStatement.getObject().asLiteral().toString();
                    result.put("relief", relief);
                } else if (predicate.contains("condition")) {
                    String condition = nextStatement.getObject().asLiteral().toString();
                    result.put("condition", condition);
                } else if (predicate.contains("mouthMountain")) {
                    String mouthMountainUri = nextStatement.getObject().asResource().getURI();
                    result.put("mouthMountainUri", mouthMountainUri);
                    String mouthMountain = nextStatement.getObject().asResource().getLocalName();
                    result.put("mouthMountain", mouthMountain);
                } else if (predicate.contains("mouthPlace")) {
                    String mouthPlaceUri = nextStatement.getObject().asResource().getURI();
                    result.put("mouthPlaceUri", mouthPlaceUri);
                    String mouthPlace = nextStatement.getObject().asResource().getLocalName();
                    result.put("mouthPlace", mouthPlace);
                } else if (predicate.contains("river")) {
                    String riverUri = nextStatement.getObject().asResource().getURI();
                    result.put("riverUri", riverUri);
                    String river = nextStatement.getObject().asResource().getLocalName();
                    result.put("river", river);
                } else if (predicate.contains("url")) {
                    String url = nextStatement.getObject().asResource().getURI();
                    result.put("url", url);
                } else if (predicate.contains("mouthPosition")) {
                    String mouthPositionUri = nextStatement.getObject().asResource().getURI();
                    result.put("mouthPositionUri", mouthPositionUri);
                    String mouthPosition = nextStatement.getObject().asResource().getLocalName();
                    result.put("mouthPosition", mouthPosition);
                } else if (predicate.contains("tributariesLeft")) {
                    String tributariesLeft = nextStatement.getObject().asLiteral().toString();
                    result.put("tributariesLeft", tributariesLeft);
                } else if (predicate.contains("tributariesRight")) {
                    String tributariesRight = nextStatement.getObject().asLiteral().toString();
                    result.put("tributariesRight", tributariesRight);
                } else if (predicate.contains("prominence")) {
                    String prominence = nextStatement.getObject().asLiteral().toString();
                    result.put("prominence", prominence);
                } else if (predicate.contains("headquarters")) {
                    String headquartersUri = nextStatement.getObject().asResource().getURI();
                    result.put("headquartersUri", headquartersUri);
                    String headquarters = nextStatement.getObject().asResource().getLocalName();
                    result.put("headquarters", headquarters);
                } else if (predicate.contains("mountainRange")) {
                    String mountainRangeUri = nextStatement.getObject().asResource().getURI();
                    result.put("mountainRangeUri", mountainRangeUri);
                    String mountainRange = nextStatement.getObject().asResource().getLocalName();
                    result.put("mountainRange", mountainRange);
                } else if (predicate.contains("geology")) {
                    String geology = nextStatement.getObject().asLiteral().toString();
                    result.put("geology", geology);
                } else if (predicate.contains("militaryUnit")) {
                    String militaryUnitUri = nextStatement.getObject().asResource().getURI();
                    result.put("militaryUnitUri", militaryUnitUri);
                    String militaryUnit = nextStatement.getObject().asResource().getLocalName();
                    result.put("militaryUnit", militaryUnit);
                }
            }
            return result;
        }
    }

    public List<Map<String, String>> findLandmarksPaged(int page, int size) {
        return findAllPaged("", page, size);
    }

    public List<Map<String, String>> findAllBeaches() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?beach WHERE { ?beach a dbo:Beach . }";
        return findAll(queryString);
    }

    public Map<String, String> findBeachByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("country", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("region", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("state")) {
                String stateUri = nextStatement.getObject().asResource().getURI();
                result.put("state", stateUri);
                String state = nextStatement.getObject().asResource().getLocalName();
                result.put("state", state);
            } else if (predicate.contains("municipality")) {
                String municipalityUri = nextStatement.getObject().asResource().getURI();
                result.put("municipalityUri", municipalityUri);
                String municipality = nextStatement.getObject().asResource().getLocalName();
                result.put("municipality", municipality);
            } else if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("length")) {
            //    Double length = nextStatement.getObject().asLiteral().getDouble();
                String length = nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("width")) {
            //    Double width = nextStatement.getObject().asLiteral().getDouble();
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("area")) {
            //    Double area = nextStatement.getObject().asLiteral().getDouble();
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("lengthOrientation")) {
                String lengthOrientation = nextStatement.getObject().asLiteral().toString();
                result.put("lengthOrientation", lengthOrientation);
            } else if (predicate.contains("widthOrientation")) {
                String widthOrientation = nextStatement.getObject().asLiteral().toString();
                result.put("widthOrientation", widthOrientation);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("description", depiction);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("visitationDate")) {
                String visitationDate = nextStatement.getObject().asLiteral().toString();
                result.put("visitationDate", visitationDate);
            } else if (predicate.contains(("elevation"))) {
            //    Double elevation = nextStatement.getObject().asLiteral().getDouble();
                String elevation = nextStatement.getObject().asLiteral().toString();
                result.put("elevation", elevation);
            } else if (predicate.contains("populationTotal")) {
            //    Integer populationTotal = nextStatement.getObject().asLiteral().getInt();
                String populationTotal = nextStatement.getObject().asLiteral().toString();
                result.put("populationTotal", populationTotal);
            } else if (predicate.contains("populationAsOf")) {
            //    Integer populationAsOf = nextStatement.getObject().asLiteral().getInt();
                String populationAsOf = nextStatement.getObject().asLiteral().toString();
                result.put("populationAsOf", populationAsOf);
            } else if (predicate.contains("district")) {
                String districtUri = nextStatement.getObject().asResource().getURI();
                result.put("district", districtUri);
                String district = nextStatement.getObject().asResource().getLocalName();
                result.put("district", district);
            } else if (predicate.contains("publiclyAccessible")) {
                String publiclyAccessible = nextStatement.getObject().asLiteral().toString();
                result.put("publiclyAccessible", publiclyAccessible);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllBeachesByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?beach WHERE { ?beach a dbo:Beach ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllCastles() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?castle WHERE { ?castle a dbo:Castle .  }";
        return findAll(queryString);
    }

    public Map<String, String> findCastleByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("buildingEndDate")) {
                String buildingEndDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingEndDate", buildingEndDate);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStyleUri", architecturalStyleUri);
                String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("floorArea")) {
            //    Double floorArea = nextStatement.getObject().asLiteral().getDouble();
                String floorArea = nextStatement.getObject().asLiteral().toString();
                result.put("floorArea", floorArea);
            } else if (predicate.contains("style")) {
                String styleUri = nextStatement.getObject().asResource().getURI();
                result.put("styleUri", styleUri);
                String style = nextStatement.getObject().asResource().getLocalName();
                result.put("style", style);
            } else if (predicate.contains("museum")) {
                String museumUri = nextStatement.getObject().asResource().getURI();
                result.put("museumUri", museumUri);
                String museum = nextStatement.getObject().asResource().getLocalName();
                result.put("museum", museum);
            } else if (predicate.contains("building")) {
                String buildingUri = nextStatement.getObject().asResource().getURI();
                result.put("buildingUri", buildingUri);
                String building = nextStatement.getObject().asResource().getLocalName();
                result.put("building", building);
            } else if (predicate.contains("residence")) {
                String residenceUri = nextStatement.getObject().asResource().getURI();
                result.put("residenceUri", residenceUri);
                String residence = nextStatement.getObject().asResource().getLocalName();
                result.put("residence", residence);
            } else if (predicate.contains("works")) {
                String worksUri = nextStatement.getObject().asResource().getURI();
                result.put("worksUri", worksUri);
                String works = nextStatement.getObject().asResource().getLocalName();
                result.put("works", works);
            } else if (predicate.contains("birthPlace")) {
                String birthPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("birthPlaceUri", birthPlaceUri);
                String birthPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("birthPlace", birthPlace);
            } else if (predicate.contains("deathPlace")) {
                String deathPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("deathPlaceUri", deathPlaceUri);
                String deathPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("deathPlace", deathPlace);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllCastlesByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?castle WHERE { ?castle a dbo:Castle ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllHistoricDistricts() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + " SELECT ?historicDistrict WHERE { ?historicDistrict dct:subject dbc:Historic_districts . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllHistoricDistrictsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + " PREFIX dbo: " + ontology + " SELECT ?historicalDistrict WHERE { ?historicDistrict dct:subject dbc:Historic_districts ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findHistoricDistrictByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("settlementType")) {
                String settlementType = nextStatement.getObject().asLiteral().toString();
                result.put("settlementType", settlementType);
            } else if (predicate.contains("nativeNameLang")) {
                String nativeNameLang = nextStatement.getObject().asLiteral().toString();
                result.put("nativeNameLang", nativeNameLang);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllMuseums() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?museum WHERE { ?museum a dbo:Museum . }";
        return findAll(queryString);
    }

    public Map<String, String> findMuseumByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().toString();
                result.put("depiction", depiction);
            } /* else if (predicate.contains("foundingYear")) {
                Date foundingYear = new Date(nextStatement.getObject().asLiteral().toString());
            } else if (predicate.contains("yearDemolished")) {
                Date yearDemolished = nextStatement.getObject().asLiteral().getDate();
            } else if (predicate.contains("openingDate")) {
                Date openingDate = nextStatement.getObject().asLiteral().getDate();
            } */ else if (predicate.contains("founder")) {
                String founder = nextStatement.getObject().asLiteral().toString();
                result.put("founder", founder);
            } else if (predicate.contains("locationCity")) {
                String locationCityUri = nextStatement.getObject().asResource().getURI();
                result.put("locationCityUri", locationCityUri);
                String locationCity = nextStatement.getObject().asResource().getLocalName();
                result.put("locationCity", locationCity);
            } else if (predicate.contains("regionServed")) {
                String regionServedUri = nextStatement.getObject().asResource().getURI();
                result.put("regionServedUri", regionServedUri);
                String regionServed = nextStatement.getObject().asResource().getLocalName();
                result.put("regionServed", regionServed);
            } else if (predicate.contains("numberOfLocations")) {
            //    Integer numberOfLocations = nextStatement.getObject().asLiteral().getInt();
                String numberOfLocations = nextStatement.getObject().asLiteral().toString();
                result.put("nummberOfLocations", numberOfLocations);
            } else if (predicate.contains("slogan")) {
                String slogan = nextStatement.getObject().asLiteral().toString();
                result.put("slogan", slogan);
            } else if (predicate.contains("product")) {
                String productUri = nextStatement.getObject().asResource().getURI();
                result.put("productUri", productUri);
                String product = nextStatement.getObject().asResource().getLocalName();
                result.put("product", product);
            } else if (predicate.contains("industry")) {
                String industryUri = nextStatement.getObject().asResource().getURI();
                result.put("industryUri", industryUri);
                String industry = nextStatement.getObject().asResource().getLocalName();
                result.put("industry", industry);
            } else if (predicate.contains("foundingPlace")) {
                String foundingPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("foundingPlaceUri", foundingPlaceUri);
                String foundingPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("foundingPlace", foundingPlace);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("owner")) {
                String ownerUri = nextStatement.getObject().asResource().getURI();
                result.put("ownerUri", ownerUri);
                String owner = nextStatement.getObject().asResource().getLocalName();
                result.put("owner", owner);
            } else if (predicate.contains("floorCount")) {
            //    Integer floorCount = nextStatement.getObject().asLiteral().getInt();
                String floorCount = nextStatement.getObject().asLiteral().toString();
                result.put("floorCount", floorCount);
            } else if (predicate.contains("floorArea")) {
            //    Double floorArea = nextStatement.getObject().asLiteral().getDouble();
                String floorArea = nextStatement.getObject().asLiteral().toString();
                result.put("floorArea", floorArea);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllMuseumsByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?museum WHERE { ?museum a dbo:Museum ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    /* public void findAllMuseumsByOpeningDate(Date openingDate) {

    } */

  /*  public void findAllSkiAreasAndResorts() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + " SELECT ?ski WHERE { ?ski dct:subject dbc:Ski_areas_and_resorts . }";
        findAll(queryString);
    } */

   /*  public void findAllSkiAreasAndResortsByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " PREFIX dct: " + terms + " PREFIX dbc: " + category + " SELECT ?ski WHERE { ?ski dct:subject dbc:Ski_areas_and_resorts ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    /* public void SkiAreaOrResortByName(String name) {
        String queryString = "PREFIX dbr: " + resource + " DESCRIBE dbr:" + name;
        Query query = QueryFactory.create(queryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            Model result = queryExecution.execDescribe();
            StmtIterator stmtIterator = result.listStatements();
            while (stmtIterator.hasNext()) {
                Statement nextStatement = stmtIterator.nextStatement();
                String comment = nextStatement.getLiteral("abstract").toString();
                String label = nextStatement.getLiteral("label").toString();
                String thumbnail = nextStatement.getResource("thumbnail").toString();
                String depiction = nextStatement.getResource("depiction").toString();
                String product = nextStatement.getResource("product").toString();
                String location = nextStatement.getResource("location").toString();
                String areaServed = nextStatement.getLiteral("areaServed").toString();
                String founder = nextStatement.getLiteral("founder").toString();
                Integer numberOfLocations = nextStatement.getLiteral("numberOfLocations").toString();
                Date foundingYear = nextStatement.getLiteral("foundingYear").getDate();
            }
        }
    } */

    public List<Map<String, String>> findAllMonuments() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?monument { WHERE ?monument a dbo:Monument . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllMonumentsByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?monument { WHERE ?monument a dbo:Monument ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findMonumentByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("yearCompleted")) {
            //    Integer yearCompleted = nextStatement.getObject().asLiteral().getInt();
                String yearCompleted = nextStatement.getObject().asLiteral().toString();
                result.put("yearCompleted", yearCompleted);
            } else if (predicate.contains("complete")) {
            //    Integer complete = nextStatement.getObject().asLiteral().getInt();
                String complete = nextStatement.getObject().asLiteral().toString();
                result.put("complete", complete);
            } else if (predicate.contains("year")) {
                String yearUri = nextStatement.getObject().asResource().getURI();
                result.put("yearUri", yearUri);
                String year = nextStatement.getObject().asResource().getLocalName();
                result.put("year", year);
            } else if (predicate.contains("dedicatedTo")) {
                String dedicatedTo = nextStatement.getObject().asLiteral().toString();
                result.put("dedicatedTo", dedicatedTo);
            } else if (predicate.contains("designer")) {
                String designer = nextStatement.getObject().asLiteral().toString();
                result.put("designer", designer);
            } else if (predicate.contains("length")) {
                String length = nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("location")) {
                String location = nextStatement.getObject().asLiteral().toString();
                result.put("location", location);
            } else if (predicate.contains("material")) {
                String material = nextStatement.getObject().asLiteral().toString();
                result.put("material", material);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("heightMetric")) {
            //    Double heightMetric = nextStatement.getObject().asLiteral().getDouble();
                String heightMetric = nextStatement.getObject().asLiteral().toString();
                result.put("heightMetric", heightMetric);
            } else if (predicate.contains("begin")) {
            //    Integer begin = nextStatement.getObject().asLiteral().getInt();
                String begin = nextStatement.getObject().asLiteral().toString();
                result.put("begin", begin);
            } else if (predicate.contains("fabricator")) {
                String fabricatorUri = nextStatement.getObject().asResource().getURI();
                result.put("fabricatorUri", fabricatorUri);
                String fabricator = nextStatement.getObject().asResource().getLocalName();
                result.put("fabricator", fabricator);
            } else if (predicate.contains("locale")) {
                String locale = nextStatement.getObject().asLiteral().toString();
                result.put("locale", locale);
            } else if (predicate.contains("officialName")) {
                String officialName = nextStatement.getObject().asLiteral().toString();
                result.put("officialName", officialName);
            } /* else if (predicate.contains("inauguration")) {
                Date inauguration = nextStatement.getObject().asLiteral().getDate();
            } */ else if (predicate.contains("owner")) {
                String ownerUri = nextStatement.getObject().asResource().getURI();
                result.put("ownerUri", ownerUri);
                String owner = nextStatement.getObject().asResource().getLocalName();
                result.put("owner", owner);
            } else if (predicate.contains("opened")) {
            //    Date opened = nextStatement.getObject().asLiteral().getDate();
                String opened = nextStatement.getObject().asLiteral().toString();
                result.put("opened", opened);
            } else if (predicate.contains("type")) {
                String type = nextStatement.getObject().asLiteral().toString();
                result.put("type", type);
            } else if (predicate.contains("features")) {
                String featuresUri = nextStatement.getObject().asResource().getURI();
                result.put("featuresUri", featuresUri);
                String features = nextStatement.getObject().asResource().getLocalName();
                result.put("features", features);
            } else if (predicate.contains("heritageDesignation")) {
                String heritageDesignationUri = nextStatement.getObject().asResource().getURI();
                result.put("heritageDesignationUri", heritageDesignationUri);
                String heritageDesignation = nextStatement.getObject().asResource().getLocalName();
                result.put("heritageDesignation", heritageDesignation);
            } else if (predicate.contains("address")) {
                String address = nextStatement.getObject().asLiteral().toString();
                result.put("address", address);
            } else if (predicate.contains("seatingCapacity")) {
            //    Integer seatingCapacity = nextStatement.getObject().asLiteral().getInt();
                String searchingCapacity = nextStatement.getObject().asLiteral().toString();
                result.put("searchingCapacity", searchingCapacity);
            } else if (predicate.contains("seatingType")) {
                String seatingType = nextStatement.getObject().asLiteral().toString();
                result.put("seatingType", seatingType);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllTowers() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?tower { WHERE ?tower a dbo:Tower . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllTowersByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?tower WHERE { ?tower a dbo:Tower ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findTowerByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("title")) {
                String titleUri = nextStatement.getObject().asResource().getURI();
                result.put("titleUri", titleUri);
                String title = nextStatement.getObject().asResource().getLocalName();
                result.put("title", title);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("height")) {
            //    Double height = nextStatement.getObject().asLiteral().getDouble();
                String height = nextStatement.getObject().asLiteral().toString();
                result.put("height", height);
            } else if (predicate.contains("openingDate")) {
            //    Date openingDate = nextStatement.getObject().asLiteral().getDate();
                String openingDate = nextStatement.getObject().asResource().toString();
                result.put("openingDate", openingDate);
            } else if (predicate.contains("condition")) {
                String condition = nextStatement.getObject().asLiteral().toString();
                result.put("condition", condition);
            } else if (predicate.contains("events")) {
                String events = nextStatement.getObject().asLiteral().toString();
                result.put("events", events);
            } else if (predicate.contains("openToPublic")) {
                String openToPublic = nextStatement.getObject().asLiteral().toString();
                result.put("openToPublic", openToPublic);
            } else if (predicate.contains("used")) {
                String usedUri = nextStatement.getObject().asResource().getURI();
                result.put("usedUri", usedUri);
                String used = nextStatement.getObject().asResource().getLocalName();
                result.put("used", used);
            } else if (predicate.contains("buildingEndDate")) {
                String buildingEndDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingEndDate", buildingEndDate);
            } else if (predicate.contains("division")) {
                String divisionUri = nextStatement.getObject().asResource().getURI();
                result.put("divisionUri", divisionUri);
                String division = nextStatement.getObject().asResource().getLocalName();
                result.put("division", division);
            } else if (predicate.contains("service")) {
                String serviceUri = nextStatement.getObject().asResource().getURI();
                result.put("serviceUri", serviceUri);
                String service = nextStatement.getObject().asResource().getLocalName();
                result.put("service", service);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStyleUri", architecturalStyleUri);
                String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("architecturalType")) {
                String architectureTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("architectureType", architectureTypeUri);
                String architectureType = nextStatement.getObject().asResource().getLocalName();
                result.put("architectureType", architectureType);
            } else if (predicate.contains("style")) {
                String styleUri = nextStatement.getObject().asResource().getURI();
                result.put("styleUri", styleUri);
                String style = nextStatement.getObject().asResource().getLocalName();
                result.put("style", style);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllParks() {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?park WHERE { ?park a dbo:Park . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllParksByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?park WHERE { ?park a dbo:Park ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findParkByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("artist")) {
                String artistUri = nextStatement.getObject().asResource().getURI();
                result.put("artistUri", artistUri);
                String artist = nextStatement.getObject().asResource().getLocalName();
                result.put("artist", artist);
            } else if (predicate.contains("occupation")) {
                String occupationUri = nextStatement.getObject().asResource().getURI();
                result.put("occupationUri", occupationUri);
                String occupation = nextStatement.getObject().asResource().getLocalName();
                result.put("occupation", occupation);
            } else if (predicate.contains("stadium")) {
                String stadiumUri = nextStatement.getObject().asResource().getURI();
                result.put("stadiumUri", stadiumUri);
                String stadium = nextStatement.getObject().asResource().getLocalName();
                result.put("stadium", stadium);
            } else if (predicate.contains("surface")) {
                String surfaceUri = nextStatement.getObject().asResource().getURI();
                result.put("surfaceUri", surfaceUri);
                String surface = nextStatement.getObject().asResource().getLocalName();
                result.put("surface", surface);
            } else if (predicate.contains("album")) {
                String albumUri = nextStatement.getObject().asResource().getURI();
                result.put("albumUri", albumUri);
                String album = nextStatement.getObject().asResource().getLocalName();
                result.put("album", album);
            } else if (predicate.contains("profession")) {
                String professionUri = nextStatement.getObject().asResource().getURI();
                result.put("professionUri", professionUri);
                String profession = nextStatement.getObject().asResource().getLocalName();
                result.put("profession", profession);
            } else if (predicate.contains("foundingDate")) {
            //    Date foundingYear = nextStatement.getObject().asLiteral().getDate();
                String foundingYear = nextStatement.getObject().asLiteral().toString();
                result.put("foundingYear", foundingYear);
            } else if (predicate.contains("areaServed")) {
                String areaServed = nextStatement.getObject().asLiteral().toString();
                result.put("areaServed", areaServed);
            } else if (predicate.contains("focus")) {
                String focus = nextStatement.getObject().asLiteral().toString();
                result.put("focus", focus);
            } else if (predicate.contains("headquarters")) {
                String headquarters = nextStatement.getObject().asLiteral().toString();
                result.put("headquarters", headquarters);
            } else if (predicate.contains("keyPeople")) {
                String keyPeople = nextStatement.getObject().asLiteral().toString();
                result.put("keyPeople", keyPeople);
            } else if (predicate.contains("mission")) {
                String mission = nextStatement.getObject().asLiteral().toString();
                result.put("mission", mission);
            } else if (predicate.contains("height")) {
                String height = nextStatement.getObject().asLiteral().toString();
                result.put("height", height);
            } else if (predicate.contains("location")) {
                String location = nextStatement.getObject().asLiteral().toString();
                result.put("location", location);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllAmusementParks() {
        String queryString = "PREFIX dbo: " + ontology + "PREFIX dbr: " + resource + "SELECT ?amusementPark WHERE { ?amusementPark dbo:type dbr:Amusement_park . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllAmusementParksByCountry(String country) {
        String queryString = "PREFIX dbo: " + ontology + " SELECT ?amusementPark WHERE { ?amusementPark a dbo:Park ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findAmusementParkByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().toString();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().toString();
                result.put("depiction", depiction);
            } else if (predicate.contains("endDate")) {
            //    Date endDate = nextStatement.getObject().asLiteral().getDate();
                String endDate = nextStatement.getObject().asLiteral().toString();
                result.put("endDate", endDate);
            } else if (predicate.contains("openingDate")) {
            //    Date openingDate = nextStatement.getObject().asLiteral().getDate();
                String openingDate = nextStatement.getObject().asLiteral().toString();
                result.put("openingDate", openingDate);
            } else if (predicate.contains("closingDate")) {
            //    Date closingDate = nextStatement.getObject().asLiteral().getDate();
                String closingDate = nextStatement.getObject().asLiteral().toString();
                result.put("closingDate", closingDate);
            } else if (predicate.contains("date")) {
            //    Date date = nextStatement.getObject().asLiteral().getDate();
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("species")) {
                String speciesUri = nextStatement.getObject().asResource().getURI();
                result.put("speciesUri", speciesUri);
                String species = nextStatement.getObject().asResource().getLocalName();
                result.put("species", species);
            } else if (predicate.contains("occupation")) {
                String occupationUri = nextStatement.getObject().asResource().getURI();
                result.put("occupationUri", occupationUri);
                String occupation = nextStatement.getObject().asResource().getLocalName();
                result.put("occupation", occupation);
            } else if (predicate.contains("industry")) {
                String industryUri = nextStatement.getObject().asResource().getURI();
                result.put("industryUri", industryUri);
                String industry = nextStatement.getObject().asResource().getLocalName();
                result.put("industry", industry);
            } else if (predicate.contains("floorArea")) {
            //    Integer floorArea = nextStatement.getObject().asLiteral().getInt();
                String floorArea = nextStatement.getObject().asLiteral().toString();
                result.put("floorArea", floorArea);
            } else if (predicate.contains("areaAcre")) {
            //    Double areaAcre = nextStatement.getObject().asLiteral().getDouble();
                String areaAcre = nextStatement.getObject().asLiteral().toString();
                result.put("areaAcre", areaAcre);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("owner")) {
                String ownerUri = nextStatement.getObject().asResource().getURI();
                result.put("ownerUri", ownerUri);
                String owner = nextStatement.getObject().asResource().getLocalName();
                result.put("owner", owner);
            } else if (predicate.contains("rides")) {
            //    Integer rides = nextStatement.getObject().asLiteral().getInt();
                String rides = nextStatement.getObject().asLiteral().toString();
                result.put("rides", rides);
            } else if (predicate.contains("address")) {
                String address = nextStatement.getObject().asLiteral().toString();
                result.put("address", address);
            } else if (predicate.contains("phoneNumber")) {
                String phoneNumber = nextStatement.getObject().asLiteral().toString();
                result.put("phoneNumber", phoneNumber);
            } else if (predicate.contains("founded")) {
            //    Integer founded = nextStatement.getObject().asLiteral().getInt();
                String founded = nextStatement.getObject().asLiteral().toString();
                result.put("founded", founded);
            } else if (predicate.contains("founder")) {
                String founder = nextStatement.getObject().asLiteral().toString();
                result.put("founder", founder);
            } else if (predicate.contains("city")) {
                String cityUri = nextStatement.getObject().asResource().getURI();
                result.put("cityUri", cityUri);
                String city = nextStatement.getObject().asResource().getLocalName();
                result.put("city", city);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("status")) {
                String status = nextStatement.getObject().asLiteral().toString();
                result.put("status", status);
            } else if (predicate.contains("theme")) {
                String themeUri = nextStatement.getObject().asResource().getURI();
                result.put("themeUri", themeUri);
                String theme = nextStatement.getObject().asResource().getLocalName();
                result.put("theme", theme);
            } else if (predicate.contains("service")) {
                String serviceUri = nextStatement.getObject().asResource().getURI();
                result.put("serviceUri", serviceUri);
                String service = nextStatement.getObject().asResource().getLocalName();
                result.put("service", service);
            } else if (predicate.contains("product")) {
                String productUri = nextStatement.getObject().asResource().getURI();
                result.put("productUri", productUri);
                String product = nextStatement.getObject().asResource().getLocalName();
                result.put("product", product);
            } else if (predicate.contains("genre")) {
                String genreUri = nextStatement.getObject().asResource().getURI();
                result.put("genreUri", genreUri);
                String genre = nextStatement.getObject().asResource().getLocalName();
                result.put("genre", genre);
            } else if (predicate.contains("otherFacilities")) {
                String otherFacilitiesUri = nextStatement.getObject().asResource().getURI();
                result.put("otherFacilitiesUri", otherFacilitiesUri);
                String otherFacilities = nextStatement.getObject().asResource().getLocalName();
                result.put("otherFacilities", otherFacilities);
            } else if (predicate.contains("operator")) {
                String operatorUri = nextStatement.getObject().asResource().getURI();
                result.put("operatorUri", operatorUri);
                String operator = nextStatement.getObject().asResource().getLocalName();
                result.put("operator", operator);
            } else if (predicate.contains("resort")) {
                String resort = nextStatement.getObject().asLiteral().toString();
                result.put("resort", resort);
            } else if (predicate.contains("season")) {
                String season = nextStatement.getObject().asLiteral().toString();
                result.put("season", season);
            } else if (predicate.contains("coasters")) {
             //   Integer coasters = nextStatement.getObject().asLiteral().getInt();
                String coasters = nextStatement.getObject().asLiteral().toString();
                result.put("coasters", coasters);
            } else if (predicate.contains("generalManager")) {
                String generalManager = nextStatement.getObject().asLiteral().toString();
                result.put("generalManager", generalManager);
            } else if (predicate.contains("area")) {
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("created")) {
            //    Integer created = nextStatement.getObject().asLiteral().getInt();
                String created = nextStatement.getObject().asLiteral().toString();
                result.put("created", created);
            } else if (predicate.contains("opened")) {
            //    Integer opened = nextStatement.getObject().asLiteral().getInt();
                String opened = nextStatement.getObject().asLiteral().toString();
                result.put("opened", opened);
            } else if (predicate.contains("manager")) {
                String managerUri = nextStatement.getObject().asResource().getURI();
                result.put("managerUri", managerUri);
                String manager = nextStatement.getObject().asResource().getLocalName();
                result.put("manager", manager);
            } else if (predicate.contains("venue")) {
                String venueUri = nextStatement.getObject().asResource().getURI();
                result.put("venueUri", venueUri);
                String venue = nextStatement.getObject().asResource().getLocalName();
                result.put("venue", venue);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllBuildings() {
        String queryString = "PREFIX dct: " + terms + "PREFIX dbc: " + category + "SELECT ?building WHERE { ?building dct:subject dbc:Folly_buildings .  }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllBuildingsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?building WHERE { ?building dct:subject dbc:Folly_buildings ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findBuildingByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("artist")) {
                String artistUri = nextStatement.getObject().asResource().getURI();
                result.put("artistUri", artistUri);
                String artist = nextStatement.getObject().asResource().getLocalName();
                result.put("artist", artist);
            } else if (predicate.contains("species")) {
                String species = nextStatement.getObject().asLiteral().toString();
                result.put("species", species);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStyleUri", architecturalStyleUri);
                String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("founded")) {
            //    Integer founded = nextStatement.getObject().asLiteral().getInt();
                String founded = nextStatement.getObject().asLiteral().toString();
                result.put("founded", founded);
            } else if (predicate.contains("width")) {
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("direction")) {
                String direction = nextStatement.getObject().asLiteral().toString();
                result.put("direction", direction);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("stateParty")) {
                String statePartyUri = nextStatement.getObject().asResource().getURI();
                result.put("statePartyUri", statePartyUri);
                String stateParty = nextStatement.getObject().asResource().getLocalName();
                result.put("stateParty", stateParty);
            } else if (predicate.contains("session")) {
            //    Integer session = nextStatement.getObject().asLiteral().getInt();
                String session = nextStatement.getObject().asResource().toString();
                result.put("session", session);
            } else if (predicate.contains("year")) {
            //    Integer year = nextStatement.getObject().asLiteral().getInt();
                String year = nextStatement.getObject().asLiteral().toString();
                result.put("year", year);
            }
        }
        return result;
    }

    /* public void findAllHikingTrails() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?hikingTrail WHERE { ?hikingTrail dct:subject dbc:Hiking_trails . }";
        findAll(queryString);
    } */

    /* public void findHikingTrailByName(String name) {
        String queryString = "PREFIX dbr: " + resource + " DESCRIBE dbr:" + name;
        Query query = QueryFactory.create(queryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            Model result = queryExecution.execDescribe();
            StmtIterator stmtIterator = result.listStatements();
            while (stmtIterator.hasNext()) {
                Statement nextStatement = stmtIterator.nextStatement();
                String comment = nextStatement.getLiteral("abstract").toString();
                String label = nextStatement.getLiteral("label").toString();
                String thumbnail = nextStatement.getResource("thumbnail").toString();
                String depiction = nextStatement.getResource("depiction").toString();
            }
        }
    } */

    /* public void findHikingTrailsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?hikingTrail WHERE { ?hikingTrail dct:subject dbc:Hiking_trails ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public List<Map<String, String>> findAllArchaeologicalSites() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?archaeologicalSite WHERE { ?archaeologicalSite dct:subject dbc:Archaeological_sites . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllArchaeologicalSitesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?archaeologicalSite WHERE { ?archaeologicalSite dct:subject dbc:Archaeological_sites ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findArchaeologicalSiteByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("location")) {
                String location = nextStatement.getObject().asResource().getURI();
                result.put("location", location);
            } else if (predicate.contains("register")) {
                String register = nextStatement.getObject().asLiteral().toString();
                result.put("register", register);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            } else if (predicate.contains("geology")) {
                String geologyUri = nextStatement.getObject().asResource().getURI();
                result.put("geologyUri", geologyUri);
                String geology = nextStatement.getObject().asResource().getLocalName();
                result.put("geology", geology);
            } else if (predicate.contains("entranceCount")) {
            //    Integer entranceCount = nextStatement.getObject().asLiteral().getInt();
                String entranceCount = nextStatement.getObject().asLiteral().toString();
                result.put("entranceCount", entranceCount);
            } else if (predicate.contains("discovery")) {
            //    Integer discovery = nextStatement.getObject().asLiteral().getInt();
                String discovery = nextStatement.getObject().asLiteral().toString();
                result.put("discovery", discovery);
            } else if (predicate.contains("access")) {
                String access = nextStatement.getObject().asLiteral().toString();
                result.put("access", access);
            } else if (predicate.contains("length")) {
            //    Double length = nextStatement.getObject().asLiteral().getDouble();
                String length= nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("depth")) {
            //    Double depth = nextStatement.getObject().asLiteral().getDouble();
                String depth = nextStatement.getObject().asLiteral().toString();
                result.put("depth", depth);
            } else if (predicate.contains("elevation")) {
            //    Double elevation = nextStatement.getObject().asLiteral().getDouble();
                String elevation = nextStatement.getObject().asLiteral().toString();
                result.put("elevation", elevation);
            } else if (predicate.contains("locatedInArea")) {
                String locatedInAreaUri = nextStatement.getObject().asResource().getURI();
                result.put("locatedInAreaUri", locatedInAreaUri);
                String locatedInArea = nextStatement.getObject().asResource().getLocalName();
                result.put("locatedInArea", locatedInArea);
            } else if (predicate.contains("mountainRange")) {
                String mountainRangeUri = nextStatement.getObject().asResource().getURI();
                result.put("mountainRangeUri", mountainRangeUri);
                String mountainRange = nextStatement.getObject().asResource().getLocalName();
                result.put("mountainRange", mountainRange);
            } else if (predicate.contains("age")) {
                String ageUri = nextStatement.getObject().asResource().getURI();
                result.put("ageUri", ageUri);
                String age = nextStatement.getObject().asResource().getLocalName();
                result.put("age", age);
            } else if (predicate.contains("builder")) {
                String builderUri = nextStatement.getObject().asResource().getURI();
                result.put("builderUri", builderUri);
                String builder = nextStatement.getObject().asResource().getLocalName();
                result.put("builder", builder);
            } else if (predicate.contains("description")) {
                String descriptionUri = nextStatement.getObject().asResource().getURI();
                result.put("descriptionUri", descriptionUri);
                String description = nextStatement.getObject().asResource().getLocalName();
                result.put("description", description);
            } else if (predicate.contains("added")) {
            //    Date added = nextStatement.getObject().asLiteral().getDate();
                String added = nextStatement.getObject().asLiteral().toString();
                result.put("added", added);
            } else if (predicate.contains("area")) {
            //    Double area = nextStatement.getObject().asLiteral().getDouble();
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("nearestCity")) {
                String nearestCityUri = nextStatement.getObject().asResource().getURI();
                result.put("nearestCityUri", nearestCityUri);
                String nearestCity = nextStatement.getObject().asResource().getLocalName();
                result.put("nearestCity", nearestCity);
            } else if (predicate.contains("governingBody")) {
                String governingBody = nextStatement.getObject().asLiteral().toString();
                result.put("governingBody", governingBody);
            } else if (predicate.contains("architecture")) {
                String architectureUri = nextStatement.getObject().asResource().getURI();
                result.put("architectureUri", architectureUri);
                String architecture = nextStatement.getObject().asResource().getLocalName();
                result.put("architecture", architecture);
            } else if (predicate.contains("municipality")) {
                String municipality = nextStatement.getObject().asLiteral().toString();
                result.put("municipality", municipality);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("district")) {
                String districtUri = nextStatement.getObject().asResource().getURI();
                result.put("districtUri", districtUri);
                String district = nextStatement.getObject().asResource().getLocalName();
                result.put("district", district);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllNaturalArches() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?naturalArch WHERE { ?naturalArch dct:subject dbc:Natural_arches . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllNaturalArchesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?naturalArch WHERE { ?naturalArch dct:subject dbc:Natural_arches ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findNaturalArchByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("footnotes")) {
                String footnotes = nextStatement.getObject().asLiteral().toString();
                result.put("footnotes", footnotes);
            } else if (predicate.contains("namedFor")) {
                String namedFor = nextStatement.getObject().asLiteral().toString();
                result.put("namedFor", namedFor);
            } else if (predicate.contains("populationAsOf")) {
            //    Integer populationAsOf = nextStatement.getObject().asLiteral().getInt();
                String populationAsOf = nextStatement.getObject().asLiteral().toString();
                result.put("populationAsOf", populationAsOf);
            } else if (predicate.contains("part")) {
                String part = nextStatement.getObject().asLiteral().toString();
                result.put("part", part);
            } else if (predicate.contains("areaTotal")) {
            //    Double areaTotal = nextStatement.getObject().asLiteral().getDouble();
                String areaTotal = nextStatement.getObject().asLiteral().toString();
                result.put("areaTotal", areaTotal);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("established")) {
            //    Integer established = nextStatement.getObject().asLiteral().getInt();
                String established = nextStatement.getObject().asLiteral().toString();
                result.put("established", established);
            } else if (predicate.contains("mountainRange")) {
                String mountainRangeUri = nextStatement.getObject().asResource().getURI();
                result.put("mountainRangeUri", mountainRangeUri);
                String mountainRange = nextStatement.getObject().asResource().getLocalName();
                result.put("mountainRange", mountainRange);
            } else if (predicate.contains("recordedIn")) {
                String recordedInUri = nextStatement.getObject().asResource().getURI();
                result.put("recordedInUri", recordedInUri);
                String recordedIn = nextStatement.getObject().asResource().getLocalName();
                result.put("recordedIn", recordedIn);
            } else if (predicate.contains("parent")) {
                String parentUri = nextStatement.getObject().asResource().getURI();
                result.put("parentUri", parentUri);
                String parent = nextStatement.getObject().asResource().getLocalName();
                result.put("parent", parent);
            } else if (predicate.contains("stateParty")) {
                String stateParty = nextStatement.getObject().asLiteral().toString();
                result.put("stateParty", stateParty);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            }
        }
        return result;
    }


    public List<Map<String, String>> findAllResorts() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?resort WHERE { ?resort dct:subject dbc:Resorts . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllResortsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?resort WHERE { ?resort dct:subject dbc:Resorts ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findResortByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("industry")) {
                String industryUri = nextStatement.getObject().asResource().getURI();
                result.put("industryUri", industryUri);
                String industry = nextStatement.getObject().asResource().getLocalName();
                result.put("industry", industry);
            } else if (predicate.contains("product")) {
                String productUri = nextStatement.getObject().asResource().getURI();
                result.put("productUri", productUri);
                String product = nextStatement.getObject().asResource().getLocalName();
                result.put("product", product);
            } else if (predicate.contains("service")) {
                String serviceUri = nextStatement.getObject().asResource().getURI();
                result.put("serviceUri", serviceUri);
                String service = nextStatement.getObject().asResource().getLocalName();
                result.put("service", service);
            } else if (predicate.contains("casinoType")) {
                String casinoTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("casinoTypeUri", casinoTypeUri);
                String casinoType = nextStatement.getObject().asResource().getLocalName();
                result.put("casinoType", casinoType);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("founded")) {
            //    Integer founded = nextStatement.getObject().asLiteral().getInt();
                String founded = nextStatement.getObject().asLiteral().toString();
                result.put("founded", founded);
            } else if (predicate.contains("managingDirector")) {
                String managingDirector = nextStatement.getObject().asLiteral().toString();
                result.put("managingDirector", managingDirector);
            } else if (predicate.contains("parentCompany")) {
                String parentCompanyUri = nextStatement.getObject().asResource().getURI();
                result.put("parentCompanyUri", parentCompanyUri);
                String parentCompany = nextStatement.getObject().asResource().getLocalName();
                result.put("parentCompany", parentCompany);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllTriumphalArches() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?triumphalArch WHERE { ?triumphalArch dct:subject dbc:Triumphal_arches . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllTriumphalArchesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?triumphalArch WHERE { ?triumphalArch dct:subject dbc:Triumphal_arches ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findTriumphalArchByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("inaugurationDate")) {
                Integer inaugurationDate = nextStatement.getObject().asLiteral().getInt();
            } else if (predicate.contains("renovationDate")) {
            //    Date renovationDate = nextStatement.getObject().asLiteral().getDate();
                String renovationDate = nextStatement.getObject().asLiteral().toString();
                result.put("renovationDate", renovationDate);
            } else if (predicate.contains("openingDate")) {
            //    Date openingDate = nextStatement.getObject().asLiteral().getDate();
                String openingDate = nextStatement.getObject().asLiteral().toString();
                result.put("openingDate", openingDate);
            } else if (predicate.contains("openedDate")) {
            //    Date openedDate = nextStatement.getObject().asLiteral().getDate();
                String openedDate = nextStatement.getObject().asLiteral().toString();
                result.put("openedDate", openedDate);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("city")) {
                String cityUri = nextStatement.getObject().asResource().getURI();
                result.put("cityUri", cityUri);
                String city = nextStatement.getObject().asResource().getLocalName();
                result.put("city", city);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStyleUri", architecturalStyleUri);
                String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("architect")) {
                String architect = nextStatement.getObject().asLiteral().toString();
                result.put("architect", architect);
            } else if (predicate.contains("structuralEngineer")) {
                String structuralEngineer = nextStatement.getObject().asLiteral().toString();
                result.put("structuralEngineer", structuralEngineer);
            } else if (predicate.contains("width")) {
            //    Integer width = nextStatement.getObject().asLiteral().getInt();
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("height")) {
            //    Integer height = nextStatement.getObject().asLiteral().getInt();
                String height = nextStatement.getObject().asLiteral().toString();
                result.put("height", height);
            } else if (predicate.contains("awards")) {
                String awards = nextStatement.getObject().asLiteral().toString();
                result.put("awards", awards);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("completed")) {
                Integer completed = nextStatement.getObject().asLiteral().getInt();
            } else if (predicate.contains("buildingStartDate")) {
            //    Date buildingStartDate = nextStatement.getObject().asLiteral().getDate();
                String buildingstartDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingStartDate", buildingstartDate);
            } else if (predicate.contains("context")) {
                String context = nextStatement.getObject().asLiteral().toString();
                result.put("context", context);
            } else if (predicate.contains("material")) {
                String material = nextStatement.getObject().asLiteral().toString();
                result.put("material", material);
            } else if (predicate.contains("title")) {
                String title = nextStatement.getObject().asLiteral().toString();
                result.put("title", title);
            } else if (predicate.contains("dedicatedTo")) {
                String dedicatedTo = nextStatement.getObject().asLiteral().toString();
                result.put("dedicatedTo", dedicatedTo);
            } else if (predicate.contains("open")) {
            //    Date open = nextStatement.getObject().asLiteral().getDate();
                String open = nextStatement.getObject().asLiteral().toString();
                result.put("open", open);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("address")) {
                String address = nextStatement.getObject().asLiteral().toString();
                result.put("address", address);
            } else if (predicate.contains("cost")) {
                String cost = nextStatement.getObject().asLiteral().toString();
                result.put("cost", cost);
            } else if (predicate.contains("client")) {
                String clientUri = nextStatement.getObject().asResource().getURI();
                result.put("clientUri", clientUri);
                String client = nextStatement.getObject().asResource().getLocalName();
                result.put("client", client);
            } else if (predicate.contains("mainContractor")) {
                String mainContractorUri = nextStatement.getObject().asResource().getURI();
                result.put("mainContractorUri", mainContractorUri);
                String mainContractor = nextStatement.getObject().asResource().getLocalName();
                result.put("mainContractor", mainContractor);
            } else if (predicate.contains("excavations")) {
            //    Integer excavations = nextStatement.getObject().asLiteral().getInt();
                String excavations = nextStatement.getObject().asLiteral().toString();
                result.put("excavations", excavations);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("cultures")) {
                String culturesUri = nextStatement.getObject().asResource().getURI();
                result.put("culturesUri", culturesUri);
                String cultures = nextStatement.getObject().asResource().getLocalName();
                result.put("cultures", cultures);
            } else if (predicate.contains("condition")) {
                String condition = nextStatement.getObject().asLiteral().toString();
                result.put("condition", condition);
            } else if (predicate.contains("builder")) {
                String builder = nextStatement.getObject().asLiteral().toString();
                result.put("builder", builder);
            } else if (predicate.contains("built")) {
                String built = nextStatement.getObject().asLiteral().toString();
                result.put("built", built);
            } else if (predicate.contains("archaeologists")) {
                String archaeologists = nextStatement.getObject().asLiteral().toString();
                result.put("archaeologists", archaeologists);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllUndergroundCities() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?undergroundCity WHERE { ?undergroundCity dct:subject dbc:Underground_cities . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllUndergroundCitiesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?undergroundCity WHERE { ?undergroundCity dct:subject dbc:Underground_cities ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findUndergroundCityByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("headquarter")) {
                String headquarterUri = nextStatement.getObject().asResource().getURI();
                result.put("headquarterUri", headquarterUri);
                String headquarter = nextStatement.getObject().asResource().getLocalName();
                result.put("headquarter", headquarter);
            } else if (predicate.contains("locationCity")) {
                String locationCityUri = nextStatement.getObject().asResource().getURI();
                result.put("locationCityUri", locationCityUri);
                String locationCity = nextStatement.getObject().asResource().getLocalName();
                result.put("locationCity", locationCity);
            } else if (predicate.contains("significantBuilding")) {
                String significantBuildingUri = nextStatement.getObject().asResource().getURI();
                result.put("significantBuildingUri", significantBuildingUri);
                String significantBuilding = nextStatement.getObject().asResource().getLocalName();
                result.put("significantBuilding", significantBuilding);
            } else if (predicate.contains("architecture")) {
                String architectureUri = nextStatement.getObject().asResource().getURI();
                result.put("architectureUri", architectureUri);
                String architecture = nextStatement.getObject().asResource().getLocalName();
                result.put("architecture", architecture);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("source")) {
                String source = nextStatement.getObject().asLiteral().toString();
                result.put("source", source);
            } else if (predicate.contains("state")) {
                String stateUri = nextStatement.getObject().asResource().getURI();
                result.put("stateUri", stateUri);
                String state = nextStatement.getObject().asResource().getLocalName();
                result.put("state", state);
            } else if (predicate.contains("establishedDate")) {
                String establishedDate = nextStatement.getObject().asLiteral().toString();
                result.put("establishedDate", establishedDate);
            } else if (predicate.contains("established")) {
            //    Integer established = nextStatement.getObject().asLiteral().getInt();
                String established = nextStatement.getObject().asLiteral().toString();
                result.put("established", established);
            } else if (predicate.contains("system")) {
                String system = nextStatement.getObject().asLiteral().toString();
                result.put("system", system);
            } else if (predicate.contains("areaCode")) {
                String areaCode = nextStatement.getObject().asLiteral().toString();
                result.put("areaCode", areaCode);
            } else if (predicate.contains("postalCode")) {
                String postalCode = nextStatement.getObject().asLiteral().toString();
                result.put("postalCode", postalCode);
            } else if (predicate.contains("populationTotal")) {
            //    Integer populationTotal = nextStatement.getObject().asLiteral().getInt();
                String populationTotal = nextStatement.getObject().asLiteral().toString();
                result.put("populationTotal", populationTotal);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("added")) {
            //    Date added = nextStatement.getObject().asLiteral().getDate();
                String added = nextStatement.getObject().asLiteral().toString();
                result.put("added", added);
            } else if (predicate.contains("architect")) {
                String architect = nextStatement.getObject().asLiteral().toString();
                result.put("architect", architect);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("partOf")) {
                String partOfUri = nextStatement.getObject().asResource().getURI();
                result.put("partOfUri", partOfUri);
                String partOf = nextStatement.getObject().asResource().getLocalName();
                result.put("partOf", partOf);
            } else if (predicate.contains("birthPlace")) {
                String birthPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("birthPlaceUri", birthPlaceUri);
                String birthPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("birthPlace", birthPlace);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("stateParty")) {
                String statePartyUri = nextStatement.getObject().asResource().getURI();
                result.put("statePartyUri", statePartyUri);
                String stateParty = nextStatement.getObject().asResource().getLocalName();
                result.put("stateParty", stateParty);
            } else if (predicate.contains("year")) {
            //    Integer year = nextStatement.getObject().asLiteral().getInt();
                String year = nextStatement.getObject().asLiteral().toString();
                result.put("year", year);
            } else if (predicate.contains("deathPlace")) {
                String deathPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("deathPlaceUri", deathPlaceUri);
                String deathPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("deathPlace", deathPlace);
            } else if (predicate.contains("spokenIn")) {
                String spokenInUri = nextStatement.getObject().asResource().getURI();
                result.put("spokenInUri", spokenInUri);
                String spokenIn = nextStatement.getObject().asResource().getLocalName();
                result.put("spokenIn", spokenIn);
            } else if (predicate.contains("garrison")) {
                String garrisonUri = nextStatement.getObject().asResource().getURI();
                result.put("garrisonUri", garrisonUri);
                String garrison = nextStatement.getObject().asResource().getLocalName();
                result.put("garrison", garrison);
            } else if (predicate.contains("place")) {
                String placeUri = nextStatement.getObject().asResource().getURI();
                result.put("placeUri", placeUri);
                String place = nextStatement.getObject().asResource().getLocalName();
                result.put("place", place);
            } else if (predicate.contains("picture")) {
                String picture = nextStatement.getObject().asResource().getURI();
                result.put("picture", picture);
            } else if (predicate.contains("title")) {
                String titleUri = nextStatement.getObject().asResource().getURI();
                result.put("titleUri", titleUri);
                String title = nextStatement.getObject().asResource().getLocalName();
                result.put("title", title);
            } else if (predicate.contains("regiontown")) {
                String regionTownUri = nextStatement.getObject().asResource().getURI();
                result.put("regionTownUri", regionTownUri);
                String regionTown = nextStatement.getObject().asResource().getLocalName();
                result.put("regionTown", regionTown);
            } else if (predicate.contains("openingDate")) {
            //    Integer openingDate = nextStatement.getObject().asLiteral().getInt();
                String openingDate = nextStatement.getObject().asLiteral().toString();
                result.put("openingDate", openingDate);
            } else if (predicate.contains("foundingDate")) {
            //    Date foundingDate = nextStatement.getObject().asLiteral().getDate();
                String foundigDate = nextStatement.getObject().asLiteral().toString();
                result.put("foundingDate", foundigDate);
            } else if (predicate.contains("foundingYear")) {
            //    Date foundingYear = nextStatement.getObject().asLiteral().getDate();
                String foundingYear = nextStatement.getObject().asLiteral().toString();
                result.put("foundingYear", foundingYear);
            } else if (predicate.contains("closingDate")) {
            //    Integer closingDate = nextStatement.getObject().asLiteral().getInt();
                String closingDate = nextStatement.getObject().asLiteral().toString();
                result.put("closingDate", closingDate);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("floors")) {
            //    Integer floors = nextStatement.getObject().asLiteral().getInt();
                String floors = nextStatement.getObject().asLiteral().toString();
                result.put("floors", floors);
            } else if (predicate.contains("connectedLandmarks")) {
                String connectedLandmarksUri = nextStatement.getObject().asResource().getURI();
                result.put("connectedLandmarksUri", connectedLandmarksUri);
                String connectedLandmarks = nextStatement.getObject().asResource().getURI();
                result.put("connectedLandmarks", connectedLandmarks);
            } else if (predicate.contains("parking")) {
            //    Integer parking = nextStatement.getObject().asLiteral().getInt();
                String parking = nextStatement.getObject().asLiteral().toString();
                result.put("parking", parking);
            } else if (predicate.contains("reason")) {
                String reason = nextStatement.getObject().asLiteral().toString();
                result.put("reason", reason);
            } else if (predicate.contains("industry")) {
                String industryUri = nextStatement.getObject().asResource().getURI();
                result.put("industryUri", industryUri);
                String industry = nextStatement.getObject().asResource().getLocalName();
                result.put("industry", industry);
            } else if (predicate.contains("owner")) {
                String owner = nextStatement.getObject().asLiteral().toString();
                result.put("owner", owner);
            } else if (predicate.contains("manager")) {
                String manager = nextStatement.getObject().asLiteral().toString();
                result.put("manager", manager);
            } else if (predicate.contains("developer")) {
                String developer = nextStatement.getObject().asLiteral().toString();
                result.put("developer", developer);
            } else if (predicate.contains("abandoned")) {
                String abandoned = nextStatement.getObject().asLiteral().toString();
                result.put("abandoned", abandoned);
            } else if (predicate.contains("excavations")) {
            //    Integer excavations = nextStatement.getObject().asLiteral().getInt();
                String excavation = nextStatement.getObject().asLiteral().toString();
                result.put("excavation", excavation);
            } else if (predicate.contains("material")) {
                String material = nextStatement.getObject().asLiteral().toString();
                result.put("material", material);
            } else if (predicate.contains("relief")) {
                String relief = nextStatement.getObject().asLiteral().toString();
                result.put("relief", relief);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllShowCaves() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllShowCavesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findShowCaveByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
            } else if (predicate.contains("discovery")) {
            //    Integer discovery = nextStatement.getObject().asLiteral().getInt();
                String discovery = nextStatement.getObject().asLiteral().toString();
                result.put("discovery", discovery);
            } else if (predicate.contains("length")) {
            //    Double length = nextStatement.getObject().asLiteral().getDouble();
                String length = nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("depth")) {
            //    Double depth = nextStatement.getObject().asLiteral().getDouble();
                String depth = nextStatement.getObject().asLiteral().toString();
                result.put("depth", depth);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            } else if (predicate.contains("locatedInArea")) {
                String locatedInAreaUri = nextStatement.getObject().asResource().getURI();
                result.put("locatedInAreaUri", locatedInAreaUri);
                String locatedInArea = nextStatement.getObject().asResource().getLocalName();
                result.put("locatedInArea", locatedInArea);
            } else if (predicate.contains("entranceCount")) {
            //    Integer entranceCount = nextStatement.getObject().asLiteral().getInt();
                String entranceCount = nextStatement.getObject().asLiteral().toString();
                result.put("entranceCount", entranceCount);
            } else if (predicate.contains("geology")) {
                String geologyUri = nextStatement.getObject().asResource().getURI();
                result.put("geologyUri", geologyUri);
                String geology = nextStatement.getObject().asResource().getLocalName();
                result.put("geology", geology);
            } else if (predicate.contains("lighting")) {
                String lighting = nextStatement.getObject().asLiteral().toString();
                result.put("lighting", lighting);
            } else if (predicate.contains("access")) {
                String access = nextStatement.getObject().asLiteral().toString();
                result.put("access", access);
            } else if (predicate.contains("altitude")) {
                String altitude = nextStatement.getObject().asLiteral().toString();
                result.put("altitude", altitude);
            } else if (predicate.contains("features")) {
                String features = nextStatement.getObject().asLiteral().toString();
                result.put("features", features);
            } else if (predicate.contains("nearestCity")) {
                String nearestCityUri = nextStatement.getObject().asResource().getURI();
                result.put("nearestCityUri", nearestCityUri);
                String nearestCity = nextStatement.getObject().asResource().getLocalName();
                result.put("nearestCity", nearestCity);
            } else if (predicate.contains("language")) {
                String languageUri = nextStatement.getObject().asResource().getURI();
                result.put("languageUri", languageUri);
                String language = nextStatement.getObject().asResource().getLocalName();
                result.put("language", language);
            } else if (predicate.contains("translation")) {
                String translation = nextStatement.getObject().asLiteral().toString();
                result.put("translation", translation);
            } else if (predicate.contains("relief")) {
                String relief = nextStatement.getObject().asLiteral().toString();
                result.put("relief", relief);
            } else if (predicate.contains("visitors")) {
            //    Integer visitors = nextStatement.getObject().asLiteral().getInt();
                String visitors = nextStatement.getObject().asLiteral().toString();
                result.put("visitors", visitors);
            } else if (predicate.contains("routeStart")) {
                String routeStartUri = nextStatement.getObject().asResource().getURI();
                result.put("routeStartUri", routeStartUri);
                String routeStart = nextStatement.getObject().asResource().getLocalName();
                result.put("routeStart", routeStart);
            } else if (predicate.contains("routeEnd")) {
                String routeEndUri = nextStatement.getObject().asResource().getURI();
                result.put("routeEndUri", routeEndUri);
                String routeEnd = nextStatement.getObject().asResource().getLocalName();
                result.put("routeEnd", routeEnd);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllHeraldicSites() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?heraldicSite WHERE { ?heraldicSite dct:subject dbc:Heraldic_sites . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllHeraldicSitesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?heraldicSite WHERE { ?heraldicSite dct:subject dbc:Heraldic_sites ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findHeraldicSiteByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("comment")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyleUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStyleUri", architecturalStyleUri);
                String architecturalStyle = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("architect")) {
                String architectUri = nextStatement.getObject().asResource().getURI();
                result.put("architectUri", architectUri);
                String architect = nextStatement.getObject().asResource().getLocalName();
                result.put("architect", architect);
            } else if (predicate.contains("denomination")) {
                String denominationUri = nextStatement.getObject().asResource().getURI();
                result.put("denominationUri", denominationUri);
                String denomination = nextStatement.getObject().asResource().getLocalName();
                result.put("denomination", denomination);
            } else if (predicate.contains("diocese")) {
                String dioceseUri = nextStatement.getObject().asResource().getURI();
                result.put("dioceseUri", dioceseUri);
                String diocese = nextStatement.getObject().asResource().getLocalName();
                result.put("diocese", diocese);
            } else if (predicate.contains("heritageDesignation")) {
                String heritageDesignation = nextStatement.getObject().asLiteral().toString();
                result.put("heritageDesignation", heritageDesignation);
            } else if (predicate.contains("landScape")) {
                String landScape = nextStatement.getObject().asLiteral().toString();
                result.put("landScape", landScape);
            } else if (predicate.contains("restingPlace")) {
                String restingPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("restingPlaceUri", restingPlaceUri);
                String restingPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("restingPlace", restingPlace);
            } else if (predicate.contains("museum")) {
                String museumUri = nextStatement.getObject().asResource().getURI();
                result.put("museumUri", museumUri);
                String museum = nextStatement.getObject().asResource().getLocalName();
                result.put("museum", museum);
            } else if (predicate.contains("completedDate")) {
            //    Integer completedDate = nextStatement.getObject().asLiteral().getInt();
                String completedDate = nextStatement.getObject().asLiteral().toString();
                result.put("completedDate", completedDate);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllPalaces() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?palace WHERE { ?palace dct:subject dbc:Palaces . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllPalacesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?palace WHERE { ?palace dct:subject dbc:Palaces ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findPalaceByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("theme")) {
                String themeUri = nextStatement.getObject().asResource().getURI();
                result.put("themeUri", themeUri);
                String theme = nextStatement.getObject().asResource().getLocalName();
                result.put("theme", theme);
            } else if (predicate.contains("classification")) {
                String classificationUri = nextStatement.getObject().asResource().getURI();
                result.put("classificationUri", classificationUri);
                String classification = nextStatement.getObject().asResource().getLocalName();
                result.put("classification", classification);
            } else if (predicate.contains("structuralSystem")) {
                String structuralSystemUri = nextStatement.getObject().asResource().getURI();
                result.put("structuralSystemUri", structuralSystemUri);
                String structuralSystem = nextStatement.getObject().asResource().getLocalName();
                result.put("structuralSystem", structuralSystem);
            } else if (predicate.contains("width")) {
            //    Integer width = nextStatement.getObject().asLiteral().getInt();
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("footer")) {
                String footer = nextStatement.getObject().asLiteral().toString();
                result.put("footer", footer);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllRoyalResidences() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?royalResidence WHERE { ?royalResidence dct:subject dbc:Royal_residences . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllRoyalResidencesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?royalResidence WHERE { ?royalResidence dct:subject dbc:Royal_residences ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findRoyalResidenceByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("client")) {
                String client = nextStatement.getObject().asLiteral().toString();
                result.put("client", client);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("buildingStartDate")) {
                String buildingStartDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingStartDate", buildingStartDate);
            } else if (predicate.contains("buildingEndDate")) {
                String buildingEndDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingendDate", buildingEndDate);
            } else if (predicate.contains("formerName")) {
                String formerName = nextStatement.getObject().asLiteral().toString();
                result.put("formerName", formerName);
            } else if (predicate.contains("material")) {
                String materialUri = nextStatement.getObject().asResource().getURI();
                result.put("materialUri", materialUri);
                String material = nextStatement.getObject().asResource().getLocalName();
                result.put("material", material);
            } else if (predicate.contains("owner")) {
                String owner = nextStatement.getObject().asLiteral().toString();
                result.put("owner", owner);
            } else if (predicate.contains("status")) {
                String status = nextStatement.getObject().asLiteral().toString();
                result.put("status", status);
            } else if (predicate.contains("alternateNames")) {
                String alternateNames = nextStatement.getObject().asLiteral().toString();
                result.put("alternateNames", alternateNames);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllNatureReserves() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?natureReserve WHERE { ?natureReserve dct:subject dbc:Nature_reserves . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllNatureReservesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?natureReserve WHERE { ?natureReserve dct:subject dbc:Nature_reserves ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findNatureReserveByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("dateOpened")) {
                String dateOpened = nextStatement.getObject().asLiteral().toString();
                result.put("dateOpened", dateOpened);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("exhibits")) {
                String exhibitsUri = nextStatement.getObject().asResource().getURI();
                result.put("exhibitsUri", exhibitsUri);
                String exhibits = nextStatement.getObject().asResource().getLocalName();
                result.put("exhibits", exhibits);
            } else if (predicate.contains("zooName")) {
                String zooName = nextStatement.getObject().asLiteral().toString();
                result.put("zooName", zooName);
            } else if (predicate.contains("website")) {
                String website = nextStatement.getObject().asResource().getURI();
                result.put("website", website);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("partOf")) {
                String partOfUri = nextStatement.getObject().asResource().getURI();
                result.put("partOfUri", partOfUri);
                String partOf = nextStatement.getObject().asResource().getLocalName();
                result.put("partOf", partOf);
            } else if (predicate.contains("governingBody")) {
                String governingBodyUri = nextStatement.getObject().asResource().getURI();
                result.put("governingBodyUri", governingBodyUri);
                String governingBody = nextStatement.getObject().asResource().getLocalName();
                result.put("governingBody", governingBody);
            } else if (predicate.contains("governmentType")) {
                String governmentTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("governmentTypeUri", governmentTypeUri);
                String governmentType = nextStatement.getObject().asResource().getLocalName();
                result.put("governmentType", governmentType);
            } else if (predicate.contains("subdivisionType")) {
                String subdivisionTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("subdivisionTypeUri", subdivisionTypeUri);
                String subdivisionType = nextStatement.getObject().asResource().getLocalName();
                result.put("subdivisionType", subdivisionType);
            } else if (predicate.contains("areaTotal")) {
            //    Double areaTotal = nextStatement.getObject().asLiteral().getDouble();
                String areaTotal = nextStatement.getObject().asLiteral().toString();
                result.put("areaTotal", areaTotal);
            } else if (predicate.contains("numberOfVisitors")) {
            //    Integer numberOfVisitors = nextStatement.getObject().asLiteral().getInt();
                String numberOfVisitors = nextStatement.getObject().asLiteral().toString();
                result.put("numberVisitors", numberOfVisitors);
            } else if (predicate.contains("region")) {
                String region = nextStatement.getObject().asLiteral().toString();
                result.put("region", region);
            } else if (predicate.contains("relief")) {
            //    Integer relief = nextStatement.getObject().asLiteral().getInt();
                String relief = nextStatement.getObject().asLiteral().toString();
                result.put("relief", relief);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("leaderTitle")) {
                String leaderTitle = nextStatement.getObject().asLiteral().toString();
                result.put("leaderTitle", leaderTitle);
            } else if (predicate.contains("municipality")) {
                String municipality = nextStatement.getObject().asLiteral().toString();
                result.put("municipality", municipality);
            } else if (predicate.contains("province")) {
                String province = nextStatement.getObject().asLiteral().toString();
                result.put("province", province);
            } else if (predicate.contains("censuscode")) {
            //    Integer censuscode = nextStatement.getObject().asLiteral().getInt();
                String censusCode = nextStatement.getObject().asLiteral().toString();
                result.put("censusCode", censusCode);
            } else if (predicate.contains("averageDepth")) {
            //    Double averageDepth = nextStatement.getObject().asLiteral().getDouble();
                String averageDepth = nextStatement.getObject().asLiteral().toString();
                result.put("averageDepth", averageDepth);
            } else if (predicate.contains("elevation")) {
            //    Double elevation = nextStatement.getObject().asLiteral().getDouble();
                String elevation = nextStatement.getObject().asLiteral().toString();
                result.put("elevation", elevation);
            } else if (predicate.contains("nearestCity")) {
                String nearestCityUri = nextStatement.getObject().asResource().getURI();
                result.put("nearestCityUri", nearestCityUri);
                String nearestCity = nextStatement.getObject().asResource().getLocalName();
                result.put("nearestCity", nearestCity);
            } else if (predicate.contains("established")) {
            //    Integer established = nextStatement.getObject().asLiteral().getInt();
                String established = nextStatement.getObject().asLiteral().toString();
                result.put("established", established);
            } else if (predicate.contains("birthPlace")) {
                String birthPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("birthPlaceUri", birthPlaceUri);
                String birthPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("birthPlace", birthPlace);
            } else if (predicate.contains("deathPlace")) {
                String deathPaceUri = nextStatement.getObject().asResource().getURI();
                result.put("deathPlaceUri", deathPaceUri);
                String deathPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("deathPlace", deathPlace);
            } else if (predicate.contains("deathCause")) {
                String deathCauseUri = nextStatement.getObject().asResource().getURI();
                result.put("deathCauseUri", deathCauseUri);
                String deathCause = nextStatement.getObject().asResource().getLocalName();
                result.put("deathCause", deathCause);
            } else if (predicate.contains("archipelago")) {
                String archipelagoUri = nextStatement.getObject().asResource().getURI();
                result.put("archipelagoUri", archipelagoUri);
                String archipelago = nextStatement.getObject().asResource().getLocalName();
                result.put("archipelago", archipelago);
            } else if (predicate.contains("ethnicGroup")) {
                String ethnicGroupUri = nextStatement.getObject().asResource().getURI();
                result.put("ethnicGroupUri", ethnicGroupUri);
                String ethnicGroup = nextStatement.getObject().asResource().getLocalName();
                result.put("ethnicGroup", ethnicGroup);
            } else if (predicate.contains("length")) {
            //    Double length = nextStatement.getObject().asLiteral().getDouble();
                String length = nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("numberOfIslands")) {
            //    Integer numberOfIslands = nextStatement.getObject().asLiteral().getInt();
                String numberOfIslands = nextStatement.getObject().asLiteral().toString();
                result.put("numberOfIslands", numberOfIslands);
            } else if (predicate.contains("populationTotal")) {
            //    Integer populationTotal = nextStatement.getObject().asLiteral().getInt();
                String populationTotal = nextStatement.getObject().asLiteral().toString();
                result.put("populationTotal", populationTotal);
            } else if (predicate.contains("additionalInfo")) {
                String additionalInfo = nextStatement.getObject().asLiteral().toString();
                result.put("additionalInfo", additionalInfo);
            } else if (predicate.contains("coastlineKm")) {
            //    Double coastlineKm = nextStatement.getObject().asLiteral().getDouble();
                String coastlineKm = nextStatement.getObject().asLiteral().toString();
                result.put("coastlineKm", coastlineKm);
            } else if (predicate.contains("countryAdminDevisionsTitle")) {
                String countryAdminDevisionsTitleUri = nextStatement.getObject().asResource().getURI();
                result.put("countryAdminDevisionsTitleUri", countryAdminDevisionsTitleUri);
                String countryAdminDevisionsTitle = nextStatement.getObject().asResource().getLocalName();
                result.put("countryAdminDevisionsTitle", countryAdminDevisionsTitle);
            } else if (predicate.contains("countryAdminDevisions")) {
                String countryAdminDevisionsUri = nextStatement.getObject().asResource().getURI();
                result.put("countryAdminDevisionsUri", countryAdminDevisionsUri);
                String countryAdminDevisions = nextStatement.getObject().asResource().getLocalName();
                result.put("countryAdminDevisions", countryAdminDevisions);
            } else if (predicate.contains("countryHeading")) {
                String countryHeading = nextStatement.getObject().asLiteral().toString();
                result.put("countryHeading", countryHeading);
            } else if (predicate.contains("demonym")) {
                String demonymUri = nextStatement.getObject().asResource().getURI();
                result.put("demonymUri", demonymUri);
                String demonym = nextStatement.getObject().asResource().getLocalName();
                result.put("demonym", demonym);
            } else if (predicate.contains("densityKm")) {
            //    Double densityKm = nextStatement.getObject().asLiteral().getDouble();
                String densityKm = nextStatement.getObject().asLiteral().toString();
                result.put("densityKm", densityKm);
            } else if (predicate.contains("largestCity")) {
                String largestCity = nextStatement.getObject().asLiteral().toString();
                result.put("largestCity", largestCity);
            } else if (predicate.contains("majorIslands")) {
                String majorIslands = nextStatement.getObject().asLiteral().toString();
                result.put("majorIslands", majorIslands);
            } else if (predicate.contains("populationAsOf")) {
            //    Integer populationAsOf = nextStatement.getObject().asLiteral().getInt();
                String populationAsOf = nextStatement.getObject().asLiteral().toString();
                result.put("populationAsOf", populationAsOf);
            } else if (predicate.contains("waterbody")) {
                String waterBodyUri = nextStatement.getObject().asResource().getURI();
                result.put("waterBodyUri", waterBodyUri);
                String waterBody = nextStatement.getObject().asLiteral().toString();
                result.put("waterBody", waterBody);
            } else if (predicate.contains("widthKm")) {
            //    Integer widthKm = nextStatement.getObject().asLiteral().getInt();
                String widthKm = nextStatement.getObject().asLiteral().toString();
                result.put("widthKm", widthKm);
            } else if (predicate.contains("parent")) {
                String parent = nextStatement.getObject().asLiteral().toString();
                result.put("parent", parent);
            } else if (predicate.contains("river")) {
                String river = nextStatement.getObject().asLiteral().toString();
                result.put("river", river);
            } else if (predicate.contains("widthOrientation")) {
                String widthOrientation = nextStatement.getObject().asLiteral().toString();
                result.put("widthOrientation", widthOrientation);
            } else if (predicate.contains("lengthOrientation")) {
                String lengthOrientation = nextStatement.getObject().asLiteral().toString();
                result.put("lengthOrientation", lengthOrientation);
            } else if (predicate.contains("width")) {
            //    Integer width = nextStatement.getObject().asLiteral().getInt();
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("creator")) {
                String creator = nextStatement.getObject().asLiteral().toString();
                result.put("creator", creator);
            } else if (predicate.contains("dateBuild")) {
                String dateBuilt = nextStatement.getObject().asLiteral().toString();
                result.put("dateBuilt", dateBuilt);
            } else if (predicate.contains("numberOfTemples")) {
            //    Integer numberOfTemples = nextStatement.getObject().asLiteral().getInt();
                String numberOfTemples = nextStatement.getObject().asLiteral().toString();
                result.put("numberOfTemples", numberOfTemples);
            } else if (predicate.contains("primaryDeity")) {
                String primaryDeityUri = nextStatement.getObject().asResource().getURI();
                result.put("primaryDeityUri", primaryDeityUri);
                String primaryDeity = nextStatement.getObject().asResource().getLocalName();
                result.put("primaryDeity", primaryDeity);
            } else if (predicate.contains("properName")) {
                String properName = nextStatement.getObject().asLiteral().toString();
                result.put("properName", properName);
            } else if (predicate.contains("state")) {
                String stateUri = nextStatement.getObject().asResource().getURI();
                result.put("stateUri", stateUri);
                String state = nextStatement.getObject().asResource().getLocalName();
                result.put("state", state);
            } else if (predicate.contains("mouthMountain")) {
                String mouthMountainUri = nextStatement.getObject().asResource().getURI();
                result.put("mouthMountainUri", mouthMountainUri);
                String mouthMountain = nextStatement.getObject().asResource().getLocalName();
                result.put("mouthMountain", mouthMountain);
            } else if (predicate.contains("mouthPlace")) {
                String mouthPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("mouthPlaceUri", mouthPlaceUri);
                String mouthPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("mouthPlace", mouthPlace);
            } else if (predicate.contains("population")) {
                String populationUri = nextStatement.getObject().asResource().getURI();
                result.put("populationUri", populationUri);
                String population = nextStatement.getObject().asResource().getLocalName();
                result.put("population", population);
            } else if (predicate.contains("areaCodeType")) {
                String areaCodeTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("areaCodeTypeUri", areaCodeTypeUri);
                String areaCodeType = nextStatement.getObject().asResource().getLocalName();
                result.put("areaCodeType", areaCodeType);
            } else if (predicate.contains("areaCode")) {
                String areaCode = nextStatement.getObject().asLiteral().toString();
                result.put("areaCode", areaCode);
            } else if (predicate.contains("postalCodeType")) {
                String postalCodeTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("postalCodeTypeUri", postalCodeTypeUri);
                String postalCodeType = nextStatement.getObject().asResource().getLocalName();
                result.put("postalCodeType", postalCodeType);
            } else if (predicate.contains("postalCode")) {
                String postalCode = nextStatement.getObject().asLiteral().toString();
                result.put("postalCode", postalCode);
            } else if (predicate.contains("area")) {
            //    Integer area = nextStatement.getObject().asLiteral().getInt();
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("title")) {
                String titleUri = nextStatement.getObject().asResource().getURI();
                result.put("titleUri", titleUri);
                String title = nextStatement.getObject().asResource().getLocalName();
                result.put("title", title);
            } else if (predicate.contains("cultures")) {
                String cultures = nextStatement.getObject().asLiteral().toString();
                result.put("cultures", cultures);
            } else if (predicate.contains("epochs")) {
                String epochs = nextStatement.getObject().asLiteral().toString();
                result.put("epochs", epochs);
            } else if (predicate.contains("publicAccess")) {
                String publicAccess = nextStatement.getObject().asLiteral().toString();
                result.put("publicAccess", publicAccess);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllProtectedAreas() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?protectedArea WHERE { ?protectedArea dct:subject dbc:Protected_areas . }";
        return findAll(queryString);
    }

    public List<Map<String, String>> findAllProtectedAreasByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?protectedArea WHERE { ?protectedArea dct:subject dbc:Protected_areas ; dbo:country \"" + country + "\" . }";
        return findAll(queryString);
    }

    public Map<String, String> findProtectedAreaByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("nonFictionSubject")) {
                String nonFictionSubjectUri = nextStatement.getObject().asResource().getURI();
                result.put("nonFictionSubjectUri", nonFictionSubjectUri);
                String nonFictionSubject = nextStatement.getObject().asResource().getLocalName();
                result.put("nonFictionSubject", nonFictionSubject);
            } else if (predicate.contains("focus")) {
                String focusUri = nextStatement.getObject().asResource().getURI();
                result.put("focusUri", focusUri);
                String focus = nextStatement.getObject().asResource().getLocalName();
                result.put("focus", focus);
            } else if (predicate.contains("method")) {
                String methodUri = nextStatement.getObject().asResource().getURI();
                result.put("methodUri", methodUri);
                String method = nextStatement.getObject().asResource().getLocalName();
                result.put("method", method);
            } else if (predicate.contains("government")) {
                String governmentTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("governmentTypeUri", governmentTypeUri);
                String governmentType = nextStatement.getObject().asResource().getLocalName();
                result.put("governmentType", governmentType);
            } else if (predicate.contains("subdivisionType")) {
                String subdivisionTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("subdivisionTypeUri", subdivisionTypeUri);
                String subdivisionType = nextStatement.getObject().asResource().getLocalName();
                result.put("subdivisionType", subdivisionType);
            } else if (predicate.contains("categoryLocal")) {
                String categoryLocalUri = nextStatement.getObject().asResource().getURI();
                result.put("categoryLocalUri", categoryLocalUri);
                String categoryLocal = nextStatement.getObject().asResource().getLocalName();
                result.put("categoryLocal", categoryLocal);
            } else if (predicate.contains("designation")) {
                String designationUri = nextStatement.getObject().asResource().getURI();
                result.put("designationUri", designationUri);
                String designation = nextStatement.getObject().asResource().getLocalName();
                result.put("designation", designation);
            } else if (predicate.contains("free")) {
                String freeUri = nextStatement.getObject().asResource().getURI();
                result.put("freeUri", freeUri);
                String free = nextStatement.getObject().asResource().getLocalName();
                result.put("free", free);
            } else if (predicate.contains("foundingDate")) {
            //    Date foundingDate = nextStatement.getObject().asLiteral().getDate();
                String foundingDate = nextStatement.getObject().asLiteral().toString();
                result.put("foundingDate", foundingDate);
            } else if (predicate.contains("dateSigned")) {
            //    Date dateSigned = nextStatement.getObject().asLiteral().getDate();
                String dateSigned = nextStatement.getObject().asLiteral().toString();
                result.put("dateSigned", dateSigned);
            } else if (predicate.contains("dateEffective")) {
            //    Date dateEffective = nextStatement.getObject().asLiteral().getDate();
                String dateEffective = nextStatement.getObject().asLiteral().toString();
                result.put("dateEffective", dateEffective);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("reason")) {
                String reason = nextStatement.getObject().asLiteral().toString();
                result.put("reason", reason);
            } else if (predicate.contains("service")) {
                String serviceUri = nextStatement.getObject().asResource().getURI();
                result.put("serviceUri", serviceUri);
                String service = nextStatement.getObject().asResource().getLocalName();
                result.put("service", service);
            } else if (predicate.contains("subjects")) {
                String subjectsUri = nextStatement.getObject().asResource().getURI();
                result.put("subjectsUri", subjectsUri);
                String subjects = nextStatement.getObject().asResource().getLocalName();
                result.put("subjects", subjects);
            } else if (predicate.contains("speciesArea")) {
                String speciesArea = nextStatement.getObject().asLiteral().toString();
                result.put("speciesArea", speciesArea);
            } else if (predicate.contains("voy")) {
                String voy = nextStatement.getObject().asLiteral().toString();
                result.put("voy", voy);
            } else if (predicate.contains("keywords")) {
                String keywordsUri = nextStatement.getObject().asResource().getURI();
                result.put("keywordsUri", keywordsUri);
                String keywords = nextStatement.getObject().asResource().getLocalName();
                result.put("keywords", keywords);
            } else if (predicate.contains("listing")) {
                String listingUri = nextStatement.getObject().asResource().getURI();
                result.put("listingUri", listingUri);
                String listing = nextStatement.getObject().asResource().getLocalName();
                result.put("listing", listing);
            } else if (predicate.contains("worldHeritageSite")) {
                String worldHeritageSiteUri = nextStatement.getObject().asResource().getURI();
                result.put("worldHeritageSiteUri", worldHeritageSiteUri);
                String worldHeritageSite = nextStatement.getObject().asResource().getLocalName();
                result.put("worldHeritageSite", worldHeritageSite);
            } else if (predicate.contains("established")) {
            //    Integer established = nextStatement.getObject().asLiteral().getInt();
                String established = nextStatement.getObject().asLiteral().toString();
                result.put("established", established);
            } else if (predicate.contains("governingBody")) {
                String governingBodyUri = nextStatement.getObject().asResource().getURI();
                result.put("governingBodyUri", governingBodyUri);
                String governingBody = nextStatement.getObject().asResource().getLocalName();
                result.put("governingBody", governingBody);
            } else if (predicate.contains("nearestCity")) {
                String nearestCity = nextStatement.getObject().asLiteral().toString();
                result.put("nearestCity", nearestCity);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            } else if (predicate.contains("relief")) {
            //    Integer relief = nextStatement.getObject().asLiteral().getInt();
                String relief = nextStatement.getObject().asLiteral().toString();
                result.put("relief", relief);
            } else if (predicate.contains("foundingPlace")) {
                String foundingPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("foundingPlaceUri", foundingPlaceUri);
                String foundingPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("foundingPlace", foundingPlace);
            } else if (predicate.contains("industry")) {
                String industryUri = nextStatement.getObject().asResource().getURI();
                result.put("industryUri", industryUri);
                String industry = nextStatement.getObject().asResource().getLocalName();
                result.put("industry", industry);
            } else if (predicate.contains("companySlogan")) {
                String companySlogan = nextStatement.getObject().asLiteral().toString();
                result.put("companySlogan", companySlogan);
            } else if (predicate.contains("goals")) {
                String goalsUri = nextStatement.getObject().asResource().getURI();
                result.put("goalsUri", goalsUri);
                String goals = nextStatement.getObject().asResource().getLocalName();
                result.put("goals", goals);
            } else if (predicate.contains("functionalStatus")) {
                String functionalStatusUri = nextStatement.getObject().asResource().getURI();
                result.put("functionalStatusUri", functionalStatusUri);
                String functionalStatus = nextStatement.getObject().asResource().getLocalName();
                result.put("functionalStatus", functionalStatus);
            } else if (predicate.contains("status")) {
                String statusUri = nextStatement.getObject().asResource().getURI();
                result.put("statusUri", statusUri);
                String status = nextStatement.getObject().asResource().getLocalName();
                result.put("status", status);
            } else if (predicate.contains("management")) {
                String managementUri = nextStatement.getObject().asResource().getURI();
                result.put("managementUri", managementUri);
                String management = nextStatement.getObject().asResource().getLocalName();
                result.put("management", management);
            } else if (predicate.contains("sights")) {
                String sights = nextStatement.getObject().asLiteral().toString();
                result.put("sights", sights);
            } else if (predicate.contains("parent")) {
                String parent = nextStatement.getObject().asLiteral().toString();
                result.put("parent", parent);
            } else if (predicate.contains("heritageDesignation")) {
                String heritageDesignationUri = nextStatement.getObject().asResource().getURI();
                result.put("heritageDesignationUri", heritageDesignationUri);
                String heritageDesignation = nextStatement.getObject().asResource().getLocalName();
                result.put("heritageDesignation", heritageDesignation);
            } else if (predicate.contains("criteria")) {
                String criteriaUri = nextStatement.getObject().asResource().getURI();
                result.put("criteriaUri", criteriaUri);
                String criteria = nextStatement.getObject().asResource().getLocalName();
                result.put("criteria", criteria);
            } else if (predicate.contains("comments")) {
                String commentsUri = nextStatement.getObject().asResource().getURI();
                result.put("commentsUri", commentsUri);
                String comments = nextStatement.getObject().asResource().getLocalName();
                result.put("comments", comments);
            } else if (predicate.contains("region")) {
                String regionUri = nextStatement.getObject().asResource().getURI();
                result.put("regionUri", regionUri);
                String region = nextStatement.getObject().asResource().getLocalName();
                result.put("region", region);
            } else if (predicate.contains("campus")) {
                String campusUri = nextStatement.getObject().asResource().getURI();
                result.put("campusUri", campusUri);
                String campus = nextStatement.getObject().asResource().getLocalName();
                result.put("campus", campus);
            } else if (predicate.contains("ratifiers")) {
            //    Integer ratifiers = nextStatement.getObject().asLiteral().getInt();
                String ratifiers = nextStatement.getObject().asLiteral().toString();
                result.put("ratifiers", ratifiers);
            } else if (predicate.contains("locationSigned")) {
                String locationSigned = nextStatement.getObject().asLiteral().toString();
                result.put("locationSigned", locationSigned);
            } else if (predicate.contains("languages")) {
                String languages = nextStatement.getObject().asLiteral().toString();
                result.put("languages", languages);
            } else if (predicate.contains("depositor")) {
                String depositor = nextStatement.getObject().asLiteral().toString();
                result.put("depositor", depositor);
            } else if (predicate.contains("conditionEffective")) {
            //    Integer conditionEffective = nextStatement.getObject().asLiteral().getInt();
                String conditionEffective = nextStatement.getObject().asLiteral().toString();
                result.put("conditionEffective", conditionEffective);
            } else if (predicate.contains("additionalInfo")) {
                String additionalInfoUri = nextStatement.getObject().asResource().getURI();
                result.put("additionalInfoUri", additionalInfoUri);
                String additionalInfo = nextStatement.getObject().asResource().getLocalName();
                result.put("additionalInfo", additionalInfo);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllVisitorCentres() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?visitorCenter WHERE { ?visitorCenter dct:subject dbc:Visitor_centers . }";
        return findAll(queryString);
    }

 /*   public void findAllShowCavesByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findVisitorCenterByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("curator")) {
                String curatorUri = nextStatement.getObject().asResource().getURI();
                result.put("curatorUri", curatorUri);
                String curator = nextStatement.getObject().asResource().getLocalName();
                result.put("curator", curator);
            } else if (predicate.contains("foundingDate")) {
            //    Date foundingDate = nextStatement.getObject().asLiteral().getDate();
                String foundingDate = nextStatement.getObject().asLiteral().toString();
                result.put("foundingDate", foundingDate);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("owner")) {
                String ownerUri = nextStatement.getObject().asResource().getURI();
                result.put("ownerUri", ownerUri);
                String owner = nextStatement.getObject().asResource().getLocalName();
                result.put("owner", owner);
            } else if (predicate.contains("headquarter")) {
                String headquarterUri = nextStatement.getObject().asResource().getURI();
                result.put("headquarterUri", headquarterUri);
                String headquarter = nextStatement.getObject().asResource().getLocalName();
                result.put("headquarter", headquarter);
            } else if (predicate.contains("language")) {
                String languageUri = nextStatement.getObject().asResource().getURI();
                result.put("languageUri", languageUri);
                String language = nextStatement.getObject().asResource().getLocalName();
                result.put("language", language);
            } else if (predicate.contains("affiliations")) {
                String affiliations = nextStatement.getObject().asLiteral().toString();
                result.put("affiliations", affiliations);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("regionServed")) {
                String regionServed = nextStatement.getObject().asLiteral().toString();
                result.put("regionServed", regionServed);
            } else if (predicate.contains("builder")) {
                String builderUri = nextStatement.getObject().asResource().getURI();
                result.put("builderUri", builderUri);
                String builder = nextStatement.getObject().asResource().getLocalName();
                result.put("builder", builder);
            } else if (predicate.contains("buildingDate")) {
                String buildingEndDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingDate", buildingEndDate);
            } else if (predicate.contains("condition")) {
                String condition = nextStatement.getObject().asLiteral().toString();
                result.put("condition", condition);
            } else if (predicate.contains("controlledBy")) {
                String controlledBy = nextStatement.getObject().asLiteral().toString();
                result.put("controlledBy", controlledBy);
            } else if (predicate.contains("materials")) {
                String materialsUri = nextStatement.getObject().asResource().getURI();
                result.put("materialsUri", materialsUri);
                String materials = nextStatement.getObject().asResource().getLocalName();
                result.put("materials", materials);
            } else if (predicate.contains("openToPublic")) {
                String openToPublic = nextStatement.getObject().asLiteral().toString();
                result.put("openToPublic", openToPublic);
            } else if (predicate.contains("partof")) {
                String partof = nextStatement.getObject().asLiteral().toString();
                result.put("partof", partof);
            } else if (predicate.contains("width")) {
            //    Integer width = nextStatement.getObject().asLiteral().getInt();
                String width = nextStatement.getObject().asLiteral().toString();
                result.put("width", width);
            } else if (predicate.contains("openingDate")) {
            //    Date openingDate = nextStatement.getObject().asLiteral().getDate();
                String openingDate = nextStatement.getObject().asLiteral().toString();
                result.put("openingDate", openingDate);
            } else if (predicate.contains("buildingType")) {
                String buildingType = nextStatement.getObject().asLiteral().toString();
                result.put("buildingType", buildingType);
            } else if (predicate.contains("leaderFunction")) {
                String leaderFunctionUri = nextStatement.getObject().asResource().getURI();
                result.put("leaderFunctionUri", leaderFunctionUri);
                String leaderFunction = nextStatement.getObject().asResource().getLocalName();
                result.put("leaderFunction", leaderFunction);
            } else if (predicate.contains("membership")) {
                String membership = nextStatement.getObject().asLiteral().toString();
                result.put("membership", membership);
            } else if (predicate.contains("address")) {
                String address = nextStatement.getObject().asLiteral().toString();
                result.put("address", address);
            } else if (predicate.contains("architecturalStyle")) {
                String architecturalStyle = nextStatement.getObject().asLiteral().toString();
                result.put("architecturalStyle", architecturalStyle);
            } else if (predicate.contains("architect")) {
                String architectUri = nextStatement.getObject().asResource().getURI();
                result.put("architectUri", architectUri);
                String architect = nextStatement.getObject().asResource().getLocalName();
                result.put("architect", architect);
            } else if (predicate.contains("buildingStartDate")) {
                String buildingStartDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingStartDate", buildingStartDate);
            } else if (predicate.contains("tenant")) {
                String tenantUri = nextStatement.getObject().asResource().getURI();
                result.put("tenantUri", tenantUri);
                String tenant = nextStatement.getObject().asResource().getLocalName();
                result.put("tenant", tenant);
            } else if (predicate.contains("floorCount")) {
                String floorCount = nextStatement.getObject().asLiteral().toString();
                result.put("floorCount", floorCount);
            } else if (predicate.contains("locationCountry")) {
                String locationCountry = nextStatement.getObject().asLiteral().toString();
                result.put("locationCountry", locationCountry);
            } else if (predicate.contains("locationTown")) {
                String locationTown = nextStatement.getObject().asLiteral().toString();
                result.put("locationTown", locationTown);
            } else if (predicate.contains("mainContractor")) {
                String mainContractorUri = nextStatement.getObject().asResource().getURI();
                result.put("mainContractorUri", mainContractorUri);
                String mainContractor = nextStatement.getObject().asResource().getLocalName();
                result.put("mainContractor", mainContractor);
            } else if (predicate.contains("renovationDate")) {
            //    Integer renovationDate = nextStatement.getObject().asLiteral().getInt();
                String renovationDate = nextStatement.getObject().asLiteral().toString();
                result.put("renovationDate", renovationDate);
            } else if (predicate.contains("architecture")) {
                String architectureUri = nextStatement.getObject().asResource().getURI();
                result.put("architectureUri", architectureUri);
                String architecture = nextStatement.getObject().asResource().getLocalName();
                result.put("architecture", architecture);
            } else if (predicate.contains("beginningDate")) {
                String beginningDate = nextStatement.getObject().asLiteral().toString();
                result.put("beginningDate", beginningDate);
            } else if (predicate.contains("beginningLabel")) {
                String beginningLabel = nextStatement.getObject().asLiteral().toString();
                result.put("beginningLabel", beginningLabel);
            } else if (predicate.contains("built")) {
            //    Integer built = nextStatement.getObject().asLiteral().getInt();
                String built = nextStatement.getObject().asLiteral().toString();
                result.put("built", built);
            } else if (predicate.contains("designation")) {
                String designation = nextStatement.getObject().asLiteral().toString();
                result.put("designation", designation);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllTownSquares() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?townSquare WHERE { ?townSquare dct:subject dbc:Town_squares . }";
        return findAll(queryString);
    }

    /* public void findAllTownSquaresByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findTownSquareByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("architecturalStyles")) {
                String architecturalStylesUri = nextStatement.getObject().asResource().getURI();
                result.put("architecturalStylesUri", architecturalStylesUri);
                String architecturalStyles = nextStatement.getObject().asResource().getLocalName();
                result.put("architecturalStyles", architecturalStyles);
            } else if (predicate.contains("architecture")) {
                String architecture = nextStatement.getObject().asLiteral().toString();
                result.put("architecture", architecture);
            } else if (predicate.contains("architect")) {
                String architect = nextStatement.getObject().asLiteral().toString();
                result.put("architect", architect);
            } else if (predicate.contains("operator")) {
                String operator = nextStatement.getObject().asLiteral().toString();
                result.put("operator", operator);
            } else if (predicate.contains("placeType")) {
                String placeTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("placeTypeUri", placeTypeUri);
                String placeType = nextStatement.getObject().asResource().getLocalName();
                result.put("placeType", placeType);
            } else if (predicate.contains("photoCaption")) {
                String photoCaption = nextStatement.getObject().asLiteral().toString();
                result.put("photoCaption", photoCaption);
            } else if (predicate.contains("region")) {
                String region = nextStatement.getObject().asLiteral().toString();
                result.put("region", region);
            } else if (predicate.contains("status")) {
                String status = nextStatement.getObject().asLiteral().toString();
                result.put("status", status);
            } else if (predicate.contains("namedFor")) {
                String namedForUri = nextStatement.getObject().asResource().getURI();
                result.put("namedForUri", namedForUri);
                String namedFor = nextStatement.getObject().asResource().getLocalName();
                result.put("namedFor", namedFor);
            } else if (predicate.contains("added")) {
            //    Date added = nextStatement.getObject().asLiteral().getDate();
                String added = nextStatement.getObject().asLiteral().toString();
                result.put("added", added);
            } else if (predicate.contains("area")) {
            //    Double area = nextStatement.getObject().asLiteral().getDouble();
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("governmentBody")) {
                String governmentBody = nextStatement.getObject().asLiteral().toString();
                result.put("governmentBody", governmentBody);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllEntertainmentDistricts() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?entertainmentDistrict WHERE { ?entertainmentDistrict dct:subject dbc:Entertainment_districts . }";
        return findAll(queryString);
    }

   /* public void findAllEntertainmentDistrictsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findEntertainmentDistrictByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("areaTotal")) {
            //    Double areaTotal = nextStatement.getObject().asLiteral().getDouble();
                String areaTotal = nextStatement.getObject().asLiteral().toString();
                result.put("areaTotal", areaTotal);
            } else if (predicate.contains("areaCode")) {
                String areaCode = nextStatement.getObject().asLiteral().toString();
                result.put("areaCode", areaCode);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("isPartOf")) {
                String isPartOfUri = nextStatement.getObject().asResource().getURI();
                result.put("isPartOfUri", isPartOfUri);
                String isPartOf = nextStatement.getObject().asResource().getLocalName();
                result.put("isPartOf", isPartOf);
            } else if (predicate.contains("leaderTitle")) {
                String leaderTitle = nextStatement.getObject().asLiteral().toString();
                result.put("leaderTitle", leaderTitle);
            } else if (predicate.contains("postalCode")) {
                String postalCode = nextStatement.getObject().asLiteral().toString();
                result.put("postalCode", postalCode);
            } else if (predicate.contains("synonym")) {
                String synonym = nextStatement.getObject().asLiteral().toString();
                result.put("synonym", synonym);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("subdivision")) {
                String subdivisionTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("subdivisionTypeUri", subdivisionTypeUri);
                String subdivisionType = nextStatement.getObject().asResource().getLocalName();
                result.put("subdivisionType", subdivisionType);
            } else if (predicate.contains("populationTotal")) {
            //    Integer populationTotal = nextStatement.getObject().asLiteral().getInt();
                String populationTotal = nextStatement.getObject().asLiteral().toString();
                result.put("populationTotal", populationTotal);
            } else if (predicate.contains("populationAsOf")) {
            //    Integer populationAsOf = nextStatement.getObject().asLiteral().getInt();
                String populationAsOf = nextStatement.getObject().asLiteral().toString();
                result.put("populationAsOf", populationAsOf);
            } else if (predicate.contains("birthPlace")) {
                String birthPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("birthPlaceUri", birthPlaceUri);
                String birthPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("birthPlace", birthPlace);
            } else if (predicate.contains("ground")) {
                String groundUri = nextStatement.getObject().asResource().getURI();
                result.put("groundUri", groundUri);
                String ground = nextStatement.getObject().asResource().getLocalName();
                result.put("ground", ground);
            } else if (predicate.contains("settlementType")) {
                String settlementType = nextStatement.getObject().asLiteral().toString();
                result.put("settlementType", settlementType);
            } else if (predicate.contains("establishedTitle")) {
                String establishedTitle = nextStatement.getObject().asLiteral().toString();
                result.put("establishedTitle", establishedTitle);
            } else if (predicate.contains("establishedDate")) {
            //    Integer establishedDate = nextStatement.getObject().asLiteral().getInt();
                String establishedDate = nextStatement.getObject().asLiteral().toString();
                result.put("establishedDate", establishedDate);
            } else if (predicate.contains("coordinatesRegion")) {
                String coordinatesRegion = nextStatement.getObject().asLiteral().toString();
                result.put("coordinatesRegion", coordinatesRegion);
            } else if (predicate.contains("city")) {
                String cityUri = nextStatement.getObject().asResource().getURI();
                result.put("cityUri", cityUri);
                String city = nextStatement.getObject().asResource().getLocalName();
                result.put("city", city);
            } else if (predicate.contains("part")) {
                String partUri = nextStatement.getObject().asResource().getURI();
                result.put("partUri", partUri);
                String part = nextStatement.getObject().asResource().getLocalName();
                result.put("part", part);
            } else if (predicate.contains("length")) {
            //    Double length = nextStatement.getObject().asLiteral().getDouble();
                String length = nextStatement.getObject().asLiteral().toString();
                result.put("length", length);
            } else if (predicate.contains("maintainedBy")) {
                String maintainedByUri = nextStatement.getObject().asResource().getURI();
                result.put("maintainedByUri", maintainedByUri);
                String maintainedBy = nextStatement.getObject().asResource().getLocalName();
                result.put("maintainedBy", maintainedBy);
            } else if (predicate.contains("routeStartDirection")) {
                String routeStartDirection = nextStatement.getObject().asLiteral().toString();
                result.put("routeStartDirection", routeStartDirection);
            } else if (predicate.contains("routeEndDirection")) {
                String routeEndDirection = nextStatement.getObject().asLiteral().toString();
                result.put("routeEndDirection", routeEndDirection);
            } else if (predicate.contains("terminusA")) {
                String terminusA = nextStatement.getObject().asLiteral().toString();
                result.put("terminusA", terminusA);
            } else if (predicate.contains("terminusB")) {
                String terminusB = nextStatement.getObject().asLiteral().toString();
                result.put("terminusB", terminusB);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("reason")) {
                String reason = nextStatement.getObject().asLiteral().toString();
                result.put("reason", reason);
            } else if (predicate.contains("routeEnd")) {
                String routeEndUri = nextStatement.getObject().asResource().getURI();
                result.put("routeEndUri", routeEndUri);
                String routeEnd = nextStatement.getObject().asResource().getLocalName();
                result.put("routeEnd", routeEnd);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllWaterfronts() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?waterfront WHERE { ?waterfront dct:subject dbc:Waterfronts . }";
        return findAll(queryString);
    }

   /* public void findAllWaterfrontsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findWaterfrontByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("architect")) {
                String architect = nextStatement.getObject().asLiteral().toString();
                result.put("architect", architect);
            } else if (predicate.contains("cost")) {
            //    Integer cost = nextStatement.getObject().asLiteral().getInt();
                String cost = nextStatement.getObject().asLiteral().toString();
                result.put("cost", cost);
            } else if (predicate.contains("developer")) {
                String developer = nextStatement.getObject().asLiteral().toString();
                result.put("developer", developer);
            } else if (predicate.contains("groundbreaking")) {
            //    Integer groundbreaking = nextStatement.getObject().asLiteral().getInt();
                String groundbreaking = nextStatement.getObject().asLiteral().toString();
                result.put("groundbreaking", groundbreaking);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("opening")) {
                String opening = nextStatement.getObject().asLiteral().toString();
                result.put("opening", opening);
            } else if (predicate.contains("proposed")) {
                String proposed = nextStatement.getObject().asLiteral().toString();
                result.put("proposed", proposed);
            } else if (predicate.contains("status")) {
                String status = nextStatement.getObject().asLiteral().toString();
                result.put("status", status);
            } else if (predicate.contains("use")) {
                String use = nextStatement.getObject().asLiteral().toString();
                result.put("use", use);
            } else if (predicate.contains("routeStart")) {
                String routeStartUri = nextStatement.getObject().asResource().getURI();
                result.put("routeStartUri", routeStartUri);
                String routeStart = nextStatement.getObject().asResource().getLocalName();
                result.put("routeStart", routeStart);
            } else if (predicate.contains("deathPlace")) {
                String deathPlaceUri = nextStatement.getObject().asResource().getURI();
                result.put("deathPlaceUri", deathPlaceUri);
                String deathPlace = nextStatement.getObject().asResource().getLocalName();
                result.put("deathPlace", deathPlace);
            } else if (predicate.contains("significantProject")) {
                String significantProjectUri = nextStatement.getObject().asResource().getURI();
                result.put("significantProjectUri", significantProjectUri);
                String significantProject = nextStatement.getObject().asResource().getLocalName();
                result.put("significantProject", significantProject);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("isPartOf")) {
                String isPartOfUri = nextStatement.getObject().asResource().getURI();
                result.put("isPartOfUri", isPartOfUri);
                String isPartOf = nextStatement.getObject().asResource().getLocalName();
                result.put("isPartOf", isPartOf);
            } else if (predicate.contains("settlementType")) {
                String settlementType = nextStatement.getObject().asLiteral().toString();
                result.put("settlementType", settlementType);
            } else if (predicate.contains("subdivisionType")) {
                String subdivisionType = nextStatement.getObject().asLiteral().toString();
                result.put("subdivisionType", subdivisionType);
            } else if (predicate.contains("unitPref")) {
                String unitPref = nextStatement.getObject().asLiteral().toString();
                result.put("unitPref", unitPref);
            } else if (predicate.contains("campus")) {
                String campusUri = nextStatement.getObject().asResource().getURI();
                result.put("campusUri", campusUri);
                String campus = nextStatement.getObject().asResource().getLocalName();
                result.put("campus", campus);
            } else if (predicate.contains("city")) {
                String cityUri = nextStatement.getObject().asResource().getURI();
                result.put("cityUri", cityUri);
                String city = nextStatement.getObject().asResource().getLocalName();
                result.put("city", city);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("synonym")) {
                String synonym = nextStatement.getObject().asLiteral().toString();
                result.put("synonym", synonym);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("added")) {
            //    Date added = nextStatement.getObject().asLiteral().getDate();
                String added = nextStatement.getObject().asLiteral().toString();
                result.put("added", added);
            } else if (predicate.contains("area")) {
            //    Double area = nextStatement.getObject().asLiteral().getDouble();
                String area = nextStatement.getObject().asLiteral().toString();
                result.put("area", area);
            } else if (predicate.contains("governmentBody")) {
                String governingBodyUri = nextStatement.getObject().asResource().getURI();
                result.put("governingBodyUri", governingBodyUri);
                String governingBody = nextStatement.getObject().asResource().getLocalName();
                result.put("governingBody", governingBody);
            } else if (predicate.contains("yearOfConstruction")) {
            //    Date yearOfConstruction = nextStatement.getObject().asLiteral().getDate();
                String yearOfConstruction = nextStatement.getObject().asLiteral().toString();
                result.put("yearOfConstruction", yearOfConstruction);
            } else if (predicate.contains("architecture")) {
                String architecture = nextStatement.getObject().asLiteral().toString();
                result.put("architecture", architecture);
            } else if (predicate.contains("routeEnd")) {
                String routeEndUri = nextStatement.getObject().asResource().getURI();
                result.put("routeEndUri", routeEndUri);
                String routeEnd = nextStatement.getObject().asResource().getLocalName();
                result.put("routeEnd", routeEnd);
            } else if (predicate.contains("place")) {
                String placeUri = nextStatement.getObject().asResource().getURI();
                result.put("placeUri", placeUri);
                String place = nextStatement.getObject().asResource().getLocalName();
                result.put("place", place);
            } else if (predicate.contains("title")) {
                String title = nextStatement.getObject().asLiteral().toString();
                result.put("title", title);
            } else if (predicate.contains("southeast")) {
                String southeastUri = nextStatement.getObject().asResource().getURI();
                result.put("southeastUri", southeastUri);
                String southeast = nextStatement.getObject().asResource().getLocalName();
                result.put("southeast", southeast);
            } else if (predicate.contains("south")) {
                String southUri = nextStatement.getObject().asResource().getURI();
                result.put("southUri", southUri);
                String south = nextStatement.getObject().asResource().getLocalName();
                result.put("south", south);
            } else if (predicate.contains("about")) {
                String about = nextStatement.getObject().asLiteral().toString();
                result.put("about", about);
            } else if (predicate.contains("height")) {
            //    Integer height = nextStatement.getObject().asLiteral().getInt();
                String height = nextStatement.getObject().asLiteral().toString();
                result.put("height", height);
            } else if (predicate.contains("date")) {
                String date = nextStatement.getObject().asLiteral().toString();
                result.put("date", date);
            } else if (predicate.contains("significantBuilding")) {
                String significantBuildingUri = nextStatement.getObject().asResource().getURI();
                result.put("significantBodyUri", significantBuildingUri);
                String significantBuilding = nextStatement.getObject().asResource().getLocalName();
                result.put("significantBody", significantBuilding);
            } else if (predicate.contains("district")) {
                String districtUri = nextStatement.getObject().asResource().getURI();
                result.put("districtUri", districtUri);
                String district = nextStatement.getObject().asResource().getLocalName();
                result.put("district", district);
            } else if (predicate.contains("locale")) {
                String localeUri = nextStatement.getObject().asResource().getURI();
                result.put("localeUri", localeUri);
                String locale = nextStatement.getObject().asResource().getLocalName();
                result.put("locale", locale);
            } else if (predicate.contains("otherNames")) {
                String otherNames = nextStatement.getObject().asLiteral().toString();
                result.put("otherNames", otherNames);
            } else if (predicate.contains("state")) {
                String stateUri = nextStatement.getObject().asResource().getURI();
                result.put("stateUri", stateUri);
                String state = nextStatement.getObject().asResource().getLocalName();
                result.put("state", state);
            } else if (predicate.contains("locations")) {
                String locationsUri = nextStatement.getObject().asResource().getURI();
                result.put("locationsUri", locationsUri);
                String locations = nextStatement.getObject().asResource().getLocalName();
                result.put("locations", locations);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllFountains() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?fountain WHERE { ?fountain dct:subject dbc:Fountains . }";
        return findAll(queryString);
    }

    /* public void findAllSpaceRelatedVisitorAttractionsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findFountainByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("height")) {
            //    Integer height = nextStatement.getObject().asLiteral().getInt();
                String height = nextStatement.getObject().asLiteral().toString();
                result.put("height", height);
            } else if (predicate.contains("classification")) {
                String classification = nextStatement.getObject().asLiteral().toString();
                result.put("classification", classification);
            } else if (predicate.contains("manufacturer")) {
                String manufacturerUri = nextStatement.getObject().asResource().getURI();
                result.put("manufacturerUri", manufacturerUri);
                String manufacturer = nextStatement.getObject().asResource().getLocalName();
                result.put("manufacturer", manufacturer);
            } else if (predicate.contains("musicians")) {
                String musicians = nextStatement.getObject().asLiteral().toString();
                result.put("musicians", musicians);
            } else if (predicate.contains("range")) {
                String range = nextStatement.getObject().asLiteral().toString();
                result.put("range", range);
            } else if (predicate.contains("related")) {
                String related = nextStatement.getObject().asLiteral().toString();
                result.put("related", related);
            } else if (predicate.contains("names")) {
                String names = nextStatement.getObject().asLiteral().toString();
                result.put("names", names);
            } else if (predicate.contains("exhibits")) {
                String exhibits = nextStatement.getObject().asLiteral().toString();
                result.put("exhibits", exhibits);
            } else if (predicate.contains("dateOpened")) {
            //    Date dateOpened = nextStatement.getObject().asLiteral().getDate();
                String dateOpened = nextStatement.getObject().asLiteral().toString();
                result.put("dateOpened", dateOpened);
            } else if (predicate.contains("imageCaption")) {
                String imageCaption = nextStatement.getObject().asLiteral().toString();
                result.put("imageCaption", imageCaption);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("numAnimals")) {
            //    Integer numAnimals = nextStatement.getObject().asLiteral().getInt();
                String numAnimals = nextStatement.getObject().asLiteral().toString();
                result.put("numAnimals", numAnimals);
            } else if (predicate.contains("numSpecies")) {
            //    Integer numSpecies = nextStatement.getObject().asLiteral().getInt();
                String numSpecies = nextStatement.getObject().asLiteral().toString();
                result.put("numSpecies", numSpecies);
            } else if (predicate.contains("zooName")) {
                String zooName = nextStatement.getObject().asLiteral().toString();
                result.put("zooName", zooName);
            } else if (predicate.contains("country")) {
                String countryUri = nextStatement.getObject().asResource().getURI();
                result.put("countryUri", countryUri);
                String country = nextStatement.getObject().asResource().getLocalName();
                result.put("country", country);
            } else if (predicate.contains("isPartOf")) {
                String isPartOfUri = nextStatement.getObject().asResource().getURI();
                result.put("isPartOfUri", isPartOfUri);
                String isPartOf = nextStatement.getObject().asResource().getLocalName();
                result.put("isPartOf", isPartOf);
            } else if (predicate.contains("condition")) {
                String condition = nextStatement.getObject().asLiteral().toString();
                result.put("condition", condition);
            } else if (predicate.contains("cultures")) {
                String culturesUri = nextStatement.getObject().asResource().getURI();
                result.put("culturesUri", culturesUri);
                String cultures = nextStatement.getObject().asResource().getLocalName();
                result.put("cultures", cultures);
            } else if (predicate.contains("publicAccess")) {
                String publicAccess = nextStatement.getObject().asLiteral().toString();
                result.put("publicAccess", publicAccess);
            } else if (predicate.contains("subdivisionType")) {
                String subdivisionTypeUri = nextStatement.getObject().asResource().getURI();
                result.put("subdivisionTypeUri", subdivisionTypeUri);
                String subdivisionType = nextStatement.getObject().asResource().getLocalName();
                result.put("subdivisionType", subdivisionType);
            } else if (predicate.contains("address")) {
                String address = nextStatement.getObject().asLiteral().toString();
                result.put("address", address);
            } else if (predicate.contains("status")) {
                String status = nextStatement.getObject().asLiteral().toString();
                result.put("status", status);
            } else if (predicate.contains("buildingStartDate")) {
                String buildingStartDate = nextStatement.getObject().asLiteral().toString();
                result.put("buildingStartDate", buildingStartDate);
            } else if (predicate.contains("renovationDate")) {
                String renovationDate = nextStatement.getObject().asLiteral().toString();
                result.put("renovationDate", renovationDate);
            } else if (predicate.contains("features")) {
                String featuresUri = nextStatement.getObject().asResource().getURI();
                result.put("featuresUri", featuresUri);
                String features = nextStatement.getObject().asResource().getLocalName();
                result.put("features", features);
            } else if (predicate.contains("mythology")) {
                String mythologyUri = nextStatement.getObject().asResource().getURI();
                result.put("mythologyUri", mythologyUri);
                String mythology = nextStatement.getObject().asResource().getLocalName();
                result.put("mythology", mythology);
            } else if (predicate.contains("siteName")) {
                String siteNameUri = nextStatement.getObject().asResource().getURI();
                result.put("siteNameUri", siteNameUri);
                String siteName = nextStatement.getObject().asResource().getLocalName();
                result.put("siteName", siteName);
            } else if (predicate.contains("industry")) {
                String industry = nextStatement.getObject().asLiteral().toString();
                result.put("industry", industry);
            }
        }
        return result;
    }

    public List<Map<String, String>> findAllSpaceRelatedVisitorAttractions() {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "SELECT ?spaceRelatedVisitorAttraction WHERE { ?spaceRelatedVisitorAttraction dct:subject dbc:Space-related_visitor_attractions . }";
        return findAll(queryString);
    }

    /* public void findAllSpaceRelatedVisitorAttractionsByCountry(String country) {
        String queryString = "PREFIX dct: " + terms + " PREFIX dbc: " + category + "PREFIX dbo: " + ontology + " SELECT ?showCave WHERE { ?showCave dct:subject dbc:Show_caves ; dbo:country \"" + country + "\" . }";
        findAll(queryString);
    } */

    public Map<String, String> findSpaceRelatedVisitorAttractionByName(String name) {
        Model model = findByName(name);
        StmtIterator stmtIterator = model.listStatements();
        Map<String, String> result = new HashMap<String, String>();
        while (stmtIterator.hasNext()) {
            Statement nextStatement = stmtIterator.nextStatement();
            String predicate = nextStatement.getPredicate().toString();
            if (predicate.contains("abstract")) {
                String comment = nextStatement.getObject().asLiteral().toString();
                result.put("abstract", comment);
            } else if (predicate.contains("label")) {
                String label = nextStatement.getObject().asLiteral().toString();
                result.put("label", label);
            } else if (predicate.contains("thumbnail")) {
                String thumbnail = nextStatement.getObject().asResource().getURI();
                result.put("thumbnail", thumbnail);
            } else if (predicate.contains("depiction")) {
                String depiction = nextStatement.getObject().asResource().getURI();
                result.put("depiction", depiction);
            } else if (predicate.contains("location")) {
                String locationUri = nextStatement.getObject().asResource().getURI();
                result.put("locationUri", locationUri);
                String location = nextStatement.getObject().asResource().getLocalName();
                result.put("location", location);
            } else if (predicate.contains("foundingDate")) {
            //    Date foundingDate = nextStatement.getObject().asLiteral().getDate();
                String foundingDate = nextStatement.getObject().asLiteral().toString();
                result.put("foundingDate", foundingDate);
            } else if (predicate.contains("numberOfVisitors")) {
            //    Integer numberOfVisitors = nextStatement.getObject().asLiteral().getInt();
                String numberOfVisitors = nextStatement.getObject().asLiteral().toString();
                result.put("numberOfVisitors", numberOfVisitors);
            } else if (predicate.contains("architect")) {
                String architectUri = nextStatement.getObject().asResource().getURI();
                result.put("architectUri", architectUri);
                String architect = nextStatement.getObject().asResource().getLocalName();
                result.put("architect", architect);
            } else if (predicate.contains("collectionSize")) {
            //    Integer collectionSize = nextStatement.getObject().asLiteral().getInt();
                String collectionSize = nextStatement.getObject().asLiteral().toString();
                result.put("collectionSize", collectionSize);
            } else if (predicate.contains("formerName")) {
                String formerName = nextStatement.getObject().asLiteral().toString();
                result.put("formerName", formerName);
            } else if (predicate.contains("caption")) {
                String caption = nextStatement.getObject().asLiteral().toString();
                result.put("caption", caption);
            } else if (predicate.contains("established")) {
            //    Integer established = nextStatement.getObject().asLiteral().getInt();
                String established = nextStatement.getObject().asLiteral().toString();
                result.put("established", established);
            }
        }
        return result;
    }

 /*      public static void main(String [] args) {
           ConsumingLinkedData consumingLinkedData = new ConsumingLinkedData();
           Map<String, String> beaches = consumingLinkedData.findCategoryByName("Beaches");
           System.out.println(beaches.toString()); */
       /*    consumingLinkedData.findAllBeaches();
           System.out.println();
           consumingLinkedData.findAllBeachesByCountry("Australia"); */
/*          String resource1URL = "http://dbpedia.org/resource/Tunnel_of_Love_(railway)";
            Model model1 = ModelFactory.createDefaultModel();
        model1.read(resource1URL);
        StmtIterator stmtIterator = model1.listStatements();
        while(stmtIterator.hasNext())

    {
        Statement nextStatement = stmtIterator.nextStatement();
        //    System.out.println(nextStatement.getPredicate().toString());
        if (nextStatement.getPredicate().toString().equals(ontology + "abstract")) {
            String comment = nextStatement.getObject().asLiteral().toString();
            System.out.println("Abstract: " + comment);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "lineLength")) {
            Double lineLength = nextStatement.getObject().asLiteral().getDouble();
            System.out.println("LineLength: " + lineLength);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "location")) {
            Resource location = nextStatement.getObject().asResource();
            String l = location.toString();
            System.out.println("Location: " + l);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "operator")) {
            Resource operator = nextStatement.getObject().asResource();
            String o = operator.toString();
            System.out.println("operator: " + o);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "routeStart")) {
            Resource routeStart = nextStatement.getObject().asResource();
            String rs = routeStart.toString();
            System.out.println("Route start: " + rs);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "routeEnd")) {
            Resource routeEnd = nextStatement.getObject().asResource();
            String re = routeEnd.toString();
            System.out.println("Route end: " + re);
        }
        if (nextStatement.getPredicate().toString().equals(ontology + "type")) {
            Resource type = nextStatement.getObject().asResource();
            String t = type.toString();
            System.out.println("Type: " + t);
        }
        if (nextStatement.getPredicate().toString().equals("dbp:caption")) {
            String caption = nextStatement.getObject().asLiteral().toString();
            //    String c = caption.toString();
            System.out.println("Caption: " + caption);
        }
        if (nextStatement.getPredicate().toString().equals("dbp:title")) {
            String title = nextStatement.getObject().asLiteral().toString();
            //    String ti = title.toString();
            System.out.println("Title: " + title);
        }
        if (nextStatement.getPredicate().toString().equals("dbp:notrack")) {
            Resource routeEnd = nextStatement.getObject().asResource();
            String nt = routeEnd.toString();
            System.out.println("Notrack: " + nt);
        }
        if (nextStatement.getPredicate().toString().equals("rdfs:label")) {
            String label = nextStatement.getObject().asLiteral().toString();
            //    String l = label.toString();
            System.out.println("Label: " + label);
        }
        if (nextStatement.getPredicate().toString().equals("foaf:name")) {
            String name = nextStatement.getObject().asLiteral().toString();
            //    String n = name.toString();
            System.out.println("Name: " + name);
        }
    }
        model1.write(System.out,"JSON-LD");

    String resource2URL = "http://dbpedia.org/resource/Daymark";
    Model model2 = ModelFactory.createDefaultModel();
        model2.read(resource2URL);
    StmtIterator si = model2.listStatements();
        while(si.hasNext())

    {
        Statement ns = si.nextStatement();
        System.out.println(ns.getPredicate().toString());

        if (ns.getPredicate().toString().equals("http://dbpedia.org/ontology/abstract")) {
            Literal c = ns.getObject().asLiteral();
            if (c.getLanguage().equals("en")) {
                String comment = c.toString();
                System.out.println(comment);
            }
        }

        if (ns.getPredicate().toString().equals("http://xmlns.com/foaf/0.1/depiction")) {
            Resource d = ns.getObject().asResource();
            StmtIterator stmtIterator1 = d.listProperties();
            while (stmtIterator1.hasNext()) {
                Property next = stmtIterator1.nextStatement().getPredicate();
                String s = next.getURI();
                System.out.println(s);
            }
        }
    }

    String resource3URL = "http://dbpedia.org/resource/Colonna_Mediterranea";
    Model model3 = ModelFactory.createDefaultModel();
        model3.read(resource3URL);
    StmtIterator stmtIterator1 = model3.listStatements();
        while(stmtIterator1.hasNext())

    {
        Statement statement = stmtIterator1.nextStatement();
        String predicate = statement.getPredicate().toString();
        System.out.println(predicate);
        System.out.println();

        if (predicate.equals("http://dbpedia.org/ontology/location")) {
            Resource l = statement.getObject().asResource();
            String location = l.getLocalName();
            String url = l.getURI();
            System.out.println(location + " " + url);
        }

        if (predicate.equals("http://dbpedia.org/ontology/buildingEndDate")) {
            Literal d = statement.getObject().asLiteral();
            String date = d.toString();
            System.out.println(date);
        }

        if (predicate.equals("http://dbpedia.org/property/imageCaption")) {
            Literal ic = statement.getObject().asLiteral();
            String imageCaption = ic.toString();
            System.out.println(imageCaption);
        }

        if (predicate.equals("http://xmlns.com/foaf/0.1/depiction")) {
            Resource n = statement.getObject().asResource();
            String url = n.getURI();
            String depiction = n.getLocalName();
            System.out.println(url);
            System.out.println(depiction);
        }

        if (predicate.equals("http://dbpedia.org/ontology/architecturalStyle")) {
            Resource as = statement.getObject().asResource();
            String architecturalStyle = as.getLocalName();
            String url = as.getURI();
            System.out.println(architecturalStyle + " " + url);
        }

        if (predicate.equals("http://dbpedia.org/ontology/owner")) {
            Resource o = statement.getObject().asResource();
            String owner = o.getLocalName();
            String url = o.getURI();
            System.out.println(owner + " " + url);
        }

        if (predicate.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
            Resource t = statement.getObject().asResource();
            String type = t.toString();
            System.out.println(type);
        }

        if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
            Literal c = statement.getObject().asLiteral();
            String comment = c.toString();
            System.out.println(comment);
        } */
//    }
}
