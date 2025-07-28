import { LiquidationFormSchemaType } from "../validations/liquidation-form.validation";

export const convertValues = (values: LiquidationFormSchemaType) => {
    return {
        ...values,
        itemLiquidationDTODetails: values?.propertyList,
        requestApprovals: values.requestApprovals,
        attribute: {
            humanResource: values.humanResource,
            propertyList: values.propertyList,
        }
    }
}