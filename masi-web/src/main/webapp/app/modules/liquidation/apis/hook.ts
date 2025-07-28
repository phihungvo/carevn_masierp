import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import useModalRedux from "app/hooks/use-modal-redux"
import useSearchQuery from "app/hooks/use-search-query"
import { liquidationApi } from "./axios"

const liquidationList = 'liquidationList'
const liquidationDetail = 'liquidationDetail'

export const useLiquidationList = () => {
    const query = useSearchQuery<any>({
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
        queryKey: [liquidationList, query?.query],
        queryFn: liquidationApi.list(query?.query),
        placeholderData: old => old
    })

    return {
        ...query,
        liquidationRes: apiRes
    }
}

export const useLiquidationDetail = (id?: string) => {
    const apiRes = useQuery({
        queryKey: [liquidationDetail, id],
        queryFn: liquidationApi.detail(id),
        enabled: !!id,
        select: res => {
            let data = res?.data
            return {
                ...data,
                ...data?.attribute,
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

export const useLiquidationCreate = () => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.create,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            handleToggleSuccessModal({
                content: 'Tạo thanh lý tài sản thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Tạo thanh lý tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useLiquidationUpdate = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.update(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            queryClient.invalidateQueries({
                queryKey: [liquidationDetail, id]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật thanh lý tài sản thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Cập nhật thanh lý tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useCancelLiquidation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.cancel(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            queryClient.invalidateQueries({
                queryKey: [liquidationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return apiRes
}

export const useApproveLiquidation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.approve(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            queryClient.invalidateQueries({
                queryKey: [liquidationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return apiRes
}

export const useRejectLiquidation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.reject(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            queryClient.invalidateQueries({
                queryKey: [liquidationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return apiRes
}

export const useRequestAcceptLiquidation = (id: string, onSuccess: Function, onError: Function) => {
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: liquidationApi.requestAccept(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [liquidationList]
            })
            queryClient.invalidateQueries({
                queryKey: [liquidationDetail, id]
            })
            onSuccess()
        },
        onError: () => {
            onError()
        }
    })

    return apiRes
}