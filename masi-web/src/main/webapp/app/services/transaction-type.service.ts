import { transactionTypeEndpoints } from 'app/constants/endpoints';
import { ITransactionType, ITransactionTypeParams } from 'app/shared/model/transaction-type.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getTransactionType = async (params: ITransactionTypeParams) => {
  try {
    const url = transactionTypeEndpoints.getTransactionType;
    const response = await axios.get<PaginationResponse<ITransactionType>>(url, {
      params,
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
  getTransactionType,
};
