import { z } from 'zod';

export const inventoryStorageSchema = z.any()

export type InventoryStorageSchema = z.infer<typeof inventoryStorageSchema>;
