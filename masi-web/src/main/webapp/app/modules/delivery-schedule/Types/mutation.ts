export type PurchaseModePayload = {
    id: string
    deliveryDate: string
    contractMaterialId: string
    quantity: number,
    address: string
    note?: string
    supplierContractId: string
}