import axios from '../custom_axios/axios';

const TouristAttractionsService = {
    fetchAllTouristAttractionsByCategory: (category) => {
        return axios.get(`/api/tourist-attractions/${category}`);
    },
    fetchTouristAttractionByCategoryAndName: (category, name) => {
        return axios.get(`/api/tourist-attractions/${category}/${name}`);
    },
    searchTouristAttractionsByCategoryAndCountry: (category, country) => {
        return axios.get(`/api/tourist-attractions/${category}?country=${country}`);
    }, 
	fetchTouristAttractionsByCategoryPaged: (category, page, pageSize) => {
		return axios.get(`/api/tourist-attractions/${category}`, {
			headers: {
				'page': page, 
				'page-size': pageSize,
			}, 
		});
	}, 
}

export default TouristAttractionsService;