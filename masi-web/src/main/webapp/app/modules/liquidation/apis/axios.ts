import { paramsExportExcel } from "app/constants/common"
import { PaginationResponse } from "app/shared/model/pagination.model"
import axios from "axios"
import { Liquidation } from "../types/list"

const path = '/services/masilogistics/api/item-liquidations'

const api = {
    list: path,
    detail: (id: string) => `${path}/${id}`,
    create: path,
    update: (id: string) => `${path}/${id}`,
}

export const liquidationApi = {
    list: (params: any) => () => {
        return axios.get<PaginationResponse<Liquidation>>(api.list, { params })
    },
    detail: (id: string) => () => {
        return axios.get(api.detail(id))
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
}