import { useQuery } from '@tanstack/react-query';
import { MapKeySelect } from 'app/shared/model/arr-obj.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';
import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import { Suppliers } from '../suppliersCode/SuppliesCode';
import WrapSelect from './WrapSelect';

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Provider = <T = any>(props: Props<T>) => {
  const { label, name } = props;

  const query: MapKeySelect = useQuery({
    queryKey: ['Suppliers_V2', { page: 0, size: 2000000 }],
    queryFn: () =>
      axios.get<PaginationResponse<Suppliers>>(
        '/services/masilogistics/api/suppliers',
        { params: { page: 0, size: 2000000 } },
      ),
    select: res => {
      let obj = {}
      let data: { label: string; value: string }[] = []
      res?.data?.data?.forEach(item => {
        data.push({
          value: item.id,
          label: item.code + ' - ' + item.name,
        })
        obj[item.id] = {
            ...item,
            label: item.code + ' - ' + item.name
        }
      })
      return {
        arr: data,
        obj,
        total: res?.data?.totalRecord,
      };
    },
  });

  return <WrapSelect {...props} label={label} name={name} data={query?.data} />;
};

export default Provider;
