import { checkFormatDateYear, formatDateYear } from "app/shared/util/date-utils";
import dayjs from "dayjs";
import { cloneDeep } from "lodash";

export const convertValues = (payload: any, isCreate: boolean, isSell: boolean) => {
    let values = cloneDeep(payload.body)
    
    if (!isSell) {
        values.deliveryDate = dayjs(values.deliveryDate)?.format('YYYY-MM-DD')
    }
    if (isSell && checkFormatDateYear(values?.actualDeliveryDate)) {
        values.actualDeliveryDate = formatDateYear(values?.actualDeliveryDate)
    }

    if (!isCreate) {
        return {
            ...values,
            id: values?.id,
            contractMaterialId: values?.deliveryDetail?.[0]?.id,
            quantity: values?.quantity,
            address: values?.deliveryLocation,
        }
    }

    let tmp = []

    values?.deliveryDetail?.forEach((item) => {
        if (!item?.isChecked) return;
        tmp.push({
            ...values,
            id: values?.id,
            contractDetailId: item?.contractDetailId,
            contractMaterialId: item?.id,
            quantity: item?.deliveryQuantity,
            address: values?.deliveryLocation,
            supplierContractId: values?.contractId,
        })
    })

    return tmp
}