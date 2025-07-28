export type Move = {
    id:                       string;
    code:                     string;
    name:                     string;
    status:                   string;
    inventoriesStorageId:     string;
    transactionTypeId:        string;
    itemCategoryId:           string;
    transferDate:             string;
    description:              string;
    fromUnit:                 string;
    fromDepartmentId:         string;
    toDepartmentId:           string;
    fromPersonId:             string;
    toPersonId:               string;
    fromAddress:              string;
    toAddress:                string;
    isDeleted:                boolean;
    createdAt:                string;
    createdBy:                string;
    company:                  string;
    assetTransferDetailsDTOS: any[];
}
