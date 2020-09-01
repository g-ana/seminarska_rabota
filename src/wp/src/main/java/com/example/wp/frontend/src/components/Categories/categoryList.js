import React from 'react';
import Category from './category';
import ReactPaginate from 'react-paginate';
import {Link} from 'react-router-dom';

const CategoryList = (props) => {

    const history = (props.history || []);

/*    function handleClick() {
        history.push(`/categories/${props.category.label}`);
    } */

    function getCategories() {
	//	console.log(props.categories);
        const categories = (props.categories || []).map((category, index) => {
            return (
             /*   <Category category={props.category} categoryId={props.category.label} key={index} className={"col-md-6 mt-2 col-sm-12"}/>
                <li className={"list-group-item"} key={index}>
                    <a href={`/categories/${category}`}>{category}</a>
                </li> */
             <div className={"col-2 m-1"}>
                    <Link to={`/categories/${category}`} className={"btn btn-info"}>
						{category}
					</Link>
             </div>
            );
        });
        return (
            <div className={"row m-5"}>
                {categories}
            </div>
        );
    }

    function handlePageClick(event) {
     //   event.preventDefault();
        props.onPageClick = event.selected;
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
                {getCategories()}
            </div>
            <hr/>
            {pagination()}
        </div>
    );
}

export default CategoryList;