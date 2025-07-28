export interface IAccount{
  id?: string;
  userName: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  imageUrl?: string;
  authorities?: string[];
  employeeId?: string;
  password?: string;
}

export interface Account {
  id:                   string;
  userName:             string;
  firstName:            string;
  lastName:             string;
  email:                string;
  workspace:            Workspace;
  imageUrl:             string;
  activated:            boolean;
  langKey:              string;
  createdBy:            string;
  createdDate:          Date;
  lastModifiedBy:       string;
  lastModifiedDate:     Date;
  authorities:          string[];
  isSuperAdmin:         boolean;
  company:              Company;
  companyId:            string;
  employeeId:           string;
  signatureId:          string;
  signatureContentType: null;
  signatureFileName:    string;
  superAdmin:           boolean;
  password?:             string;
}

export interface Company {
  id:             string;
  name:           string;
  description:    null;
  parentId:       null;
  normalizedName: string;
}

export interface Workspace {
  id:             string;
  name:           string;
  normalizedName: string;
}

