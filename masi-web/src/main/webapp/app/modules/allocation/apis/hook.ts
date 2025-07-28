import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import useEmployeesObj from "app/hooks/use-employees-object"
import useModalRedux from "app/hooks/use-modal-redux"
import useSearchQuery from "app/hooks/use-search-query"
import { Filter } from "app/modules/depreciation/types/filter"
import { useEffect } from "react"
import { allocationApi } from "./api"
import { isAxiosError } from "axios"

export const allocationList = 'allocationList'
export const allocationDetail = 'allocationDetail'

export const useAllocationList = () => {
    const query = useSearchQuery<Filter>({
        defaultValue: {
            query: {
                page: 0,
                size: 10,
            }
        },
        howToResolveData: (key, value) => {
            switch (key) {
                default:
                    return value
            }
        }
    })

    const apiRes = useQuery({
        queryKey: [allocationList, query.query],
        queryFn: allocationApi.list(query.query)
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
        allocationRes: apiRes,
        employeesObj
    }
}

export const useAllocationDetail = (id?: string) => {
    const apiRes = useQuery({
        queryKey: [allocationDetail, id],
        queryFn: allocationApi.detail(id),
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
        }
    })

    return apiRes
}

export const useAllocationCreate = () => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.create,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            handleToggleSuccessModal({
                content: 'Tạo phân bổ công cụ dụng cụ thành công'
            })
        },
        onError: (err) => {
            if (isAxiosError(err) && err?.response?.data?.message === 'error.ItemAssetDepreciationExists') {
                handleToggleFailModal({
                    content: 'Kỳ phân bổ hoặc ngày tính phân bổ đã tồn tại'
                })
                return
            }
            handleToggleFailModal({
                content: 'Tạo phân bổ công cụ dụng cụ thất bại'
            })
        }
    })

    return mutation
}

export const useAllocationUpdate = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.update(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            queryClient.invalidateQueries({
                queryKey: [allocationDetail, id]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật phân bổ công cụ dụng cụ thành công'
            })
        },
        onError: (err) => {
            if (isAxiosError(err) && err?.response?.data?.message === 'error.ItemAssetDepreciationExists') {
                handleToggleFailModal({
                    content: 'Kỳ phân bổ hoặc ngày tính phân bổ đã tồn tại'
                })
                return
            }
            handleToggleFailModal({
                content: 'Cập nhật phân bổ công cụ dụng cụ thất bại'
            })
        }
    })

    return mutation
}

export const useAllocationApprove = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.approve(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            queryClient.invalidateQueries({
                queryKey: [allocationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return mutation
}

export const useAllocationReject = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.reject(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            queryClient.invalidateQueries({
                queryKey: [allocationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return mutation
}

export const useAllocationRequestAccept = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.requestAccept(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            queryClient.invalidateQueries({
                queryKey: [allocationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return mutation
}

export const useAllocationCancel = (id: string, onSuccess?: Function, onError?: Function) => {
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.cancel(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            queryClient.invalidateQueries({
                queryKey: [allocationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return mutation
}

export const useAllociationDelete = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const mutation = useMutation({
        mutationFn: allocationApi.delete(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [allocationList]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật phân bổ công cụ dụng cụ thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Cập nhật phân bổ công cụ dụng cụ thất bại'
            })
        }
    })

    return mutation
}