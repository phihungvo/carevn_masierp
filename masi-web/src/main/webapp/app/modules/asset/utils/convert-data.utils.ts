import { DeepPartial } from "react-hook-form";
import { Asset } from "../types/list";
import { AssetSchemaType } from "../validations/index.validation";

export const convertDataToUpdate = (values: AssetSchemaType): DeepPartial<Asset> => {
    let {
        basisInformation,
        generalInformation,
        includeAccessoriesSchema,
        relativedDocument,
        otherInformation
    } = values
    return {
        code: basisInformation?.propertyCode,
        warehouse: {
            code: basisInformation?.codeFormWarehouse
        },
        notes: basisInformation?.note,
        itemInfo: {
            code: basisInformation?.groupCode,
            itemSubCategoryId: basisInformation?.smallGroup,
            registrationNumber: generalInformation?.registerCode,
            registrationDate: generalInformation?.registerDate,
            handoverDate: generalInformation?.handoverDate,
            handoverById: generalInformation?.handoverPersonId,
            userId: generalInformation?.whoUseId,
            userPosition: generalInformation?.locationId,
            seriesNumber: generalInformation?.series,
            usageDate: generalInformation?.usingDate,
            invoiceNumber: generalInformation?.numberBill,
            invoiceDate: generalInformation?.billDate,
            liquidationDate: generalInformation?.liquidationDate,
            unit: generalInformation?.unitCalculateId,
            yearOfUse: generalInformation?.usageYear * 12,
            monthOfUse: generalInformation?.usageMonth,
            warrantyPeriod: generalInformation?.warrantyPeriod,
            manufacturer: generalInformation?.manufacturerId,
            isMadeIn: generalInformation?.isDomestic,
            specs: generalInformation?.parameter,
            removalDate: generalInformation?.suspensionDay,
            reasonForRemoval: generalInformation?.reason,
            attribute: {
                generalInformationNote: generalInformation?.note,
                relativedDocuments: relativedDocument,
                otherInformation,
                dateManufacture: generalInformation?.dateManufacture,
                IncludedAccessoriesDTO: includeAccessoriesSchema?.map((item) => ({
                    code: item?.code,
                    name: item?.name,
                    id: item?.unitId,
                    quantity: item?.quantity,
                    price: item?.price,
                    status: item?.statusId,
                    brokenDate: item?.brokenDate,
                    addDate: item?.addedDate,
                    warehouseId: item?.save,
                    notes: item?.note,
                })),
                reasonForEnteringAsset: basisInformation?.reason,
            }
        },
        price: basisInformation?.originalPrice,
        remainingPrice: basisInformation?.remainingValue,
        quantity: basisInformation?.remainingQuantity,
        department: basisInformation?.managementUnit,
        warehouseId: generalInformation?.warehouseId,
    }
}