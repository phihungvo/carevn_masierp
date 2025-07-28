export type Liquidation = {
    id:               string;
    code:             string;
    attribute:        Attribute;
    status:           string;
    liquidationDate:  string;
    description:      string;
    reason:           string;
    isDeleted:        boolean;
    createdAt:        string;
    createdBy:        string;
    updatedAt:        string;
    updatedBy:        string;
    company:          string;
    department:       string;
    requestApprovals: RequestApproval[];
}

export type Attribute = {
    propertyList:  PropertyList[];
    humanResource: HumanResource[];
}

export type HumanResource = {
    key:            string;
    role:           string;
    position:       string;
    employeeId:     string;
    representative: string;
}

export type PropertyList = {
    id:             string;
    ttcp:           string;
    content:        string;
    codeVtcc:       string;
    quantity:       number;
    providerId:     string;
    depreciation:   number;
    originalPrice:  number;
    tkThanhLyGiam:  string;
    remainingValue: number;
}

export type RequestApproval = {
    id:           string;
    index:        number;
    documentId:   string;
    employeeId:   string;
    company:      string;
    department:   string;
    isDeleted:    boolean;
    createdBy:    string;
    createdDate:  string;
    groupRequest: string;
}
