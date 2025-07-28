import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import useAccountApp from "app/hooks/use-account-app"
import useModalRedux from "app/hooks/use-modal-redux"
import useSearchQuery from "app/hooks/use-search-query"
import { convertCurrency, format2Digit } from "app/shared/util/format"
import { AssetFilter } from "../types/filter"
import { AssetSchemaType } from "../validations/index.validation"
import { assetApis } from "./api"
import { useFormContext } from "react-hook-form"
import { Arise } from "../validations/arise.validation"
import dayjs from "dayjs"
import { Move } from "../validations/move.validation"

const AssetList = 'ASSET_LIST'
const AssetDetail = 'ASSET_DETAIL'
const AraiseDetail = 'ARAISE_DETAIL'

export const useAssetList = () => {
    const queryFilter = useSearchQuery<AssetFilter>({
        defaultValue: {
            query: {
                size: 10,
                page: 0,
            }
        },
        howToResolveData: (key, value) => {
            switch (key) {
                default:
                    return value
            }
        }
    })

    const assetList = useQuery({
        queryKey: [AssetList, queryFilter.query],
        queryFn: assetApis.list(queryFilter.query),
        select: (data) => data.data
    })

    return {
        ...queryFilter,
        assetList
    }
}

export const useAssetDetail = (id?: string) => {
    const account = useAccountApp()
    const fullName = account?.lastName + ' ' + account?.firstName
    const employeeId = account?.id

    const assetDetail = useQuery({
        queryKey: [AssetDetail, id],
        queryFn: assetApis.detail(id),
        enabled: !!id,
        select: (data) => {
            let res = data?.data
            let itemInfo = res?.itemInfo
            let move = res?.itemAssetTransfers
            let attribute = itemInfo?.attribute
            let included = attribute?.IncludedAccessoriesDTO
            let arised = res?.itemAssetDepreciationDetails
            let otherInformation = itemInfo?.attribute?.otherInformation
            let assetForm: AssetSchemaType = {
                basisInformation: {
                    propertyCode: res?.code, // Mã tài sản
                    codeFormWarehouse: res?.warehouse?.code, // Mã từ kho
                    note: res?.notes, // Diễn giải
                    groupCode: res?.item?.itemCategory?.['id'], // Mã nhóm
                    smallGroup: itemInfo?.itemSubCategoryId, // Tiểu nhóm
                    reason: res?.itemInfo?.attribute?.reasonForEnteringAsset, // Lý do nhập
                    originalPrice: res?.price, // Nguyên giá
                    remainingValue: res?.remainingPrice, // Giá trị còn lại
                    remainingQuantity: res?.quantity, // Số lương còn lại
                    managementUnit: res?.department, // Đơn vị quản lý
                },
                generalInformation: {
                    registerCode: itemInfo?.registrationNumber,
                    registerDate: itemInfo?.registrationDate,
                    numberHandover: itemInfo?.handoverNumber,
                    handoverDate: itemInfo?.handoverDate,
                    handoverPersonId: itemInfo?.handoverById,
                    whoUseId: itemInfo?.userId,
                    locationId: itemInfo?.userPosition,
                    series: itemInfo?.seriesNumber,
                    usingDate: itemInfo?.usageDate,
                    numberBill: itemInfo?.invoiceNumber,
                    billDate: itemInfo?.invoiceDate,
                    warehouseId: res?.warehouseId,
                    note: attribute?.generalInformationNote,
                    liquidationDate: itemInfo?.liquidationDate,
                    quantity: res?.quantity,
                    usageYear: format2Digit((itemInfo?.monthOfUse || 0) / 12),
                    usageMonth: itemInfo?.monthOfUse,
                    warrantyPeriod: itemInfo?.warrantyPeriod,
                    manufacturerId: itemInfo?.manufacturer,
                    isDomestic: itemInfo?.isMadeIn,
                    parameter: itemInfo?.specs,
                    suspensionDay: itemInfo?.removalDate,
                    reason: itemInfo?.reasonForRemoval,
                    dateManufacture: attribute?.dateManufacture,
                    status: res?.status,
                    unitCalculateId: res?.item?.['uom']?.['id']
                },
                includeAccessoriesSchema: included?.map((item) => ({
                    code: item?.code,
                    name: item?.name,
                    unitId: item?.id,
                    quantity: item?.quantity,
                    price: item?.price,
                    statusId: item?.status,
                    brokenDate: item?.brokenDate,
                    addedDate: item?.addDate,
                    save: item?.warehouseId,
                    note: item?.['notes'],
                })),
                move: move?.map((item) => ({
                    date: item?.createdAt,
                    reflect: item?.code,
                    from: item?.fromAddress,
                    to: item?.toAddress,
                    fromTtcp: item?.fromDepartmentId,
                    toTtcp: item?.toDepartmentId,
                    fromNsd: item?.fromPersonId,
                    toNsd: item?.toPersonId,
                    personChange: item?.toPersonId,
                    note: item?.name,
                }) as any),
                otherInformation: {
                    ...otherInformation,
                    createdAt: res?.createdAt,
                    updatedAt: res?.updatedAt,
                    createdBy: res?.createdBy,
                    updatedBy: res?.updatedBy,
                    pxEmployee: otherInformation?.pxEmployee,
                    pxInformation: otherInformation?.pxInformation,
                    software: otherInformation?.software,
                },
                relativedDocument: itemInfo?.attribute?.relativedDocuments,
                // arises: arised?.map((item) => ({
                //     job: item?.,
                //     date: item?.createdAt,
                //     reflectNumber: item?.inventoriesStorage?.code,
                //     ttcp: item?.amortizedCostInformation,
                //     quantity: item?.inventoriesStorage?.quantity,
                //     price: item?.inventoriesStorage?.price,
                //     depriciation: item?.amortizationAmount,
                //     KhMonth: item?.amortizationAmount,
                //     note: item?.note,
                //     recourseCode: item?.inventoriesStorage?.code,
                // }))
            }
            return {
                ...res,
                ...assetForm
            }
        },
    })

    return assetDetail
}

export const useAssetCreate = () => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: assetApis.create,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [AssetList]
            })
            handleToggleSuccessModal({
                content: 'Tạo thông tin tài sản thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Tạo thông tin tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useAssetUpdate = (id: string) => {
    const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux()
    const queryClient = useQueryClient()

    const apiRes = useMutation({
        mutationFn: assetApis.update(id),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: [AssetList]
            })
            queryClient.invalidateQueries({
                queryKey: [AssetDetail, id]
            })
            handleToggleSuccessModal({
                content: 'Cập nhật thông tin tài sản thành công'
            })
        },
        onError: () => {
            handleToggleFailModal({
                content: 'Cập nhật thông tin tài sản thất bại'
            })
        }
    })

    return apiRes
}

export const useArisesDetail = (id: string) => {
    const ariseMap = [
        {
            key: 'InventoriesStorage',
            value: 'Nhập kho'
        }, 
        {
            key: 'AssetTransferDetails',
            value: 'Điều chuyển'
        },
        {
            key: 'ItemAssetDepreciationDetail',
            value: 'Khấu hao'
        }, 
        {
            key: 'ItemLiquidationDetail',
            value: 'Thanh lý'
        }
    ]

    const { getValues, setValue } = useFormContext<AssetSchemaType>()

    const query = useQuery({
        queryKey: [AraiseDetail, id],
        queryFn: assetApis.arises(id),
        select: (res) => {
            let arr: Partial<Arise>[] = []
            let data = res?.data
            ariseMap.forEach((record) => {
                let { key, value } = record
                let item = data?.[key]
                let move = {}
                let title = value
                if (!item) return
                switch (key) {
                    case ariseMap[0].key: // InventoriesStorage
                        if (!item || !Object.keys(item).length) return
                        arr.push({
                            job: title,
                            date: item?.importDate,
                            reflectNumber: item?.inventoriesDetail?.code,
                            ttcp: getValues('basisInformation.codeFormWarehouse'),
                            quantity: item?.quantity,
                            price: item?.price,
                            depriciation: item?.description,
                            KhMonth: undefined,
                            note: item?.notes,
                            recourseCode: undefined,
                        })
                        break;
                    case ariseMap[1].key: // AssetTransferDetails
                        let move: Move[] = []
                        item?.forEach((record) => {
                            let itemDetail = record?.itemAssetTransfer
                            if (
                                !itemDetail || 
                                !['APPROVED', 'COMPLETED'].includes(itemDetail?.status)
                            ) return
                            arr.push({
                                job: title,
                                date: itemDetail?.transferDate,                                
                                reflectNumber: itemDetail?.code,
                                ttcp: getValues('basisInformation.codeFormWarehouse'),
                                note: itemDetail?.description,
                                recourseCode: undefined,
                            })
                            move.push({
                                date: record?.createdAt,
                                reflect: itemDetail?.code,
                                fromEmployee: record?.employeeFromId,
                                toEmployee: record?.employeeToId,
                                fromPosition: itemDetail?.fromDepartmentId,
                                toPosition: itemDetail?.toDepartmentId,
                                personChange: record?.createdBy,
                                note: itemDetail?.description,
                            })
                        })
                        setValue('move', move)
                        break;
                    case ariseMap[2].key: // ItemAssetDepreciationDetail
                        if (!getValues('generalInformation.usageMonth')) return
                        item?.sort((a, b) => (dayjs(a).isAfter(dayjs(b)) ? 1 : -1))?.forEach((record, index) => {
                            let itemDetail = record?.itemAssetDepreciation
                            if (itemDetail?.status !== 'APPROVED' || !record?.amortizationAmount) return
                            arr.push({
                                job: title,
                                date: itemDetail?.depreciationDate,
                                reflectNumber: itemDetail?.code,
                                ttcp: getValues('basisInformation.codeFormWarehouse'),
                                quantity: record?.quantity,
                                depriciation: record?.amortizationAmount,
                                KhMonth: itemDetail?.depreciationDate?.split('-')[1],
                                note: record?.note,
                                recourseCode: undefined,
                            })
                        })
                        break;
                    default: // ItemLiquidationDetail
                        if (
                            !item || 
                            !Object.keys(item).length ||
                            item?.itemLiquidation?.status !== 'APPROVED'
                        ) return
                        arr.push({
                            job: title,
                            date: item?.createdAt,
                            reflectNumber: item?.itemLiquidation?.code,
                            ttcp: getValues('basisInformation.codeFormWarehouse'),
                            quantity: -1,
                            depriciation: undefined,
                            KhMonth: undefined,
                            note: item?.description,
                            recourseCode: undefined,
                        })
                        break;
                }
            })

            return arr
        }
    })

    return query
}