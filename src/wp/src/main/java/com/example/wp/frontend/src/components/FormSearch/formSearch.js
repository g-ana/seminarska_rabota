import React from "react";

const FormSearch = (props) => {

    const onSearch = (event) => {
        event.preventDefault();
        props.onSearch(event.target["searchTerm"].value);
    };

    return (
        <form onSubmit={onSearch} className={"form-inline mt-2 mt-md-0"}>
            <input type={"text"} className={"form-control mr-sm-2"}
                   name={"searchTerm"} placeholder={"Search..."} aria-label={"Search"}/>
                   <button type={"submit"} className={"btn btn-success my-2 my-sm-0"}>
                       <div className={"d-inline-block"}>
                            <i className={"fa fa-search"}></i>
                            <span className={"m-1"}>Search</span>
                       </div>
                   </button>
        </form>
    );

}

export default FormSearch;