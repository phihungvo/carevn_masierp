export interface DepreciationAPIItem {
    id:                           string;
    code:                         string;
    name:                         string;
    status:                       string;
    depreciationDate:             string;
    accountingDate:               string;
    employeeId:                   string;
    employee:                     Employee;
    description:                  string;
    typePageDepreciation:         string;
    itemAssetDepreciationDetails: ItemAssetDepreciationDetail[];
    requestApprovals:             RequestApproval[];
    createdBy:                    string;
    createdAt:                    string;
}

export type RequestApproval = {
    id:           string;
    index:        number;
    documentId:   string;
    employeeId:   string;
    employee:     Employee;
    company:      string;
    department:   string;
    isDeleted:    boolean;
    createdBy:    string;
    createdDate:  string;
    groupRequest: string;
}

export interface Employee {
    id:                      string;
    firstName:               string;
    lastName:                string;
    fullName:                string;
    employeeCode:            string;
    workspaceName:           string;
    workspaceNormalizedName: string;
}

export interface ItemAssetDepreciationDetail {
    id:                            string;
    inventoriesStorageId:          string;
    note:                          string;
    costInformation:               string;
    amortizedCostInformation:      string;
    amortizationAmount:            number;
    amortizationRate:              number;
    accumulatedAmortizationAmount: number;
    recipe:                        Recipe;
    itemAssetDepreciationId:       string;
}

export enum Recipe {
    Default = "DEFAULT",
}
