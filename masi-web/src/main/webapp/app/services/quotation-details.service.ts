import axios from 'axios';

import { quotationDetailsEndpoints } from 'app/constants/endpoints';
import { IQuotationDetail } from 'app/shared/model/quotation.model';

const getQuotationDetailsById = async (id: string) => {
  try {
    const url: string = quotationDetailsEndpoints.getQuotationDetailsById(id);
    const response = await axios.get<IQuotationDetail>(url);
    return response;
  } catch (error) {
    console.error(error);
  }
};

const postQuotationDetails = async (data: IQuotationDetail) => {
  const url: string = quotationDetailsEndpoints.postQuotationDetails;
  return await axios.post(url, data);
};

const patchQuotationDetails = async (id: string, data: IQuotationDetail) => {
  const url: string = quotationDetailsEndpoints.patchQuotationDetails(id);
  return await axios.patch(url, data);
};

const deleteQuotationDetails = async (id: string) => {
  const url: string = quotationDetailsEndpoints.deleteQuotationDetails(id);
  return await axios.delete(url);
};

export default {
  getQuotationDetailsById,
  postQuotationDetails,
  patchQuotationDetails,
  deleteQuotationDetails,
};
