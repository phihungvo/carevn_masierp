import axios from 'axios';

import { configEndpoint } from 'app/constants/endpoints';
import { IParameterConfig } from 'app/shared/model/config.model';

const getConfig = async (key: string, data: IParameterConfig) => {
    try {
        const url = configEndpoint.getConfig(key)
        const response = await axios.post(url, data);
        return response;
    } catch (error) {
        console.error(error);
    }
};

export default {
    getConfig,
};
