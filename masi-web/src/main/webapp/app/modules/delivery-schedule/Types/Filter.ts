import { DateObject } from "react-multi-date-picker";

export type Filter = {
  'date': DateObject[],
  'deliveryDate': string,
  'expectedReceiveDate': string,
}
