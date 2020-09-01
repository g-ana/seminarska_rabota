import React, {useState, useEffect} from 'react';
import logo from '../../logo.svg';
import './App.css';
import Header from "../Header/header";
import {Route, Redirect, BrowserRouter as Router} from 'react-router-dom';
import LandmarkList from "../Landmarks/landmarkList";
import CategoryList from "../Categories/categoryList";
import LandmarkListGrouped from "../Landmarks/landmarkListGrouped";
import LandmarksService from "../../repository/axiosLandmarksRepository";
import CategoriesService from "../../repository/axiosCategoriesRepository";
import Category from "../Categories/category";
import Landmark from "../Landmarks/landmark";
import TouristAttractionsService from "../../repository/axiosTouristAttractionsRepository";

function App() {

  const [category, setCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [landmark, setLandmark] = useState({});
  const [landmarks, setLandmarks] = useState([]);
  const [touristAttraction, setTouristAttraction] = useState({});
  const [touristAttractions, setTouristAttractions] = useState([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(2);
  const [totalPages, setTotalPages] = useState(0);

  const searchData = (category, name) => {
    LandmarksService.fetchLandmarkByCategoryAndName(category, name).then((response) => {
      console.log(response.data);
      setLandmark({
        label: response.data.label,
        comment: response.data.comment,
        thumbnail: response.data.thumbnail,
        depiction: response.data.depiction,
        caption: response.data.caption,
      })
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadCategories = () => {
    CategoriesService.fetchAllCategories().then((response) => {
      console.log(response.data);
      setCategories(response.data.value);
      setTotalPages(response.data.value.length / pageSize);
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadLandmarks = () => {
    LandmarksService.fetchAllLandmarksByCategory(category).then((response) => {
      console.log(response.data);
      setLandmarks(response.data.value);
      setTotalPages(response.data.value.length / pageSize);
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadTouristAttractions = () => {
    TouristAttractionsService.fetchAllTouristAttractionsByCategory(category).then((response) => {
      console.log(response.data);
      setTouristAttractions(response.data.value);
      setTotalPages(response.data.value.length / pageSize);
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadCategory = (name) => {
    CategoriesService.fetchCategoryByName(name).then((response) => {
      console.log(response.data);
      setCategory({
        label: response.data.label,
      });
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadLandmark = (category, name) => {
    LandmarksService.fetchLandmarkByCategoryAndName(category, name).then((response) => {
      console.log(response.data);
      setLandmark({
        label: response.data.label,
        comment: response.data.comment,
        thumbnail: response.data.thumbnail,
        depiction: response.data.depiction,
        caption: response.data.caption,
      });
    }).catch((error) => {
      console.log(error.message());
    });
  };

  const loadTouristAttraction = (category, name) => {
    TouristAttractionsService.fetchTouristAttractionByCategoryAndName(category, name).then((response) => {
      console.log(response.data);
      setTouristAttraction({
        label: response.data.label,
        comment: response.data.comment,
        thumbnail: response.data.thumbnail,
        depiction: response.data.depiction,
        caption: response.data.depiction,
      });
    }).catch((error) => {
      console.log(error.message());
    });
  };

  useEffect(() => loadCategories, [categories, totalPages]);

  const routing = (
    <Router>
      <Header onSearch={(category, name) => searchData(category, name)}/>
      <main role={"main"} className={"mt-3"}>
        <div className={"container"}>
          <Route path={"/landmarks"} exact render={() =>
            <LandmarkList onPageClick={() => loadLandmarks()} landmarks={landmarks}/>
          }>
          </Route>
          <Route path={"/categories"} exact render={() =>
            <CategoryList onPageClick={() => loadCategories()} categories={categories}/>
          }>
          </Route>
          <Route path={"/categories/:name"} exact render={() =>
            <Category onPageClick={(name) => loadCategory(name)} categories = {category}/>
          }>
          </Route>

          <Route path={"/landmark/:name"} exact render={() =>
            <Landmark onClick = {(name) => loadLandmark(name)} landmark={landmark}/>
          }>
          </Route>
             <Redirect to={"/categories"}/>
        </div>
      </main>
    </Router>
  );

  return ( /*
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
          <Header onSearch = {props.searchData}/>
      </header>

    </div> */


   <div className={"App"}>
    {routing}
  </div>
  );
}

export default App;

