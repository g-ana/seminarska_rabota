import React from 'react';
import Landmark from '../Landmarks/landmark';

const LandmarkListGrouped = (props) => {

    function getLandmarks() {
        const landmarks = props.landmarks.map((landmark, index) => {
            return (
                <Landmark landmark={landmark} landmarkId={landmark.name} key={index} className={"col-md-6 mt-2 col-sm-12"}/>
            );
        });
    }

    function handlePageClick(event) {
        event.preventDefault();
        props.onPageClick(event.selected);
    }

    function pagination() {
        if (props.totalPages!==0) {
            return (
                <nav aria-label={"Page navigation"} className={"mt-5"}>
                    <ul className={"pagination justify-content-center"}>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>Previous</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>1</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>2</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>3</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>4</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>5</a>
                        </li>
                        <li className={"page-item"}>
                            <a className={"page-link"} href={"#"}>Next</a>
                        </li>
                    </ul>
                </nav>
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

export default LandmarkListGrouped;