import axios from '../custom_axios/axios';

const CategoriesService = {
    fetchAllCategories: () => {
        return axios.get(`api/categories`);
    },
    fetchCategoryByName: (name) => {
        return axios.get(`/api/categories/${name}`);
    },
	fetchCategoriesPaged: (page, pageSize) => {
		return axios.get(`/api/categories`, {
			headers: {
				'page': page, 
				'page-size': pageSize,
			}, 
		});
	},
}

export default CategoriesService;