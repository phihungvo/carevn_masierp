import { useQuery } from '@tanstack/react-query';
import { BasicSelect, MapKeySelect } from 'app/shared/model/arr-obj.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';
import { Controller, ControllerRenderProps, Path, useFormContext } from 'react-hook-form';
import { Form, Label } from 'reactstrap';
import SelectV3 from '../select/SelectV3';
import { useEffect } from 'react';
import FormError from '../form/form-error';

type Props<T> = {
  onSelectChange?: (value: PaymentMethod) => void;
  name?: Path<T>
  isShowLabel?: boolean
  label?: string
  placeholder?: string
  getItemById?: (data: PaymentMethod) => void
  showErrorMessage?: boolean
  disabled?: boolean
};

const PaymentMethod = <T extends object>(props: Props<T>) => {
  const {
    onSelectChange,
    name = 'summary.paymentMethod',
    isShowLabel,
    placeholder,
    getItemById,
    showErrorMessage,
    label = 'Phương thức thanh toán',
    disabled
  } = props;
  const { control, getValues, watch } = useFormContext<T>();
  const paymentMethodChange = watch(name as Path<T>);

  const query: MapKeySelect = useQuery({
    queryKey: ['paymentMethod', { page: 0, size: 2000000 }],
    queryFn: () =>
      axios.get<PaginationResponse<PaymentMethod>>(
        '/services/masilogistics/api/payment-methods',
        { params: { page: 0, size: 2000000 } },
      ),
    select: res => {
      if (!res?.data) return undefined
      let data = res?.data;
      let obj = {};
      let arr = [];
      data?.data?.forEach(item => {
        let tmp = {
          value: item.id,
          label: item.name,
        };
        arr.push(tmp);
        obj[item.id] = {
          ...item,
          label: tmp.label,
        };
      });
      return {
        arr,
        obj,
        totalRecord: data?.totalRecord,
      };
    },
  });

  const onChange = (field: ControllerRenderProps<T, Path<T>>) => (value: BasicSelect) => {
    field.onChange(value?.value);
    onSelectChange && onSelectChange(query?.data?.obj?.[value?.value]);
  }

  useEffect(() => {
    if (getItemById && query?.data) {
      getItemById(query?.data?.obj?.[paymentMethodChange]);
    }
  }, [paymentMethodChange])

  return (
    <Controller
      control={control}
      name={name as Path<T>}
      render={({ field, fieldState: { error } }) => {
        return (
          <Form className="form-group-v2">
          {isShowLabel && <Label className="form-label-v2">{label}</Label>}
          <SelectV3
            options={query.data?.arr || []}
            onChange={onChange(field)}
            value={field?.value && [{
              value: field?.value,
              label: query?.data?.obj?.[field?.value].label,
            }]}
            placeholder={placeholder || "Chọn phương thức thanh toán"}
            isDisabled={disabled}
          />
          {showErrorMessage && error?.message && (
            <FormError message={error?.message} />
          )}
        </Form>
        )
      }}
    />
  );
};

export default PaymentMethod;

export type PaymentMethod = {
  id: string;
  code: string;
  name: string;
  description: string;
  isActive: boolean;
  attributes: string;
  isCash: boolean;
  isDeleted: boolean;
  createdAt: Date;
  createdBy: string;
  updatedAt: Date;
  updatedBy: string;
  company: string;
  department: string;
};
