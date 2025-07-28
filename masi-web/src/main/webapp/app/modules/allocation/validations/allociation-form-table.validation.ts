import z from 'zod';

export const allociationFormTable = z.object({
  itemAssetDepreciationDetails: z.array(
    z.object({
      id: z.string(),
      code: z.string(),
      note: z.string(),
      costInformation: z.string(), // thông tin chi phí
      amortizedCostInformation: z.string(), // thông tin chi phí đã khấu hao
      amortizationAmount: z.number(), // số tiền khấu hao
      amortizationRate: z.number(), // tỉ lệ khấu hao
      originalCost: z.number(), // nguyên giá gốc
      accumulatedAmortizationAmount: z.number(), // số tiền khấu hao lũy kế
      recipe: z.enum(['default', 'custom']).default('default'), // cách tính
      isCustomRecipe: z.boolean().default(false).optional(), // cách tính
  
      name: z.string().optional(),
      inventoriesStorageId: z.string(), //
      attribute: z.string().optional(),
    }),
  ),
  itemAssetDepreciationDetailsTmp: z.record(z.string(), z.object({
    id: z.string(),
    code: z.string(),
    note: z.string(),
    costInformation: z.string(), // thông tin chi phí
    amortizedCostInformation: z.string(), // thông tin chi phí đã khấu hao
    amortizationAmount: z.number(), // số tiền khấu hao
    amortizationRate: z.number(), // tỉ lệ khấu hao
    originalCost: z.number(), // nguyên giá gốc
    accumulatedAmortizationAmount: z.number(), // số tiền khấu hao lũy kế
    recipe: z.enum(['default', 'custom']).default('default'), // cách tính
    isCustomRecipe: z.boolean().default(false).optional(), // cách tính

    name: z.string().optional(),
    inventoriesStorageId: z.string(), //
    attribute: z.string().optional(),
  }))
}).deepPartial()

export type AllociationTableSchemaType = z.infer<typeof allociationFormTable>

export type Allociation = AllociationTableSchemaType['itemAssetDepreciationDetails'][number]