export type TableDeliverySchedule = {
  id:                  string;
  itemId:              string;
  itemCode:            string;
  itemName:            string;
  supplierId:          string;
  supplierCode:        string;
  supplierName:        string;
  contractCode:        string;
  contractQuantity:    number;
  received:            number;
  remain:              number;
  planningImport:      number;
  outstandingQuantity: number;
  deliveryDate:        string;
  expectedQuantity:    number;
  actualQuantity:      number;
  differenceQuantity:  number;
  createdAt:           string;
  contractId:          string;
  orderId?:            string;
}

export type DeliveryScheduleCalendar = {
  start:    Date;
  itemId:   string;
  itemCode: string;
  itemName: string;
  title:    string;
  quantity: number;
  end:      Date;
}
