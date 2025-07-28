import { PaginationResponse } from "app/shared/model/pagination.model";
import { Move } from "./move";

export type AssetList = PaginationResponse<Asset>;

export type Asset = {
    id:                  string;
    code:                string;
    itemId:              string;
    item:                Item;
    inventoriesDetailId: string;
    inventoriesDetail:   InventoriesDetailClass;
    importDate:          string;
    exportDate:          string;
    depreciation:        string;
    expiryDate:          string;
    itemType:            string;
    quantity:            number;
    status:              string;
    price:               number;
    remainingPrice:      number;
    notes:               string;
    attribute:           InventoriesDetailClass;
    itemInfo:            ItemInfo;
    isDeleted:           boolean;
    createdAt:           string;
    createdBy:           string;
    company:             string;
    department:          string;
    warehouseId:         string;
    warehouse:           Warehouse;
    itemAssetTransfers:  Move[];
    itemAssetDepreciationDetails: Arise[];
    updatedBy:           string;
    updatedAt:           string;
}

export type InventoriesDetailClass = {
}

export type ItemSubCategory = {
    id:        string;
    code:      string;
    attribute: string;
    name:      string;
    isDeleted: boolean;
    createdAt: Date;
    createdBy: string;
    company:   string;
}

export type ItemCategory = {
    isDeleted: boolean;
    code?: string
}

export type ItemInfo = {
    id:                  string;
    code:                string;
    name:                string;
    registrationNumber:  string;
    registrationDate:    string;
    handoverNumber:      string;
    handoverDate:        string;
    handoverBy:          string;
    handoverById:        string;
    userId:              string;
    userPosition:        string;
    seriesNumber:        string;
    usageDate:           string;
    invoiceNumber:       string;
    invoiceDate:         string;
    inventoryStorageId:  string;
    includedAccessories: IncludedAccessor[];
    note:                string;
    status:              string;
    liquidationDate:     string;
    unit:                string;
    yearOfUse:           number;
    monthOfUse:          number;
    warrantyPeriod:      string;
    manufacturer:        string;
    isMadeIn:            boolean;
    specs:               string;
    removalDate:         string;
    reasonForRemoval:    string;
    attribute:           ItemInfoAttribute;
    isDeleted:           boolean;
    createdAt:           string;
    createdBy:           string;
    company:             string;
    department:          string;
    itemSubCategory:     ItemSubCategory;
    itemSubCategoryId:   string;
}

export type ItemInfoAttribute = {
    material:               { [key: string]: number };
    material_id:            string;
    IncludedAccessoriesDTO: IncludedAccessor[];
    relativedDocuments:     any;
    otherInformation:       OtherInformation;
    dateManufacture:        string;
    reasonForEnteringAsset: string;
    generalInformationNote: string;
}

export type RelativedDocument = {
    id:          string;
    code:        string;
    name:        string;
    price:       number;
    itemId:      string;
    status:      string;
    addDate:     string;
    quantity:    number;
    brokenDate:  string;
    warehouseId: string;
}

export type OtherInformation = {
    createdBy:     string;
    createdAt:     string;
    updatedBy:     string;
    updatedAt:     string;
    software:      string;
    pxEmployee:    string;
    pxInformation: string;
    createdById?:  string;
    updatedById?:  string;
}

export type IncludedAccessor = {
    id:          string;
    code:        string;
    name:        string;
    price:       number;
    itemId:      string;
    status:      string;
    addDate:     string;
    quantity:    number;
    brokenDate:  string;
    warehouseId: string;
}

export type Warehouse = {
    id:   string;
    code: string;
    name: string;
}

export type Arise = {
    id:                            string;
    inventoriesStorageId:          string;
    inventoriesStorage:            InventoriesStorage;
    note:                          string;
    costInformation:               string;
    amortizedCostInformation:      string;
    amortizationAmount:            number;
    amortizationRate:              number;
    accumulatedAmortizationAmount: number;
    recipe:                        string;
    itemAssetDepreciationId:       string;
    isDeleted:                     boolean;
    createdAt:                     Date;
    createdBy:                     string;
    company:                       string;
    department:                    string;
}

export type InventoriesStorage = {
    id:                  string;
    code:                string;
    itemId:              string;
    item:                Item;
    inventoriesDetailId: string;
    importDate:          Date;
    itemType:            string;
    quantity:            number;
    status:              string;
    price:               number;
    remainingPrice:      number;
    notes:               string;
    isDeleted:           boolean;
    createdAt:           Date;
    createdBy:           string;
    updatedAt:           Date;
    updatedBy:           string;
    company:             string;
    department:          string;
    warehouseId:         string;
}

export type Item = {
    id:             string;
    code:           string;
    name:           string;
    uomId:          string;
    attribute:      Attribute;
    company:        string;
    isDeleted:      boolean;
    createdDate:    Date;
    itemCategoryId: string;
    itemTypeId:     string;
    percentProtein: number;
    vatRate:        number;
    vatId:          string;
    unitPrice:      number;
    supplierId:     string;
    itemType:       string;
    isActive:       boolean;
    itemCategory:   ItemCategory;
}

export type Attribute = {
    origin: string;
}

export type Arises = {
    title: string,
    date: string,
    reflectNumber: string,
    ttcp: number,
    quantity: number,
    price: number,
    depriciation: number,
    KhMonth: string,
    note: string,
    recourseCode: string,
}

