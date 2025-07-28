import { CUSTOMER_STATUS } from './enumerations/customer.model';
import { PaginationParams } from './pagination.model';

export interface ICustomer {
  id?: string;
  customerCode: string;
  companyName?: string;
  address: string;
  taxCode?: string;
  firstName: string;
  lastName: string;
  birthday: string;
  phoneNumber: string;
  email: string;
  position?: string;
  customerOwner: string;
  contractSigned?: string;
  contractFrom?: string;
  contractTo?: string;
  customerStatus?: CUSTOMER_STATUS;
  lastUpdated?: string;
  createdDate?: string;
  isDeleted?: boolean;
  note?: string;
  customerOwnerDTO?: ICustomerOwnerDTO
}

interface ICustomerOwnerDTO {
  id: string;
  fullName: string;
  firstName: string;
  lastName: string;
}

export interface ICustomerParams extends PaginationParams {
  search?: string;
  contractFrom?: string;
  contractTo?: string;
  customerStatus?: CUSTOMER_STATUS;
  listEmployeeOwner?: string[];
  birthdayFrom?: string;
  birthdayTo?: string;
  sort?: string[];
  customerId?: string;
}
