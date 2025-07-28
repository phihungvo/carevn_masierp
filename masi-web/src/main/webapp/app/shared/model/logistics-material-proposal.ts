import { LOGISTICS_MATERIAL_RPOPOSAL_STATUS } from "./enumerations/logistics-material-proposal";
import { PaginationParams } from "./pagination.model";

export interface ILogisticsMaterialProposal {
    id?: string;
    numberVoucher: string;
    voucherDate: string;
    employeeId: string;
    workspaceId: string;
    productName: string;
    units: string;
    quantity: number;
    unitPrice: number;
    supplier: string;
    note: string;
    status: LOGISTICS_MATERIAL_RPOPOSAL_STATUS
}

export interface ILogisticsMaterialProposalParams extends PaginationParams {
    search?: string;
}

export interface IChangeItem {
    newValue: string;
    oldValue: string;
    fieldName: string;
}

export interface ILogisticsMaterialProposalChangeLog {
    id: string;
    changeDate: string;
    change: IChangeItem[];
    changeBy: string;
    employeeId: string;
}

