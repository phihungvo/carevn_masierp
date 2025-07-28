import z from 'zod';

export const depriciationTableSchema = z.object({
  itemAssetDepreciationDetails: z.record(z.string(), z.object({
    id: z.string(),
    code: z.string(),
    note: z.string(),
    costInformation: z.string(), // thông tin chi phí
    amortizedCostInformation: z.string(), // thông tin chi phí đã khấu hao
    amortizationAmount: z.coerce.number(), // số tiền khấu hao
    amortizationRate: z.coerce.number(), // tỉ lệ khấu hao
    originalCost: z.coerce.number(), // nguyên giá gốc
    accumulatedAmortizationAmount: z.coerce.number(), // số tiền khấu hao lũy kế
    recipe: z.enum(['default', 'custom']).default('default'), // cách tính
    isCustomRecipe: z.boolean().default(false).optional(), // cách tính

    name: z.string().optional(),
    inventoriesStorageId: z.string(), //
    isCalculated: z.boolean().default(false),
    isResetCalculated: z.boolean().default(false),
    isSubtract: z.boolean().default(false),
  }).deepPartial()),
  itemAssetDepreciationDetailsTmp: z.array(
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
      isCalculated: z.boolean().default(false),
      isSubtract: z.boolean().default(false),
    }),
  ),
}).deepPartial()

export type DepreciationTableSchemaType = z.infer<typeof depriciationTableSchema>

export type DepreciationItemSchema = DepreciationTableSchemaType['itemAssetDepreciationDetails'][number]