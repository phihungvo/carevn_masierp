import useUom from 'app/hooks/use-uom';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

const {
  useGetUoms
} = useUom

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Unit = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  const uoms = useGetUoms(
    {},
    (res) => {
      let data = res?.data;
      let converted = {
          obj: {},
          arr: []
      }
      data?.forEach((item, idx) => {
        let tmp = {
          label: item?.name,
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
  )

  return <WrapSelect { ...props } label={label} name={name} data={uoms?.data} />;
};

export default Unit;
