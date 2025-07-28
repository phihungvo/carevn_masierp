import { useQuery } from '@tanstack/react-query';
import useEmployee from 'app/hooks/use-employee';
import { DepreciationApi } from 'app/modules/depreciation/apis/axios';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';
import { DepriciationItem } from 'app/modules/depreciation/types/depriciation-item';

const {
    useGetEmployeesQuery
  } = useEmployee

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
  filter?: any;
  keyValidate?: string;
};

const Properties = <T = any>(props: Props<T>) => {
  const { label, name, filter, keyValidate = 'properties' } = props;

  const queries = useQuery({
    queryKey: [keyValidate],
    queryFn: DepreciationApi.liquidationDepriciationList(filter),
    placeholderData: old => old,
    select: res => {
        let arr = []
        let obj: Record<string, DepriciationItem> = {}
        res?.data?.data?.forEach((item) => {
            let tmp = {
                label: item?.code + ' - ' + item?.item?.['name'],
                value: item.id
            }
            arr.push(tmp)
            obj[item.id] = {
                ...item,
                label: tmp.label
            } as any
        })
        return { arr, obj, totalRecord: res?.data?.totalRecord }
    }
  });

  return <WrapSelect {...props} label={label} name={name} data={queries?.data} />;
};

export default Properties;
