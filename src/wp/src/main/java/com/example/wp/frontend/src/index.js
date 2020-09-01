import React from 'react';
import ReactDOM from 'react-dom';
import './all.css';
import './index.css';
// import App from './App';
import 'jquery/dist/jquery';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap';
import 'bootstrap/dist/js/bootstrap.min'
import 'font-awesome/css/font-awesome.css';
// import App from './components/App/App';
import Appc from './components/App/Appc';
// import Header from './components/Header/header';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(
  <React.StrictMode>
    <Appc />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
