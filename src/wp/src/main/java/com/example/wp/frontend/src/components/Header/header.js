import React from 'react';
import {Link} from 'react-router-dom';
import FormSearch from '../FormSearch/formSearch';

const Header = (props) => {
    return (
        <header>
            <nav className={"navbar navbar-md-expand navbar-light navbar-fixed bg-light"}>
                <Link className={"navbar-brand"} to={"/index"}>DBpedia-based tourist guide</Link>
                <button type={"button"} className={"navbar-toggler"} data-toggle={"collapse"} data-target={"#navbarCollapse"}
                        aria-controls={"navbarColapse"} aria-expanded={"false"} aria-label={"Toggle navigation"}>
                    <span className={"navbar-toggler-icon"}></span>
                </button>
                <div className={"collapse navbar-collapse"} id={"navbarCollapse"}>
                    <ul className={"navbar-nav mr-auto"}>
                        <li className={"nav-item"}>
                            <Link className={"nav-link"} to={"/index"}>Home</Link>
                        </li>
                        <li className={"nav-item"}>
                            <Link className={"nav-link"} to={"/tourist-attractions"}>Tourist attractions</Link>
                        </li>
                        <li className={"nav-item"}>
                            <Link className={"nav-link"} to={"/landmarks"}>Landmarks</Link>
                        </li>
                        <li className={"nav-item active"}>
                            <Link className={"nav-link"} to={"/categories"}>Categories</Link>
                        </li>
                    </ul>
                </div>
                <FormSearch onSearch = {props.onSearch}/>
            </nav>
        </header>
    );

}

export default Header;