import { paramsExportExcel } from "app/constants/common"
import { PaginationResponse } from "app/shared/model/pagination.model"
import axios from "axios"
import { DepriciationItem } from "../types/depriciation-item"
import { DepreciationAPIItem } from "../types/detail"
import { Depreciation } from "../types/list"

const path = '/services/masilogistics/api/item-asset-depreciations'

const api = {
    list: path,
    detail: (id: string) => `${path}/${id}`,
    create: path,
    update: (id: string) => `${path}/${id}`,
    depriciationList: '/services/masilogistics/api/inventories-storages/depreciation',
    depriciationItemDetail: (id: string) => `${path}/detail/${id}`,
    delete: (id: string) => `${path}/${id}`
}

export const DepreciationApi = {
    list: (params: any) => () => {
        return axios.get<PaginationResponse<Depreciation>>(api.list, { params })
    },
    detail: (id: string) => () => {
        return axios.get<DepreciationAPIItem>(api.detail(id))
    },
    create: (data: any) => {
        return axios.post(api.create, data)
    },
    update: (id: string) => (data: any) => {
        return axios.patch(api.update(id), data)
    },
    export: () => {
        return axios.get(`${path}/export`, paramsExportExcel as any)
    },
    cancel: (id: string) => () => {
        return axios.patch(`${path}/${id}/cancel`)
    },
    approve: (id: string) => (data?: any) => {
        return axios.patch(`${path}/${id}/review`, data)
    },
    reject: (id: string) => (data?: any) => {
        return axios.patch(`${path}/${id}/review`, data)
    },
    requestAccept: (id: string) => () => {
        return axios.patch(`${path}/${id}/request-review`)
    },
    depriciationList: (params?: any) => () => {
        return axios.get<PaginationResponse<DepriciationItem>>(api.depriciationList, { params: { ...params, checkDepreciation: true } } )
    },
    liquidationDepriciationList: (params?: any) => () => {
        return axios.get<PaginationResponse<DepriciationItem>>(api.depriciationList, { params } )
    },
    depriciationItemDetail: (id: string, params: any) => () => {
        return axios.get<PaginationResponse<DepriciationItem>>(api.depriciationItemDetail(id), { params })
    },
    delete: (id: string) => (values: any) => {
        return axios.delete(api.update(id)).then(() => {
            return axios.post(api.create, values)
        }).catch(() => {
            throw new Error('Lỗi')
        })
    }
}