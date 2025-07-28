import useOrders from 'app/hooks/use-orders';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { IOrder } from 'app/shared/model/order.model';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

const {
    useOrdersQuery,
  } = useOrders

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Order = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const orderList: MapKeySelect = useOrdersQuery({
    page: 0,
    size: 2000000
  }, (res) => {
    let obj: Record<string, IOrder> = {}
    let arr = []
    res?.data?.data?.forEach(item => {
      let { orderCode, id, contract: { customer: { lastName, firstName } } } = item
      let label = orderCode + ' - ' + lastName + ' ' + firstName
      obj[id] = { ...item, label } as any
      arr.push({ label, value: id })
    })
    return { obj, arr, totalRecord: res?.data?.totalRecord }
  })

  return <WrapSelect {...props} label={label} name={name} data={orderList?.data} />;
};

export default Order;
