import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import useModalRedux from "app/hooks/use-modal-redux"
import useSearchQuery from "app/hooks/use-search-query"
import { Filter } from "../types/filter"
import { DepreciationApi } from "./axios"
import useEmployeesObj from "app/hooks/use-employees-object"
import { useEffect, useState } from "react"
import { FormSchemaType } from "../validations/form.validation"
import { isAxiosError } from "axios"
import { useParams } from "react-router"

export const depreciationList = 'depreciationList'
export const depreciationDetail = 'depreciationDetail'

export const depreciationItemList = 'depreciationItemList'
export const depreciationItemDetail = 'depreciationItemDetail'

export const useDepriciationList = () => {
    const query = useSearchQuery<Filter>({
        defaultValue: {
            query: {
                page: 0,
                size: 10,
            },
        },
        howToResolveData: (key, value) => {
            switch (key) {
                default:
                    return value
            }
        }
    })

    const apiRes = useQuery({
        queryKey: [depreciationList, query?.query],
        queryFn: DepreciationApi.list(query?.query),
        placeholderData: old => old
    })
    
    const { employeesObj, handleEmployeeChange } = useEmployeesObj()

    useEffect(() => {
        if (apiRes?.data?.data?.data) {
            const data = apiRes.data.data.data
            handleEmployeeChange(data?.map(item => item.employeeId))
        }
    }, [apiRes?.data?.data?.data])

    return {
        ...query,
        depriciationApi: apiRes,
        employeesObj
    }
}

export const useDepriciationDetail = (id?: string) => {
    const apiRes = useQuery({
        queryKey: [depreciationDetail, id],
        queryFn: DepreciationApi.detail(id),
        enabled: !!id,
        select: (res) => {
            let data = res?.data
            return {
                ...data,
                itemAssetDepreciationDetails: {},
                requestApprovals: res?.data?.requestApprovals?.map((item) => {
                    return {
                      employeeId: item.employeeId,
                      employee: {
                        code: '',
                        fullName: '',
                      },
                      department: item.department,
                      createdAt: item?.createdDate,
                      updatedAt: item?.['updatedAt'],
                      result: item?.['result'],
                    };
                })
            }
        },
    })

    return apiRes
}

export const useDepriciationCreate = () => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.create,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            handleToggleSuccessModal({
                content: 'Tạo khấu hao tài sản thành công'
            })
        },
        onError: (err) => {
            if (isAxiosError(err) && err?.response?.data?.message === 'error.ItemAssetDepreciationExists') {
                handleToggleFailModal({
                    content: 'Kỳ khấu hao hoặc ngày tính khấu hao đã tồn tại'
                })
                return
            }
            handleToggleFailModal({
                content: 'Tạo khấu hao tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useDepriciationUpdate = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.update(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationDetail, id]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationItemDetail]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật khấu hao tài sản thành công'
            })
        },
        onError: (err) => {
            if (isAxiosError(err) && err?.response?.data?.message === 'error.ItemAssetDepreciationExists') {
                handleToggleFailModal({
                    content: 'Kỳ khấu hao hoặc ngày tính khấu hao đã tồn tại'
                })
                return
            }
            handleToggleFailModal({
                content: 'Cập nhật khấu hao tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useApproveDepreciation = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.approve(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return apiRes
}

export const useCancelDepreciation = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.cancel(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationDetail, id]
            })
            onSuccess()
        },
        onError: (error) => {
            console.log('error', error);
            onError()
        }
    })

    return apiRes
}

export const useRejectDepreciation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.reject(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationDetail, id]
            })
            onSuccess()
        },
        onError: (err) => {
            console.log('error', err);
            onError()
        }
    })

    return apiRes
}

export const useRequestAcceptDepreciation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.requestAccept(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            queryClient.invalidateQueries({
                queryKey: [depreciationDetail, id]
            })
            onSuccess()
        },
        onError: (err) => {
            console.log('error', err);
            onError()
        }
    })

    return apiRes
}

export const useDepriciationItemList = (id?: string) => {
    const params = useParams()
    const isEditMode = !!params?.id

    const defaultQuery = {
        page: 0,
        size: 10,
    }

    const query = useSearchQuery<Filter>({
        defaultValue: {
            query: defaultQuery,
        },
        howToResolveData: (key, value) => {
            switch (key) {
                default:
                    return value
            }
        }
    })

    const apiRes = useQuery({
        queryKey: [depreciationItemList, query?.query],
        queryFn: DepreciationApi.depriciationList(query?.query),
        placeholderData: old => old,
        enabled: !!!id,
        select: (res) => {
            let obj = {}
            let data = []
            res?.data?.data?.forEach((item) => {
                let tmp = {
                    ...item,
                    id: item?.id,
                    code: item?.code,
                    note: item?.notes,
                    amortizationAmount: item?.depreciationValue,
                    amortizationRate: item?.depreciationRate,
                    originalCost: item?.price,
                    accumulatedAmortizationAmount: item?.accumulated,
                    recipe: 'default',
                    isCustomRecipe: false,
                    inventoriesStorageId: item?.inventoriesDetailId,
                    old: {
                        amortizationAmount: item?.depreciationValue,
                        amortizationRate: item?.depreciationRate,
                        accumulatedAmortizationAmount: item?.accumulated,
                    },
                    resourceData: {
                        amortizationAmount: item?.depreciationValue,
                        amortizationRate: item?.depreciationRate,
                        accumulatedAmortizationAmount: item?.accumulated,
                    }
                }
                data.push(tmp)
                obj[item?.id] = tmp
            })
            return { data, obj, totalRecord: res?.data?.totalRecord }
        }
    })

    const apiResDetail = useQuery({
        queryKey: [depreciationItemDetail, query?.query],
        queryFn: DepreciationApi.depriciationItemDetail(id, query?.query),
        enabled: !!id,
        select: (res) => {
            let obj = {}
            let data = []
            res?.data?.data?.forEach((item) => {
                let tmp = {
                    ...item,
                    id: item?.id,
                    code: item?.inventoriesStorage?.code,
                    note: item?.['note'],
                    amortizationAmount: item?.amortizationAmount,
                    amortizationRate: item?.amortizationRate,
                    originalCost: item?.inventoriesStorage?.price,
                    accumulatedAmortizationAmount: item?.accumulatedAmortizationAmount,
                    recipe: 'default',
                    isCustomRecipe: false,
                    inventoriesStorageId: item?.inventoriesStorageId,
                    resourceData: {
                        amortizationAmount: item?.amortizationAmount,
                        amortizationRate: item?.amortizationRate,
                        accumulatedAmortizationAmount: item?.accumulatedAmortizationAmount,
                    },
                    old: {
                        amortizationAmount: item?.amortizationAmount,
                        amortizationRate: item?.amortizationRate,
                        accumulatedAmortizationAmount: item?.accumulatedAmortizationAmount,
                    },
                    isCalculated: true,
                    isSubtract: true,
                }
                data.push(tmp)
                obj[item?.id] = tmp
            })
            return { data, obj, totalRecord: res?.data?.totalRecord }
        }
    })

    return {
        ...query,
        depriciationItemApi: !id ? apiRes : apiResDetail,
    }
}

export const useDepreciationDelete = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: DepreciationApi.delete(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [depreciationList]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật khấu hao tài sản thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Cập nhật khấu hao tài sản thất bại'
            })
        }
    })

    return apiRes
}
