import axios from "axios";
import { IncomingBody, IncommingDetail } from "../types/api.type";

const rootEndpoint = '/services/masilogistics/api/incoming-invoices';

const endpoint = {
  detail: (id: string) => `${rootEndpoint}/${id}`,
  create: () => rootEndpoint,
  update: (id: string) => `${rootEndpoint}/${id}`,
  requestApprove: (id: string) => `${rootEndpoint}/${id}/send-approve`,
  reject: (id: string) => `${rootEndpoint}/${id}/reject`,
  approve: (id: string) => `${rootEndpoint}/${id}/approve`,
  nextCode: () => `${rootEndpoint}/next-invoice-no`,
  exportExcel: () => `${rootEndpoint}/export`,
};

export const incomingInvoiceApi = {
  list: (params: any) => () => {
    let url = rootEndpoint;
    return axios.get(url, { params });
  },
  detail: (id: string) => () => {
    let url = endpoint.detail(id);
    return axios.get<IncommingDetail>(url);
  },
  create: (data: IncomingBody) => {
    let url = endpoint.create();
    return axios.post<IncomingBody>(url, data)
  },
  update: (id: string) => (data: IncomingBody) => {
    let url = endpoint.update(id);

    return axios.patch(url, data);
  },
  nextCode: () => {
    let url = endpoint.nextCode();
    return axios.get(url);
  },
  exportExcel: (params: any) => {
    let url = endpoint.exportExcel();
    return axios.get(url, {
      params: {
        download: true,
      },
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });
  },
  cancel: (id: string) => {
    let url = `${rootEndpoint}/${id}/cancel`;
    return axios.patch(url);
  }
};
