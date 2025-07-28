import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { DATE_FORMAT } from "app/constants/common"
import useModalRedux from "app/hooks/use-modal-redux"
import useSearchQuery from "app/hooks/use-search-query"
import { IOrder } from "app/shared/model/order.model"
import { startAndEndOfCalendar } from "app/shared/util/date-utils"
import dayjs from "dayjs"
import { SupplierContract } from "../Types/detailt"
import useDeliverySearchParams from "../useDeliverySearchParams"
import { isSupplierItem } from "../utils/checkType"
import { deliveryScheduleApi } from "./endpoint"

export const sellTable = 'SELL_TABLE_DELIVERY_SCHEDULE'
export const sellCalendar = 'SELL_CALENDAR_DELIVERY_SCHEDULE'

export const purchaseTable = 'PURCHASE_TABLE_DELIVERY_SCHEDULE'
export const purchaseCalendar = 'PURCHASE_CALENDAR_DELIVERY_SCHEDULE'

export const purchaseItemList = 'PURCHASE_ITEM_LIST_DELIVERY_SCHEDULE'
export const sellItemList = 'SELL_ITEM_LIST_DELIVERY_SCHEDULE'

export const purchaseScheduleList = 'PURCHASE_SCHEDULE_LIST_DELIVERY_SCHEDULE'
export const sellScheduleList = 'SELL_SCHEDULE_LIST_DELIVERY_SCHEDULE'

const purchaseApi = deliveryScheduleApi.purchase
const sellApi = deliveryScheduleApi.sell

export const useDeliveryScheduleList = () => {
  const {
    isCalendar, isTable, isPurchase, isSell,
  } = useDeliverySearchParams()

  const searchQuery = useSearchQuery<any>({
    defaultValue: {
      query: {
        page: 1,
        size: 10,
        table: {
          'deliveryDate': dayjs().add(-2, 'days').toISOString(),
          'expectedReceiveDate': dayjs().endOf('month').toISOString()
        },
        calendar: {
          'deliveryDate': startAndEndOfCalendar(dayjs()).start.toISOString(),
          'expectedReceiveDate': startAndEndOfCalendar(dayjs()).end.toISOString()
        }
      },
    },
    howToResolveData: (key, value) => {
      return value
    },
  })

  const { query } = searchQuery

  const sellTableQuery = useQuery({
    queryKey: [sellTable, query],
    queryFn: sellApi.table(query),
    placeholderData: old => old,
    enabled: isSell && isTable && !!query?.table?.deliveryDate && !!query?.table?.expectedReceiveDate,
    select: (res) => {
      let lastRow = {
        contractQuantity: 0,
        received: 0,
        remain: 0,
        expectedQuantity: 0,
        planningImport: 0,
        outstandingQuantity: 0,
      }
      res?.data?.data?.forEach(item => {
        lastRow.contractQuantity += ('contractQuantity' in item ? item?.contractQuantity : 0)
        lastRow.received += ('received' in item ? item?.received : 0)
        lastRow.remain += ('remain' in item ? item?.remain : 0)
        lastRow.planningImport += ('planningImport' in item ? item?.planningImport : 0)
        lastRow.outstandingQuantity += ('outstandingQuantity' in item ? item?.outstandingQuantity : 0)
      })
      console.log('lastRow', lastRow);

      return {
        data: res?.data?.data || [],
        lastRow,
        event: [],
        totalRecord: res?.data?.totalRecord || 0
      }
    }
  })

  const sellCalendarQuery = useQuery({
    queryKey: [sellCalendar, query],
    queryFn: sellApi.calendar(query),
    placeholderData: old => old,
    enabled: isSell && isCalendar && !!query?.calendar?.deliveryDate && !!query?.calendar?.expectedReceiveDate,
    select: (res) => {
      return {
        data: [],
        events:
          res?.data?.map(item => ({
            ...item,
            start: dayjs(item?.start)
              ?.toDate(),
            end: dayjs(item?.end)
              ?.toDate(),
          })) || [],
        totalRecord: 0,
      };
    }
  })

  const sellQuery = isSell && isTable ? sellTableQuery : sellCalendarQuery

  const purchaseTableQuery = useQuery({
    queryKey: [purchaseTable, query],
    queryFn: purchaseApi.table(query),
    enabled: isPurchase && isTable && !!query?.table?.deliveryDate && !!query?.table?.expectedReceiveDate,
    select: (res) => {
      let lastRow = {
        contractQuantity: 0,
        received: 0,
        remain: 0,
        expectedQuantity: 0,
        planningImport: 0,
        outstandingQuantity: 0,
      }
      res?.data?.data?.forEach(item => {
        lastRow.contractQuantity += ('contractQuantity' in item ? item?.contractQuantity : 0)
        lastRow.received += ('received' in item ? item?.received : 0)
        lastRow.remain += ('remain' in item ? item?.remain : 0)
        lastRow.planningImport += ('planningImport' in item ? item?.planningImport : 0)
        lastRow.outstandingQuantity += ('outstandingQuantity' in item ? item?.outstandingQuantity : 0)
      })

      return {
        data: res?.data?.data || [],
        lastRow,
        event: [],
        totalRecord: res?.data?.totalRecord || 0
      }
    }
  })

  const purchaseCalendarQuery = useQuery({
    queryKey: [purchaseCalendar, query],
    queryFn: purchaseApi.calendar(query),
    enabled: isPurchase && isCalendar && !!query?.calendar?.deliveryDate && !!query?.calendar?.expectedReceiveDate,
    select: (res) => {
      return {
        data: [],
        events:
          res?.data?.map(item => ({
            ...item,
            start: dayjs(item?.start)
              ?.add(7, 'hours')
              ?.toDate(),
            end: dayjs(item?.end)
              ?.add(7, 'hours')
              ?.toDate(),
          })) || [],
        totalRecord: 0,
      };
    }
  })

  console.log('purchaseCalendarQuery', purchaseCalendarQuery);

  const purchaseQuery = isPurchase && isTable ? purchaseTableQuery : purchaseCalendarQuery

  return {
    apiResponse: isSell ? sellQuery : purchaseQuery,
    filter: searchQuery,
  }
}

export const useDeliveryScheduleMutation = (id?: string) => {
  const { handleToggleSuccessModal } = useModalRedux()
  const queryClient = useQueryClient()

  const { isPurchase, isSell, isTable, isCreate } = useDeliverySearchParams()
  const { onToggleModal } = useDeliverySearchParams()

  const content = `${isCreate ? 'Tạo' : 'Cập nhật'} lịch ${isPurchase ? 'mua hàng' : 'bán hàng'}`

  // validation key
  let isPurchaseKey = isTable ? purchaseTable : purchaseCalendar
  let isSellKey = isTable ? sellTable : sellCalendar
  let revalidateKey = isPurchase ? isPurchaseKey : isSellKey

  // mutation function
  const sellMutation = isSell && isCreate ? sellApi.create : sellApi.update(id)
  const purchaseMutation = isPurchase && isCreate ? purchaseApi.create : purchaseApi.update(id)
  const mutationFn = isPurchase ? purchaseMutation : sellMutation

  const query = useMutation({
    mutationFn,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [revalidateKey]
      })
      handleToggleSuccessModal({ content: content + ' thành công' }, false)
    },
    onError: () => {
      handleToggleSuccessModal({ content: content + ' thất bại' }, false)
    },
    onSettled: () => {
      onToggleModal()()
    }
  })

  return query
}

export const useGetItemList = (id: string) => {
  const {
    isPurchase, isSell,
  } = useDeliverySearchParams()
  const isSupplier = isSupplierItem(isPurchase)

  const queryKey = isPurchase ? purchaseItemList : sellItemList

  const query = useQuery({
    queryKey: [queryKey, id],
    queryFn: isPurchase ? purchaseApi.supplierContractDetail(id) : sellApi.orderDetail(id),
    enabled: !!id,
    select: res => {
      let data: SupplierContract | IOrder = res?.data
      if (!data) return;
      return isSupplier(data) 
      ? data?.supplierContractDetails?.map(item => ({
        contractQuantity: item?.quantity,
        deliveryQuantity: 0,
        contractDetailId: item?.id,
        isChecked: false,
        name: item?.item?.name,
        id: item?.supplyItemId,
      })) : data?.contract?.contractMaterialDTOS?.map(item => ({
        contractQuantity: item?.quantity,
        deliveryQuantity: 0,
        contractDetailId: item?.id,
        isChecked: false,
        name: item?.materialName,
        id: item?.idMaterial,
      }))
    }
  })

  return query
}

export const useScheduleList = (itemId: string, selectedDate?: string) => {
  const {
    isPurchase
  } = useDeliverySearchParams()

  const filter = {
    'contractMaterialId.equals': itemId,
    'deliveryDate.equals': dayjs(selectedDate).format(DATE_FORMAT.YEAR_DATE)
  }

  const axiosFn = isPurchase ? purchaseApi.scheduleList(filter) : sellApi.scheduleList(filter)
  const keyValidate = isPurchase ? purchaseScheduleList : sellScheduleList

  const query = useQuery({
    queryKey: [keyValidate, filter],
    queryFn: axiosFn,
    enabled: !!selectedDate,
    select: res => res?.data
  })

  return query;
}

export const useDeleteDeliverySchedule = (id: string) => {
  const {
    isPurchase, isTable
  } = useDeliverySearchParams()
  const queryClient = useQueryClient()
  const { handleToggleFailModal, handleToggleSuccessModal } = useModalRedux()

  const mutationFn = isPurchase ? purchaseApi.delete(id) : sellApi.delete(id)
  const isPurchaseKey = isTable ? purchaseTable : purchaseCalendar
  const isSellKey = isTable ? sellTable : sellCalendar
  const revalidateKey = isPurchase ? isPurchaseKey : isSellKey;

  const query = useMutation({
    mutationFn,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [revalidateKey]
      })
      handleToggleSuccessModal({ content: `Xóa lịch ${isPurchase ? 'mua hàng' : 'bán hàng'} thành công` }, false)
    },
    onError: () => {
      handleToggleSuccessModal({ content: `Xóa lịch ${isPurchase ? 'mua hàng' : 'bán hàng'} thất bại` }, false)
    },
  })

  return query
}