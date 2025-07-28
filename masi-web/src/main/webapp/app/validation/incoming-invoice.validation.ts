import { z } from 'zod';

export const incomingInvoiceSchema = z.object({
  invoiceNo: z.string().optional(),
  seriNumber: z.string().optional(),
  nbr: z.string().optional(),
  createAt: z.string().optional(),
  supplierId: z.string().optional(),
  address: z.string().optional(),
  createBy: z.string().optional(),
  representative: z.string().optional(),
  note: z.string().optional(),
  phone: z.string().optional(),
  taxCode: z.string().optional(),
  paymentMethod: z.string().optional(),
  status: z.string().optional(),
  info: z.string().optional(),
  warehouse: z.string().optional(),
  goods: z.array(
    z.object({
      id: z.string().optional(),
      code: z.string().optional(),
      name: z.string().optional(),
      detail: z.string().optional(),
      unit: z.string().optional(),
      quantity: z.string().optional(),
      unitPrice: z.string().optional(),
      totalAmount: z.string().optional(),
      tax: z.string().optional(),
    }),
  ).optional(),
  tmp: z.object({
    selectedGoods: z.object({
      obj: z.record(z.object({ idUom: z.string().optional() })),
      arr: z.array(z.object({ id: z.string().optional() })),
      total: z.number().optional(),
      totalPrice: z.number().optional(),
    }),
  }).optional(),
});

export type IncomingInvoiceSchema = z.infer<typeof incomingInvoiceSchema>;
