import React from 'react';

const Landmark = (props) => {

//	console.log(props.landmark);

    function cardHeader() {
        return (
            <div className={"card-header"}>
				<div className={"row"}>
					<div className={"col-md-6 text-left"}>
						<div className={"card-title"}>
							<h1 className={"display-6"}>
								{props.landmark.label}
							</h1>
						</div>
					</div>
					<div className={"col-md-6 text-right"}>
						<a href={"#"} className={"btn btn-outline-warning m-1"} title={"Add to visited attractions"}>
							<i className={"fa fa-star"}></i>
						</a>
						<a href={"#"} className={"btn btn-outline-danger m-1"} title={"Add to favourite attractions"}>
							<i className={"fa fa-heart"}></i>
						</a>
						<a href={"#"} className={"btn btn-outline-primary m-1"} title={"Add to wish list"}>
							<i className={"fa fa-globe"}></i>
						</a>
					</div>
                </div>
            </div>
        );
    }

    function cardBody() {
        return (
            <div className={"card-body"}>
                <img className={"card-img img-responsive img-thumbnail"}
                     alt={props.landmark.caption} src={props.landmark.depiction}/>
                     <div className={"card-text"}>
                         {props.landmark.comment}
                     </div>
            </div>
        );
    }

    function cardFooter() {
        return (
            <div className={"card-footer"}>
				<div className={"row"}>
					<div className={"text-left col-md-6"}>
						<a href={"#"} className={"btn btn-primary m-1"}>Add a rating</a>
					</div>
					<div className={"text-right col-md-6"}>
						<a href={"#"} className={"btn btn-secondary m-1"}>Write a comment or a review</a>
					</div>	
				</div>
            </div>
        );
    }

    return (
      <div className={"card col-md-6 offset-md-3"}>
          {cardHeader()}
          {cardBody()}
          {cardFooter()}
      </div>
    );
}

export default Landmark;