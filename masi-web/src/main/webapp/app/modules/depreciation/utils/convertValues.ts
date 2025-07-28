import dayjs from "dayjs";
import { FormSchemaType } from "../validations/form.validation";
import { DATE_FORMAT } from "app/constants/common";

export const convertValues = (values: FormSchemaType, isEditMode: boolean) => {
  console.log('values', {...values});
    let tmp = []
    for (let index in values.itemAssetDepreciationDetails) {
      let item = values.itemAssetDepreciationDetails[index]
      if (item?.isCustomRecipe) {
        if (!isEditMode || values?.isDeleteAll) {
          item.inventoriesStorageId = item?.id
        }
        if (!item?.isCalculated) {
          let percent = (+item?.amortizationAmount * 100) / +item?.originalCost
          item.amortizationRate += percent
          item.accumulatedAmortizationAmount += item?.amortizationAmount
        }
        tmp.push(item)
      }
    }
    delete values.itemAssetDepreciationDetails
    delete values.itemAssetDepreciationDetailsTmp
    return {
        ...values,
        "depreciationDate": dayjs(values.depreciationDate).format(DATE_FORMAT.YEAR_DATE),
        "accountingDate": dayjs(values.accountingDate).format(DATE_FORMAT.YEAR_DATE),
        "description": values?.description,
        "typePageDepreciation": "AMORTIZATION",
        "itemAssetDepreciationDetails": tmp as any,
      }
}