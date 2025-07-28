import axios from "axios";

import { paymentDetails } from "app/constants/endpoints";
import { IPatchPaymemtDetailsDto, IPaymemtDetails, IPaymemtDetailsParams, IPostPaymemtDetailsDto } from "app/shared/model/payment-details.model";
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from "app/constants/common";

const getPaymentDetails = async (filter: IPaymemtDetailsParams) => {
    try {
        const url = paymentDetails.getPaymentDetails;
        const response = await axios.get<IPaymemtDetails[]>(url, {
            params: {
                page: filter?.page ? filter.page : DEFAULT_PAGE,
                size: filter?.size ? filter?.size : DEFAULT_PAGE_SIZE_NAX,
                search: filter?.search,
                ...(filter?.status && { status: filter?.status }),
                ...(filter?.type && { type: filter?.type }),
                ...(filter?.['paymentRequestId.equals'] && { 'paymentRequestId.equals': filter?.['paymentRequestId.equals'] }),
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

const getPaymentDetailsById = async (id: string) => {
    try {
        return await axios.get<IPaymemtDetails>(paymentDetails.getPaymentDetailsById(id));
    } catch (error) {
        console.error(error);
    }
};

const postPaymentDetails = async (data: IPostPaymemtDetailsDto[]) => {

    const url = paymentDetails.postPaymentDetails;

    return await axios.post<IPaymemtDetails>(url, data);
};

const patchPaymentDetails = async (data: IPatchPaymemtDetailsDto[], id: string) => {
    const url = paymentDetails.patchPaymentDetails(id);

    return await axios.patch<IPaymemtDetails>(url, data);
};

const deletePaymentDetails = async (id: string) => {
    const url = paymentDetails.deletePaymentDetails(id);

    return await axios.delete(url);
};

export default {
    getPaymentDetails,
    getPaymentDetailsById,
    postPaymentDetails,
    patchPaymentDetails,
    deletePaymentDetails
}