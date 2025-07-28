export interface ILiquidationPropertyDto {
  id: string;
  property?: string;
  description?: string;
  ttcp?: string;
  vtccCode?: string;
  amount?: number;
  originalPrice?: number;
  depreciation?: number;
  remainingValue?: number;
  liquidationAccount?: string;
  supplier?: string;
  note?: string;
}
