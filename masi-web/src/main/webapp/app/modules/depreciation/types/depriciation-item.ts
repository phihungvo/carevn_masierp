export type DepriciationItem = {
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
    accumulated:         number;
    depreciationRate:    number;
    depreciationValue:   number;
    notes:               string;
    attribute:           InventoriesDetailClass;
    itemInfo:            ItemInfo;
    isDeleted:           boolean;
    createdAt:           string;
    createdBy:           string;
    updatedAt:           string;
    updatedBy:           string;
    company:             string;
    department:          string;
    warehouseId:         string;
    warehouse:           Warehouse;
    inventoriesStorage:  InventoryStorage;
    accumulatedAmortizationAmount: number;
    amortizationAmount:  number;
    amortizationRate:    number;
    amortizedCostInformation: string;
    inventoriesStorageId: string
}

export type InventoriesDetailClass = {
}

export type Item = {
    id:             string;
    code:           string;
    name:           string;
    uom:            Uom;
    itemCategory:   ItemCategory;
    percentProtein: number;
    supplierId:     string;
    supplier:       Supplier;
}

export type Supplier = {
    id:              string;
    code:            string;
    birthday:        Date;
    name:            string;
    email:           string;
    address:         string;
    phone:           string;
    taxCode:         string;
    paymentTerm:     Date;
    fax:             string;
    note:            string;
    isActive:        boolean;
    fullName:        string;
    position:        string;
    createAt:        Date;
    createBy:        string;
    updateAt:        Date;
    updateBy:        string;
    company:         string;
    supplierGroupId: string;
}

export type Uom = {
    id:       string;
    name:     string;
    createAt: Date;
    createBy: string;
    company:  string;
}


export type ItemCategory = {
    isDeleted: boolean;
}

export type ItemInfo = {
    id:                 string;
    registrationNumber: string;
    usageDate:          string;
    inventoryStorageId: string;
    attribute:          ItemInfoAttribute;
    isDeleted:          boolean;
    createdAt:          string;
    createdBy:          string;
    company:            string;
    department:         string;
}

export type ItemInfoAttribute = {
    otherInformation: OtherInformation;
}

export type OtherInformation = {
    createdAt:   string;
    createdBy:   string;
    updatedAt:   string;
    updatedBy:   string;
    createdById: string;
    updatedById: string;
}

export type Warehouse = {
    id:   string;
    code: string;
    name: string;
}

export type InventoryStorage = {
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
export type Attribute = {
    origin: string;
}

