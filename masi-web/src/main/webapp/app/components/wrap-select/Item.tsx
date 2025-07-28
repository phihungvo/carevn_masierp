import useItems from 'app/hooks/use-items';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { IItem } from 'app/shared/model/item.model';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

const {
  useGetItemsQuery
} = useItems

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Item = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const items = useGetItemsQuery(
    {},
    {
        select: (res) => {
            let data = res?.['data'];
            let converted: MapKeySelect['data'] = {
                obj: {},
                arr: []
            }
            data?.data?.forEach((item: IItem, idx) => {
              let tmp = {
                label: item?.code + ' - ' + item?.name,
                value: item?.id,
              };
              converted.arr.push(tmp);
              converted.obj[item?.id] = {
                ...item,
                label: tmp.label
              }
            });
            return converted as any;
          }
    }
  )

  return <WrapSelect {...props} label={label} name={name} data={items?.data} />;
};

export default Item;
