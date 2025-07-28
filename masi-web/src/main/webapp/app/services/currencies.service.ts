import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { currenciesEndpoints } from 'app/constants/endpoints';
import { ICurrency, ICurrencyParams } from 'app/shared/model/currencies.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getCurrencies = async (filter?: ICurrencyParams) => {
  try {
    const url = currenciesEndpoints.getCurrencies;

    const response = await axios.get<PaginationResponse<ICurrency>>(url, {
      params: {
        page: filter?.page ? filter?.page : DEFAULT_PAGE,
        size: filter?.size ? filter?.size : DEFAULT_PAGE_SIZE_NAX,
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

const createCurrency = async (data: ICurrency) => {
  const url = currenciesEndpoints.postCurrency;

  return axios.post<ICurrency>(url, data);
};

const updateCurrency = async (data: ICurrency, id: string) => {
  const url = currenciesEndpoints.patchCurrency(id);

  return axios.patch<ICurrency>(url, data);
};

const getCurrencyById = async (id: string) => {
  try {
    const url = currenciesEndpoints.getCurrencyById(id);

    const response = await axios.get<ICurrency>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const deleteCurrency = async (id: string) => {
  const url = currenciesEndpoints.deleteCurrency(id);

  return axios.delete(url);
};

export default {
  getCurrencies,
  createCurrency,
  updateCurrency,
  getCurrencyById,
  deleteCurrency,
};
