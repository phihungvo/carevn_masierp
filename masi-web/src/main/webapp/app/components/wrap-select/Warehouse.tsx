import useWarehouse from 'app/hooks/use-warehouse';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

const { useGetWarehouses } = useWarehouse;

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  label?: string;
  name: Path<T>;
};

const WareHouse = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const warehouses = useGetWarehouses({}, res => {
    let data = res?.data;
    let converted = {
      obj: {},
      arr: [],
    };
    data?.data?.forEach((item, idx) => {
      let tmp = {
        label: item?.code + ' - ' + item?.name,
        value: item?.id as string,
      };
      converted.arr.push(tmp);
      converted.obj[item?.id] = {
        ...item,
        label: tmp.label
      }
    });
    return converted as any;
  })

  return (
    <WrapSelect
      { ...props }
      label={label}
      name={name}
      data={warehouses?.data}
      isLoading={warehouses?.['isLoading']}
    />
  );
};

export default WareHouse;
