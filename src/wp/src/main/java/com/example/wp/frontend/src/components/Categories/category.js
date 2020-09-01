import React from 'react';
import {Link} from 'react-router-dom';

const Category = (props) => {

    const history = (props.history || []);

/*    function handleClick() {
        history.push(`/landmarks/${props.landmark.label}`);
    } */
	
//	console.log(props.category);

    function cardHeader() {
        return (
            <div className={"card-header"}>
                <div className={"row"}>
                    <div className={"text-left col-md-6"}>
                        <div className={"card-title"}>
                            <h1 className={"display-6"}>
                                {props.category.label}
                            </h1>
                        </div>
                    </div>
                    <div className={"text-right col-md-6"}>
						<a href={"#"} className={"btn btn-outline-danger mr-1"} title={"Add to favourite categories"}>
							<i className={"fa fa-heart"}></i>
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
                     alt={props.category.caption} src={props.category.depiction}/>
                     <div className={"card-text"}>
                         {props.category.comment}
                     </div>
            </div>
        );
    }

    function cardFooter() {
        return (
            <div className={"card-footer"}>
                <Link to={`/landmarks`} className={"btn btn-primary"}>
					{"List all attractions in category"}
                </Link>
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

export default Category;