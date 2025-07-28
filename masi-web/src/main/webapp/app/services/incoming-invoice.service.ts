import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { incomingInvoiceEndpoints } from 'app/constants/endpoints';
import {
  IIncomingInvoice,
  IIncomingInvoiceParams,
} from 'app/shared/model/incoming-invoice.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

async function getIncomingInvoices(params: IIncomingInvoiceParams) {
  try {
    const url = incomingInvoiceEndpoints.getIncomingInvoices;

    const response = await axios.get<PaginationResponse<IIncomingInvoice>>(
      url,
      {
        params,
      },
    );

    return response;
  } catch (error) {
    console.error(error);
  }
}
async function createIncomingInvoice(incomingInvoice: IIncomingInvoice) {
  try {
    const url = incomingInvoiceEndpoints.postIncomingInvoice;

    const response = await axios.post<IIncomingInvoice>(url, incomingInvoice);

    return response;
  } catch (error) {
    console.error(error);
  }
}

async function getIncomingInvoiceById(id: string) {
  try {
    const url = incomingInvoiceEndpoints.getIncomingInvoiceById(id);

    const response = await axios.get<IIncomingInvoice>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
}

async function getNextIncomingInvoiceNumber() {
  try {
    const url = incomingInvoiceEndpoints.getNextInvoiceNo;
    const response = await axios.get<{
      nextAndIncrement: string;
      currentSequence: number;
    }>(url);
    return {
      ...response.data,
      nextAndIncrement: response.data?.nextAndIncrement ?? '',
    };
  } catch (error) {
    console.error(error);
  }
}

async function patchIncomingInvoice(data: IIncomingInvoice) {
  try {
    const url = incomingInvoiceEndpoints.patchIncomingInvoice(data.id);
    const response = await axios.patch<IIncomingInvoice>(url, {
      ...data,
    });
  } catch (error) {
    console.error(error);
  }
}

async function deleteIncomingInvoice(id: string) {
  try {
    const url = incomingInvoiceEndpoints.deleteIncomingInvoice(id);
    const response = await axios.delete(url);
  } catch (error) {
    console.error(error);
  }
}

export default {
  getIncomingInvoices,
  createIncomingInvoice,
  getIncomingInvoiceById,
  getNextIncomingInvoiceNumber,
  patchIncomingInvoice,
  deleteIncomingInvoice,
};
