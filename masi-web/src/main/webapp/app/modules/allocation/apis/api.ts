import { paramsExportExcel } from "app/constants/common"
import { Depreciation } from "app/modules/depreciation/types/list"
import { PaginationResponse } from "app/shared/model/pagination.model"
import axios from "axios"
import { Allociation } from "../types/list"

const root = '/services/masilogistics/api/item-asset-depreciations'

const endpoint = {
    list: root,
    detail: (id: string) => `${root}/${id}`,
    create: root,
    update: (id: string) => `${root}/${id}`,
}

export const allocationApi = {
    list: (params: any) => () => {
        return axios.get<PaginationResponse<Depreciation>>(endpoint.list, { params })
    },
    detail: (id: string) => () => {
        return axios.get<Allociation>(endpoint.detail(id))
    },
    create: (data: any) => {
        return axios.post(endpoint.create, data)
    },
    update: (id: string) => (data: any) => {
        return axios.patch(endpoint.update(id), data)
    },
    cancel: (id: string) => () => {
        return axios.patch(`${root}/${id}/cancel`)
    },
    approve: (id: string) => (data?: any) => {
        return axios.patch(`${root}/${id}/review`, data)
    },
    reject: (id: string) => (data?: any) => {
        return axios.patch(`${root}/${id}/review`, data)
    },
    requestAccept: (id: string) => () => {
        return axios.patch(`${root}/${id}/request-review`)
    },
    exportExcel: () => {
        return axios.get(`${endpoint.list}/export`, paramsExportExcel as any)
    },
    delete: (id: string) => (values: any) => {
        return axios.delete(endpoint.update(id)).then(() => {
            return axios.post(endpoint.create, values)
        }).catch(() => {
            throw new Error('Lỗi')
        })
    }
}