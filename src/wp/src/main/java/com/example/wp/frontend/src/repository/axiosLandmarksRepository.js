import axios from '../custom_axios/axios';

const LandmarksService = {
    fetchAllLandmarksByCategory: (category) => {
        return axios.get(`/api/landmarks/${category}`);
    },
    fetchLandmarkByCategoryAndName: (category, name) => {
        return axios.get(`/api/landmarks/${category}/${name}`);
    },
    searchLandmarksByCategoryAndCountry : (category, country) => {
        return axios.get(`/api/landmarks/${category}?country=${country}`);
    }, 
	fetchLandmarksByCategoryPaged: (category, page, pageSize) => {
		return axios.get(`/api/landmarks/${category}`, {
			headers: {
				'page': page, 
				'page-size': pageSize,
			}, 
		});
	}, 
}

export default LandmarksService;