export type Depreciation = {
    id:                   string;
    code:                 string;
    name:                 string;
    status:               string;
    depreciationDate:     string;
    accountingDate:       string;
    employeeId:           string;
    employee:             Employee;
    typePageDepreciation: string;
    requestApprovals:     RequestApproval[];
    isDeleted:            boolean;
    createdAt:            string;
    createdBy:            string;
    updatedAt:            string;
    updatedBy:            string;
    company:              string;
    department:           string;
}

export type Employee = {
    id:                      string;
    firstName:               string;
    lastName:                string;
    fullName:                string;
    employeeCode:            string;
    workspaceName:           string;
    workspaceNormalizedName: string;
}

export type RequestApproval = {
    id:               string;
    index:            number;
    documentId:       string;
    employeeId:       string;
    employee:         Employee;
    result:           boolean;
    approvedSign:     string;
    approvedSignName: string;
    company:          string;
    department:       string;
    isDeleted:        boolean;
    createdBy:        string;
    createdDate:      string;
    updatedBy:        string;
    updatedAt:        string;
    groupRequest:     string;
}
