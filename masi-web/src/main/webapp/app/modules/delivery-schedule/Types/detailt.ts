import { IItem } from "app/shared/model/item.model";

export type SupplierContract = {
    createdBy:                   string;
    createdAt:                   string;
    updatedBy:                   string;
    updatedAt:                   string;
    company:                     string;
    department:                  string;
    id:                          string;
    contractCode:                string;
    supplierId:                  string;
    supplier:                    Supplier;
    contractDate:                string;
    startDate:                   string;
    endDate:                     string;
    contractAmount:              number;
    paymentTermNumber:           number;
    totalAmount:                 number;
    totalAmountAfterVat:         number;
    totalQuantity:               number;
    supplierFullName:            string;
    supplierPosition:            string;
    supplierPhone:               string;
    supplierEmail:               string;
    note:                        string;
    supplierContractDetails:     SupplierContractDetail[];
    status:                      string;
    suppliesRequest:             SuppliesRequest;
    deliveryEstDate:             string;
    requestApprovals:            any[];
    liquidationRequestApprovals: any[];
    totalPrice:                  number;
}

export type Supplier = {
    id:              string;
    code:            string;
    birthday:        string;
    name:            string;
    email:           string;
    address:         string;
    addressService:  string;
    phone:           string;
    taxCode:         string;
    paymentTerm:     string;
    supplierTypeId:  string;
    fax:             string;
    note:            string;
    isActive:        boolean;
    createAt:        string;
    createBy:        string;
    company:         string;
    supplierGroupId: string;
}

export type SupplierContractDetail = {
    id:                  string;
    supplierContractId:  string;
    supplyItemId:        string;
    unitId:              string;
    price:               number;
    quantity:            number;
    note:                string;
    vatId:               string;
    vatRate:             number;
    vatAmount:           number;
    totalAmount:         number;
    totalAmountAfterVat: number;
    createdBy:           string;
    createdAt:           string;
    isDeleted:           boolean;
    company:             string;
    department:          string;
    item?:               IItem;
}

export type SuppliesRequest = {
    totalAmount:         number;
    totalAmountAfterVat: number;
    totalQuantity:       number;
}
