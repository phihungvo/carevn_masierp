import { orderEndpoints } from 'app/constants/endpoints';
import {
  IOrder,
  IOrderParams,
  IOrderWithManufactureOrders,
  OrderReview,
  OrderReviewCreate,
} from 'app/shared/model/order.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IProductionOrdersParams } from 'app/shared/model/production-command.model';
import axios from 'axios';

const getOrders = async (filter?: IOrderParams) => {
  try {
    const url = orderEndpoints.getOrders;

    const response = await axios.get<PaginationResponse<IOrder>>(url, {
      params: {
        ...filter,
        page: filter?.page,
        size: filter?.size,
        searchString: filter?.searchString,
        ...(filter?.statuses?.length && { statuses: filter.statuses }),
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const createOrder = async (order: IOrder) => {
  const url = orderEndpoints.postOrder;

  return axios.post<IOrder>(url, order);
};

const updateOrder = async (order: IOrder, id: string) => {
  const url = orderEndpoints.patchOrder(id);

  return axios.patch<IOrder>(url, order);
};

const getOrderById = async (id: string) => {
  try {
    const url = orderEndpoints.getOrderById(id);

    const response = await axios.get<IOrder>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteOrder = async (id: string) => {
  const url = orderEndpoints.deleteOrder(id);

  return axios.delete(url);
};

const cancelOrder = async (id: string) => {
  const url = orderEndpoints.patchOrderCancel(id);

  return axios.patch(url);
};

const postOrderReview = async (data: OrderReviewCreate) => {
  const url = orderEndpoints.postOrderReview;

  return axios.post(url, data);
};

const patchOrderReview = async (data: OrderReview, id: string) => {
  const url = orderEndpoints.patchOrderReview(id);

  return axios.patch(url, data);
};

const getOrderReviewByOrderId = async (orderId: string) => {
  try {
    const url = orderEndpoints.getOrderReviewByOrderId(orderId);

    return axios.get<OrderReview[]>(url);
  } catch (error) {
    console.error(error);
  }
};

const getOrderExport = async (id: string, download: boolean) => {
  try {
    const url = orderEndpoints.getOrderExport(id, download);
    const response = await axios.get(url, {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getOrdersWithManufactureOrders = async (
  filter?: IProductionOrdersParams,
) => {
  try {
    const url = orderEndpoints.getOrdersWithManufactureOrders;

    const response = await axios.get<
      PaginationResponse<IOrderWithManufactureOrders>
    >(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        searchString: filter?.searchString,
        ...(filter?.startDate && { startDate: filter.startDate }),
        ...(filter?.endDate && { endDate: filter.endDate }),
        ...(filter?.statuses?.length && { statuses: filter.statuses }),
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getOrders,
  createOrder,
  updateOrder,
  deleteOrder,
  cancelOrder,
  getOrderById,
  getOrderExport,
  postOrderReview,
  patchOrderReview,
  getOrderReviewByOrderId,
  getOrdersWithManufactureOrders,
};
