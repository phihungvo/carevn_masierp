import { PAYMENT_REQUEST_STATUS } from "../model/enumerations/payment-request";

export const isEnableApprovalOrReject = <T = any>(payload: {
    employeeId: string, requestApproval?: keyof T, statusKey?: keyof T, data: T
}) => {
    // đúng người, watiing approval
    const {
        employeeId, requestApproval = 'requestApprovals', data, statusKey = 'status'
    } = payload;
    const isOneOfApprovalPerson = (data?.[requestApproval as any] as any[] || []).some(item => item.employeeId === employeeId);
    const isWaitingApproval = [
        PAYMENT_REQUEST_STATUS.WAITING_APPROVE,
        PAYMENT_REQUEST_STATUS.WAITING_APPROVED
    ].includes(data?.[statusKey as any] as any);
    return isOneOfApprovalPerson && isWaitingApproval;
}

export const isEnableCancel = <T = any>(payload: {
    employeeId: string, createdBy?: keyof T, statusKey?: keyof T, data: T
}) => {
    // đúng người tạo, new
    const {
        employeeId, createdBy = 'createdBy', data, statusKey = 'status'
    } = payload;
    const isCreateByMySelf = employeeId === data?.[createdBy as any];
    const isNew = PAYMENT_REQUEST_STATUS.NEW === data?.[statusKey as any];
    return isCreateByMySelf && isNew;
}

export const isEnableRequestApproval = <T = any>(payload: {
    employeeId: string, createdBy?: keyof T, statusKey?: keyof T, data: T
}) => {
    // người tạo, new, reject
    const {
        employeeId, createdBy = 'createdBy', data, statusKey = 'status'
    } = payload;
    const isCreateByMySelf = employeeId === data?.[createdBy as any];
    const isNewOrReject = [
        PAYMENT_REQUEST_STATUS.NEW, PAYMENT_REQUEST_STATUS.REJECTED
    ].includes(data?.[statusKey as any] as any);
    return isCreateByMySelf && isNewOrReject;
}

export const isEnableUpdate = <T = any>(payload: {
    employeeId: string, isEditMode: boolean, createdBy?: keyof T, statusKey?: keyof T, data: T
}) => {
    // người tạo, new, reject
    const {
        employeeId, createdBy = 'createdBy', data, statusKey = 'status', isEditMode
    } = payload;
    const isCreateByMySelf = employeeId === data?.[createdBy as any];
    const isNewOrReject = [
        PAYMENT_REQUEST_STATUS.NEW, PAYMENT_REQUEST_STATUS.REJECTED
    ].includes(data?.[statusKey as any] as any);
    return !isEditMode || (isCreateByMySelf && isNewOrReject);
}