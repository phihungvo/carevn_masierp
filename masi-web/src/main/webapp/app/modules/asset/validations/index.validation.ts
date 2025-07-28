import z from 'zod';
import { ariseSchema } from './arise.validation';
import { generalInfoSchema } from './general-info.validation';
import { includeAccessoriesSchema } from './include-accessories.validation';
import { moveSchema } from './move.validation';
import { otherInformationSchema } from './other-information.validation';
import { relativedDocumentSchema } from './relatived-document.validation';

export const assetSchema = z
  .object({
    basisInformation: z.object({
      propertyCode: z.string().optional(),
      codeFormWarehouse: z.string().optional(),
      note: z.string().optional(),
      groupCode: z.string().optional(),
      smallGroup: z.string().optional(),
      reason: z.string().optional(),
      originalPrice: z.coerce.number().optional(),
      remainingValue: z.coerce.number().optional(),
      remainingQuantity: z.coerce.number().optional(),
      managementUnit: z.string().optional(),
    }),
  }).deepPartial()
  .merge(generalInfoSchema)
  .merge(includeAccessoriesSchema)
  .merge(relativedDocumentSchema)
  .merge(ariseSchema)
  .merge(moveSchema)
  .merge(otherInformationSchema)

export type AssetSchemaType = z.infer<typeof assetSchema>;

export type AssetForm = Pick<AssetSchemaType, 'basisInformation'>;