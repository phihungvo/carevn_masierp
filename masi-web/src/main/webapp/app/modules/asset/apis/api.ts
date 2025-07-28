import axios from "axios";
import { Asset, AssetList } from "../types/list";
import { paramsExportExcel } from "app/constants/common";

const rootEndpoint = '/services/masilogistics/api/inventories-storages';

const endpoint = {
    list: rootEndpoint,
}

export const assetApis = {
    list: (params: any) => () => {
        return axios.get<AssetList>(endpoint.list + '/depreciation', { params });
    },
    detail: (id: string) => () => {
        return axios.get<Asset>(rootEndpoint + '/' + id);
    },
    create: (data: any)  => {
        return axios.post<Asset>(rootEndpoint, data);
    },
    update: (id: string) => (data?: any)  => {
        return axios.patch<Asset>(rootEndpoint + '/' + id, data);
    },
    exportExcel: () => {
        return axios.get(rootEndpoint + '/export', paramsExportExcel as any);
    },
    arises: (id: string) => () => {
        return axios.get(`/services/masilogistics/api/inventories-storages/arises/${id}`)
    }
}