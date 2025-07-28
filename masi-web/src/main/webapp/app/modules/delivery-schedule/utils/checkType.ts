import { SupplierContract } from "../Types/detailt";

export const isSupplierItem = (isPurchase: boolean) => (data: any): data is SupplierContract => {
    return isPurchase
}