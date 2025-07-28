import useEmployee from 'app/hooks/use-employee';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

const {
    useGetEmployeesQuery
  } = useEmployee

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const PersonSelect = <T = any>(props: Props<T>) => {
  const { label, name } = props;

  const employees = useGetEmployeesQuery({}, res => {
    let data = res?.data;
    let converted: MapKeySelect['data'] = {
        obj: {},
        arr: []
    }
    data?.data?.forEach((item, idx) => {
      let tmp = {
        label: item?.code + ' - ' + item?.lastName + ' ' + item?.firstName,
        value: item?.id as string,
      };
      converted.arr.push(tmp);
      converted.obj[item?.id] = {
        ...item,
        label: tmp.label
      };
    });
    return converted as any;
  })

  return <WrapSelect {...props} label={label} name={name} data={employees?.data} />;
};

export default PersonSelect;
