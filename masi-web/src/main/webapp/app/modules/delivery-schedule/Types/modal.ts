export type ScheduleModalData = {
    contractId: string,
    orderId?: string,
    contractCode: string,
    supplierContractId: string
}

export type ItemListModal = {
    isChecked?: boolean,
    name: string,
    contractQuantity: number,
    deliveryQuantity: number,
}