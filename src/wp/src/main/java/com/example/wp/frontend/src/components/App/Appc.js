import React, {Component} from 'react';
import logo from '../../logo.svg';
import './App.css';

import Header from '../Header/header';
import Category from '../Categories/category';
import Landmark from '../Landmarks/landmark';

import CategoryList from '../Categories/categoryList';
import LandmarkList from '../Landmarks/landmarkList';

import CategoriesService from '../../repository/axiosCategoriesRepository';
import LandmarksService from '../../repository/axiosLandmarksRepository';
import TouristAttractionsService from '../../repository/axiosTouristAttractionsRepository';

import {Route, Redirect, BrowserRouter as Router} from 'react-router-dom';

class Appc extends Component {
	
	constructor(props) {
		super(props);
		this.state = {
            page: 0,
            pageSize: 2,
            totalPages: 0,
            category: {
                label: "beaches",
                comment: "A beach is a landform along a body of water. " +
                    "It usually consists of loose particles, which are often composed of rock, such as sand, gravel, shingle, pebbles, or cobblestones. " +
                    "The particles comprising a beach are occasionally biological in origin, such as mollusc shells or coralline algae. " +
                    "Some beaches have man-made infrastructure, such as lifeguard posts, changing rooms, and showers. " +
                    "They may also have hospitality venues (such as resorts, camps, hotels, and restaurants) nearby. " +
                    "Wild beaches, also known as undeveloped or undiscovered beaches, are not developed in this manner. " +
                    "Wild beaches can be valued for their untouched beauty and preserved nature. " +
                    "Beaches typically occur in areas along the coast where wave or current action deposits and reworks sediments. ",
                thumbnail: "https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Man_o%27war_cove_near_lulworth_dorset_arp.jpg/300px-Man_o%27war_cove_near_lulworth_dorset_arp.jpg",
                depiction: "https://upload.wikimedia.org/wikipedia/commons/8/86/Man_o%27war_cove_near_lulworth_dorset_arp.jpg",
                caption: "Beach in the Galápagos Islands reserved for marine animals\n" +
                    "Sand and shingle is scoured, graded and moved around by the action of waves and currents\n" +
                    "Quartz sand particles and shell fragments from a beach. The primary component of typical beach sand is quartz, or silica .\n" +
                    "Playing in the surf is a favourite activity for many people",
            },
            landmark: {
                label: "Dail Mòr",
                comment: "Dail Mòr (or Dalmore) is a hamlet situated in the Northside of Carloway, a major settlement on the Isle of Lewis in Scotland. " +
                    "The hamlet has a beach and a cemetery. " +
                    "A small well kept car park is available for visitors as are picnic & public BBQ facilities. " +
                    "The beach is a known surf destination mentioned in numerous guidebooks. Note there is a strong rip current at the north end of the beach. " +
                    "Despite its remoteness, five of the six houses in the village were connected to fibre broadband in November 2012. " +
                    "In August 2016, the Transocean Winner oil rig ran aground, on a headland just off Dalmore beach. " +
                    "The oil rig was being towed from Norway to Malta, when it became detached from the tug boat.",
                thumbnail: "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Picnic_Area_and_Cemetery%2C_Dail_Mor_-_geograph.org.uk_-_568187.jpg/300px-Picnic_Area_and_Cemetery%2C_Dail_Mor_-_geograph.org.uk_-_568187.jpg",
                depiction: "https://upload.wikimedia.org/wikipedia/commons/0/06/Picnic_Area_and_Cemetery%2C_Dail_Mor_-_geograph.org.uk_-_568187.jpg",
                caption: "Shown within Scotland",
            },
            touristAttraction: {},
                categories: [
                "Beaches", "Parks", "Show caves", "Towers", "Castles", "Palaces", "Royal residences",
                "Museums", "Monuments", "Buildings", "Resorts", "Protected areas", "Nature reserves", "Heraldic sites", 
                "Natural arches",  "Town squares", "Waterfronts", "Fountains", "Visitor centers", "Amusement parks", 
                "Archaeological sites", "Historical districts", "Triumphal arches", "Underground cities", 
                "Space-related visitor attractions",
            ],
            landmarks: [
            "Dail Mòr", "Port de Sant Miguel", "Jones Beach (New South Wales)", "Ninety Mile Beach, Victoria", "Kuakata",
            "Praia Grande (Ferragudo)", "Costalegre", "Tannirbhavi Beach", "Kairaki", "Jobos Beach", "Panambur Beach",
            "Flamenco Beach",
            ],
            touristAttractions: [],
        };
    /*    this.loadCategories = this.loadCategories.bind(this);
        this.loadLandmarks = this.loadLandmarks.bind(this);
        this.loadTouristAttractions = this.loadTouristAttractions.bind(this);
        this.loadCategory = this.loadCategory.bind(this);
        this.loadTouristAttraction = this.loadTouristAttraction.bind(this); */
    }

    searchData(category, name) {
        LandmarksService.fetchLandmarkByCategoryAndName(category, name).then((response) => {
            console.log(response.data);
            this.setState({
                landmark: {
                    label: response.data.label,
                    comment: response.data.comment,
                    thumbnail: response.data.thumbnail,
                    depiction: response.data.depiction,
                    caption: response.data.caption,
                }
            })
        }).catch((error) => {
            console.log(error.message);
        });
    };
	
	loadCategories() {
        CategoriesService.fetchCategoriesPaged(this.state.page, this.state.pageSize).then((response) => {
            console.log(response.data);
            this.setState({
                categories: {
                    value: response.data.value,
                },
                totalPages: Math.ceil(1.0 * response.data.value.length / this.state.pageSize),
            });
        }).catch((error) => {
            console.log(error.message);
        });
    };

    loadLandmarks(category) {
        LandmarksService.fetchLandmarksByCategoryPaged(category, this.state.page, this.state.pageSize).then((response) => {
            console.log(response.data);
            this.setState({
                landmarks: {
                    value: response.data.value,
                },
                totalPages: Math.ceil(1.0 * response.data.value.length / this.state.pageSize),
            });
        }).catch((error) => {
            console.log(error.message);
        });
    };
	
	loadTouristAttractions(category) {
        TouristAttractionsService.fetchTouristAttractionsByCategoryPaged(category, this.state.page, this.state.pageSize).then((response) => {
            console.log(response.data);
            this.setState(
                {
                    touristAttractions: response.data.value,
                    totalPages: Math.ceil(1.0 * response.data.value.length / this.state.pageSize),
                });
        }).catch((error) => {
            console.log(error.message);
        });
    };

    loadCategory(name) {
        CategoriesService.fetchCategoryByName(name).then((response) => {
            console.log(response.data);
            this.setState({
                category: {
                    label: response.data.label,
                }
            });
        }).catch((error) => {
            console.log(error.message);
        });
    };

    loadLandmark(category, name) {
        LandmarksService.fetchLandmarkByCategoryAndName(category, name).then((response) => {
            console.log(response.data);
            this.setState({
                landmark: {
                    label: response.data.label,
                    comment: response.data.comment,
                    thumbnail: response.data.thumbnail,
                    depiction: response.data.depiction,
                    caption: response.data.caption,
                }
            });
        }).catch((error) => {
            console.log(error.message);
        });
    }; 
	
	loadTouristAttraction(category, name) {
        TouristAttractionsService.fetchTouristAttractionByCategoryAndName(category, name).then((response) => {
            console.log(response.data);
            this.setState({
                touristAttraction: {
                    label: response.data.label,
                    comment: response.data.comment,
                    thumbnail: response.data.thumbnail,
                    depiction: response.data.depiction,
                    caption: response.data.depiction,
                }
            });
        }).catch((error) => {
            console.log(error.message);
        });
    };

    componentDidMount() {
		console.log(this.state.landmark);
		console.log(this.state.category);
        this.loadCategories();
    }
	
	
	render() {
		
		const routing = (
			<Router>
				<Header onSearch={this.searchData}/>
				<main role={"main"} className={"mt-3"}>
					<div className={"container"}>
						<Route path={"/landmarks"} exact render={() =>
                            <LandmarkList onPageClick={this.loadLandmarks} landmarks={this.state.landmarks}/>
                        }>
                        </Route>
                        <Route path={"/categories"} exact render={() =>
                            <CategoryList onPageClick={this.loadCategories} categories={this.state.categories}/>
                        }>
                        </Route>
                        <Route path={"/categories/:name"} exact render={() =>
                            <Category onPageClick={this.loadCategory} category = {this.state.category}/>
                        }>
                        </Route>
                        <Route path={"/landmarks/:name"} exact render={() =>
                            <Landmark onPageClick = {this.loadLandmark} landmark={this.state.landmark}/>
                        }>
                        </Route>
						<Redirect to={"/categories"}/> 
					</div>
				</main>
			</Router>
		); 
		
		return (
			<div className={"App"}>
				{routing}
			</div>
		);
	}
	
 }

export default Appc; 