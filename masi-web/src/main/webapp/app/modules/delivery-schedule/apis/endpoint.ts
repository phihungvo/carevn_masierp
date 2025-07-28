import { checkFormatDateYear, formatDateYear } from "app/shared/util/date-utils"
import axios from "axios"
import dayjs from "dayjs"

const sellDeliverySchedulePath = '/services/masisale/api/delivery-details'

const purchaseDeliverySchedulePath = '/services/masilogistics/api/delivery-details'

export const defaultFilter = {
  page: 0,
  size: 2000000,
}

const deliveryScheduleEndpoint = {
  purchase: {
    list: purchaseDeliverySchedulePath,
    table: purchaseDeliverySchedulePath + '/table',
    calendar: purchaseDeliverySchedulePath + '/calendar',
    create: purchaseDeliverySchedulePath,
    update: (id: string) => `${purchaseDeliverySchedulePath}/${id}`,
  },
  sell: {
    list: sellDeliverySchedulePath,
    table: sellDeliverySchedulePath + '/table',
    calendar: sellDeliverySchedulePath + '/calendar',
    detail: (id: string) => `${sellDeliverySchedulePath}/${id}`,
    create: sellDeliverySchedulePath,
    update: (id: string) => `${sellDeliverySchedulePath}/${id}`,
  }
}

const sellPath = deliveryScheduleEndpoint.sell
const purchasePath = deliveryScheduleEndpoint.purchase

export const deliveryScheduleApi = {
  purchase: {
    table: (filter: any) => () => {
      const cloneFilter = { ...filter }
      delete cloneFilter?.calendar
      delete cloneFilter?.table
      cloneFilter.startDate = dayjs(filter?.table?.deliveryDate).format('YYYY-MM-DD')
      cloneFilter.endDate = dayjs(filter?.table?.expectedReceiveDate).format('YYYY-MM-DD')
      return axios.get(purchasePath.table, { params: cloneFilter })
    },
    calendar: (filter: any) => () => {
      const cloneFilter = { ...filter }
      delete cloneFilter?.calendar
      delete cloneFilter?.table
      cloneFilter.startDate = dayjs(filter?.calendar?.deliveryDate).format('YYYY-MM-DD')
      cloneFilter.endDate = dayjs(filter?.calendar?.expectedReceiveDate).format('YYYY-MM-DD')
      return axios.get(purchasePath.calendar, { params: cloneFilter })
    },
    scheduleList: (params: any) => () => {
      return axios.get(purchasePath.list, { params })
    },
    create: (data: any) => {
      return axios.post(purchasePath.create, data)
    },
    update: (id: string) => (data: any) => {
      return axios.patch(purchasePath.update(id), data)
    },
    supplierContracts: (params?: any) => () => {
      return axios.get('/services/masilogistics/api/supplier-contracts', { params })
    },
    supplierContractDetail: (id: string) => () => {
      return axios.get(`/services/masilogistics/api/supplier-contracts/${id}`)
    },
    delete: (id: string) => () => {
      return axios.delete(purchasePath.update(id))
    }
  },
  sell: {
    table: (params: any) => () => {
      const cloneFilter = { ...params }
      delete cloneFilter?.calendar
      delete cloneFilter?.table
      cloneFilter.startDate = dayjs(params?.table?.deliveryDate).toISOString()
      cloneFilter.endDate = dayjs(params?.table?.expectedReceiveDate).toISOString()
      return axios.get(sellPath.table, { params: cloneFilter })
    },
    calendar: (params: any) => () => {
      const cloneFilter = { ...params }
      delete cloneFilter?.calendar
      delete cloneFilter?.table
      cloneFilter.startDate = dayjs(params?.calendar?.deliveryDate).toISOString()
      cloneFilter.endDate = dayjs(params?.calendar?.expectedReceiveDate).toISOString()
      return axios.get(sellPath.calendar, { params: cloneFilter })
    },
    scheduleList: (params: any) => () => {
      let converted = formatDateYear(params['deliveryDate.equals'])
      return axios.get(sellPath.list, { 
        params: { 
          ...params, 
          'deliveryDate.equals': converted,
        }})
    },
    detail: (id: string) => () => {
      return axios.get(sellPath.detail(id))
    },
    create: (data: any) => {
      return axios.post(sellPath.create, data)
    },
    update: (id: string) => (data: any) => {
      return axios.patch(sellPath.update(id), data)
    },
    orders: (id: string, params?: any) => () => {
      return axios.get(`/services/masisale/api/orders/${id}`, { params })
    },
    orderDetail: (id: string) => () => {
      return axios.get(`/services/masisale/api/orders/${id}`)
    },
    delete: (id: string) => () => {
      return axios.delete(sellPath.update(id))
    }
  }
}
