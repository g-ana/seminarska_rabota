import React from 'react';
import Landmark from '../Landmarks/landmark';
import ReactPaginate from 'react-paginate';
// import { useHistory } from "react-router-dom";
import {Link} from 'react-router-dom';

const LandmarkList = (props) => {

    const history = (props.history || []);

/*    function handleClick() {
        history.push(`/landmarks/${props.landmark.label}`);
    } */

    function getLandmarks() {
	//	console.log(props.landmarks);
        const landmarks = (props.landmarks || []).map((landmark, index) => {
            return ( /*
                <Landmark landmark={props.landmark} landmarkId={props.landmark.label} key={index} className={"col-md-6 mt-2 col-sm-12"}/>
            <li className={"list-group-item"} key={index}>
                <a href={`/landmarks/${landmark}`}>{landmark.label}</a>
            </li> */
            <div className={"col-2 m-1"}>
                <Link to={`/landmarks/${landmark}`} className={"btn btn-secondary"}>
                    {landmark}
                </Link>
            </div>
            );
        });
        return (
            <div className={"row m-5"}>
                {landmarks}
            </div>
        )
    }

    function handlePageClick(event) {
    //    event.preventDefault();
        props.onPageClick(event.selected);
    }

    function pagination() {
        if (props.totalPages!==0) {
            return (
                <ReactPaginate previousLabel={"previous"}
                               nextLabel={"next"}
                               breakLabel={<span className="gap">...</span>}
                               breakClassName={"break-me"}
                               pageCount={props.totalPages}
                               marginPagesDisplayed={2}
                               pageRangeDisplayed={5}
                               pageClassName={"page-item"}
                               pageLinkClassName={"page-link"}
                               previousClassName={"page-item"}
                               previousLinkClassName={"page-link"}
                               nextClassName={"page-item"}
                               nextLinkClassName={"page-link"}
                               activeClassName={"active"}
                               forcePage={props.page}
                               onPageChange={handlePageClick}
                               containerClassName={"pagination justify-content-center"}
                               />
            );
        }
    }

    return (
        <div>
            <div className={"row"}>
                {getLandmarks()}
            </div>
            <hr/>
            {pagination()}
        </div>
    );
}

export default LandmarkList;