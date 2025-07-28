import { vatRatesEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IVatRate, IVatRateParams } from 'app/shared/model/vat-rate.model';
import axios from 'axios';

const getVatRates = async (filter?: IVatRateParams) => {
  try {
    const url = vatRatesEndpoints.getVatRates;

    const response = await axios.get<PaginationResponse<IVatRate>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
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

const createVatRate = async (data: IVatRate) => {
  const url = vatRatesEndpoints.postVatRate;

  return axios.post<IVatRate>(url, data);
};

const updateVatRate = async (data: IVatRate, id: string) => {
  const url = vatRatesEndpoints.patchVatRate(id);

  return axios.patch<IVatRate>(url, data);
};

const getVatRateById = async (id: string) => {
  try {
    const url = vatRatesEndpoints.getVatRateById(id);

    const response = await axios.get<IVatRate>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteVatRate = async (id: string) => {
  const url = vatRatesEndpoints.deleteVatRate(id);

  return axios.delete(url);
};

export default {
  getVatRates,
  createVatRate,
  updateVatRate,
  getVatRateById,
  deleteVatRate,
};
