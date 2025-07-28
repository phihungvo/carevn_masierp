import { InvoiceType } from "../constants/status";

export type Attachments = any;

export type File = {
  id:       string;
  fileName: string;
  createdAt?: Date;
}

export type InvoiceSupply = {
  itemId:      string;
  quantity:    number;
  price:       number;
  total:       number;
  tax:         number;
  description: string;
}

export type IncomingBody = {
  invoiceDate:      Date;
  employeeId:       string;
  departmentId:     string;
  content:          string;
  invoiceType:      string;
  attachments:      Attachments;
  files?:            File[];
  series:           string;
  supplierId:       string;
  paymentMethod:    string;
  debtDays:         number;
  currencyId:       string;
  currencyCode:     string;
  currencyRate:     number;
  patternNo:        string;
  invoiceSupplies:  InvoiceSupply[];
  requestApprovals: Attachments[];
  relatedCosts?: any[]
}

export type IncommingDetail = {
  createdBy:       string;
  createdAt:       Date;
  updatedAt:       Date;
  company:         string;
  id:              string;
  invoiceNo:       string;
  invoiceDate:     Date;
  patternNo:       string;
  orderCreatedAt?: string;
  employeeId:      string;
  content:         string;
  invoiceType:     InvoiceType;
  attachments:     any[];
  series:          string;
  supplierId:      string;
  paymentMethod:   string;
  debtDays:        number;
  currencyId:      string;
  currency:        Currency;
  currencyCode:    string;
  currencyRate:    number;
  needApproval:    boolean;
  status:          string;
  suppliers:       Suppliers;
  invoiceSupplies: InvoiceSupplies[];
  grandTotal:      number;
  relatedCosts:    RelativedFees[];
  supplierContractId: string;
  supplierContract: SupplierContract;
  inventories:    Inventories[];
  isInvoice:       boolean;
}

export type Currency = {
  id:         string;
  code:       string;
  name:       string;
  symbol:     string;
  isActive:   boolean;
  rate:       number;
  attributes: string;
  isDeleted:  boolean;
  createdAt:  Date;
  createdBy:  string;
  updatedAt:  Date;
  updatedBy:  string;
  company:    string;
  department: string;
}

export type Suppliers = {
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


export type InvoiceSupplies = {
  id:                  string;
  itemId:              string;
  item:                Item;
  invoiceId:           string;
  quantity:            number;
  price:               number;
  total:               number;
  vat:                 number;
  isDeleted:           boolean;
  createdAt:           Date;
  createdBy:           string;
  company:             string;
  department:          string;
  vatAmount:           number;
  importTaxPercentage: number;
  envFeePercentage:    number;
  importTaxAmount:     number;
  envFeeAmount:        number;
  grandTotal:          number;
  note:                string;
  vatId:               string;
  totalAmountAfterVat: number;
}

export type Item = {
  id:             string;
  code:           string;
  name:           string;
  uomId:          string;
  uom:            Uom;
  attribute:      Attribute;
  company:        string;
  isDeleted:      boolean;
  createdDate:    Date;
  itemCategory:   ItemCategory;
  itemCategoryId: string;
  itemTypeId:     string;
  percentProtein: number;
  vatRate:        number;
  vatId:          string;
  unitPrice:      number;
  supplierId:     string;
  itemType:       string;
  isActive:       boolean;
}

export type Attribute = {
  material:    { [key: string]: number };
  material_id: string;
}

export type ItemCategory = {
  id:              string;
  code:            string;
  name:            string;
  company:         string;
  isDeleted:       boolean;
  createdBy:       string;
  createdDate:     Date;
  items:           any[];
  warehouseTypeId: string;
}

export type Uom = {
  id:       string;
  name:     string;
  createAt: Date;
  createBy: string;
  company:  string;
}

export interface RelativedFees {
  id:         string;
  invoiceId:  string;
  invoice:    Invoice;
  isDeleted:  boolean;
  createdAt:  Date;
  createdBy:  string;
  company:    string;
  department: string;
}

export interface Invoice {
  createdBy:           string;
  createdAt:           Date;
  updatedAt:           Date;
  company:             string;
  id:                  string;
  invoiceNo:           string;
  invoiceDate:         Date;
  employeeId:          string;
  content:             string;
  totalAmount:         number;
  invoiceType:         string;
  attachments:         Attachment[];
  series:              string;
  supplierContractId:  string;
  supplierId:          string;
  paymentMethod:       string;
  debtDays:            number;
  currencyId:          string;
  currencyCode:        string;
  currencyRate:        number;
  totalQuantity:       number;
  importFee:           number;
  totalAmountVat:      number;
  grandTotal:          number;
  patternNo:           string;
  totalFeeAfterImport: number;
  totalAmountSupplies: number;
  totalPreImportFee:   number;
  totalImportTax:      number;
  totalEnvTax:         number;
  totalVat:            number;
  totalAmountAfterVat: number;
  inventoryIds:        any[];
  needApproval:        boolean;
  status:              string;
}

export interface Attachment {
  fileId:    string;
  fileName:  string;
  createdAt: Date;
}

export interface SupplierContract {
  createdBy:               string;
  createdAt:               Date;
  updatedAt:               Date;
  company:                 string;
  department:              string;
  id:                      string;
  contractCode:            string;
  contractName:            string;
  supplierId:              string;
  contractDate:            Date;
  startDate:               Date;
  endDate:                 Date;
  contractAmount:          number;
  paymentTermNumber:       number;
  totalAmount:             number;
  totalAmountAfterVat:     number;
  totalQuantity:           number;
  supplierFullName:        string;
  supplierPosition:        string;
  supplierPhone:           string;
  supplierEmail:           string;
  note:                    string;
  attachments:             any[];
  supplierContractDetails: any[];
  status:                  string;
}

export interface Inventories {
  id:                  string;
  code:                string;
  inventoriesTypeId:   string;
  inventoriesType:     Customer;
  dateCreate:          Date;
  customerId:          string;
  customer:            Customer;
  customerRecipient:   CustomerRecipient;
  invoiceId:           string;
  isInvoice:           boolean;
  note:                string;
  incomingWarehouseId: string;
  incomingWarehouse:   Customer;
  outgoingWarehouse:   CustomerRecipient;
  totalAmount:         number;
  employeeId:          string;
  purchaseContractId:  string;
  purchaseContract:    PurchaseContract;
  warehouseGroupType:  string;
  isReview:            boolean;
  attribute:           Attribute;
  status:              string;
  isDeleted:           boolean;
  createdAt:           Date;
  createdBy:           string;
  updatedAt:           Date;
  updatedBy:           string;
  company:             string;
  department:          string;
  totalQuantity:       number;
}

export interface Customer {
  id:   string;
  code: string;
  name: string;
}

export interface CustomerRecipient {
}

export interface PurchaseContract {
  createdAt:           Date;
  updatedAt:           Date;
  id:                  string;
  contractCode:        string;
  contractName:        string;
  totalAmount:         number;
  totalAmountAfterVat: number;
  totalQuantity:       number;
}
