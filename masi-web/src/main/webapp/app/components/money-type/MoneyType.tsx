import { useQuery } from '@tanstack/react-query';
import { BasicSelect, MapKeySelect } from 'app/shared/model/arr-obj.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';
import { Controller, ControllerRenderProps, get, Path, useFormContext } from 'react-hook-form';
import { Form, Label } from 'reactstrap';
import FormError from '../form/form-error';
import SelectV3 from '../select/SelectV3';
import { useEffect } from 'react';

type Props<T> = {
  onSelectChange?: (value: MoneyType) => void
  name?: Path<T>
  defaultCode?: any
};

const MoneyType = <T extends object>(props: Props<T>) => {
  const { onSelectChange, name = 'currency', defaultCode } = props;
  const { control, setValue, watch } = useFormContext<T>();

  const value = watch(name as Path<T>);


  const query: MapKeySelect = useQuery({
    queryKey: ['moneyType', { page: 0, size: 20 }],
    queryFn: () =>
      axios.get<PaginationResponse<MoneyType>>(
        '/services/masilogistics/api/currencies',
        { params: { page: 0, size: 20 } },
      ),
    select: res => {
      if (!res?.data) return undefined
      const data = res?.data;
      const obj = {};
      const arr = [];
      data?.data?.forEach(item => {
        const tmp = {
          value: item.id,
          label: item.name,
          code: item.code,
        };
        arr.push(tmp);
        obj[item.id] = item;
      });
      return {
        arr,
        obj,
        totalRecord: data?.totalRecord,
      };
    },
  });


  useEffect(() => {
    if (!value) {
      const item = query.data?.arr?.find(s => s.code === defaultCode)
      if (item) {
        setValue(name as Path<T>, item?.value)
        setTimeout(() => {
          onSelectChange && onSelectChange(query?.data?.obj?.[item?.value]);
        },1)
      }
    }
  }, [query.data?.arr])

  // eslint-disable-next-line @typescript-eslint/no-shadow
  const onChange = (field: ControllerRenderProps<T, Path<T>>) => (value: BasicSelect) => {
    field.onChange(value?.value);
    onSelectChange && onSelectChange(query?.data?.obj?.[value?.value]);
  }

  return (
    <Controller
      control={control}
      name={name as Path<T>}
      render={({ field, fieldState: { error } }) => (
        <Form className="form-group-v2">
          <Label className="form-label-v2">Loại tiền</Label>
          <SelectV3
            options={query.data?.arr || []}
            onChange={onChange(field)}
            value={field?.value && [{
              value: field?.value,
              label: query?.data?.obj?.[field?.value]?.name,
            }]}
            placeholder="Chọn loại tiền"
          />
          {error && <FormError message={error?.message} />}
        </Form>
      )}
    />
  );
};

export default MoneyType;

export interface MoneyType {
  id: string;
  code: string;
  name: string;
  symbol: string;
  isActive: boolean;
  rate: number;
  attributes: string;
  isDeleted: boolean;
  createdAt: Date;
  createdBy: string;
  updatedAt: Date;
  updatedBy: string;
  company: string;
  department: string;
}
