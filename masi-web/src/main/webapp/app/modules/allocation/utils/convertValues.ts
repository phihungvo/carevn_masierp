import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import { AllociationFormSchemaType } from '../validations/allociation-form.validation';

export const convertValues = (values: AllociationFormSchemaType) => {
  let tmp = [];
  for (let index in values.itemAssetDepreciationDetails) {
    let item = values.itemAssetDepreciationDetails[index];
    if (item?.recipe && item?.recipe !== 'default') {
      item.id = item?.inventoriesStorageId;
      tmp.push(item);
    }
  }
  delete values.itemAssetDepreciationDetailsTmp;
  return {
    ...values,
    depreciationDate: dayjs(values.depreciationDate).format(
      DATE_FORMAT.YEAR_DATE,
    ),
    accountingDate: dayjs(values.accountingDate).format(DATE_FORMAT.YEAR_DATE),
    description: values?.description,
    typePageDepreciation: 'AMORTIZATION',
    itemAssetDepreciationDetails: tmp as any,
  };
};
