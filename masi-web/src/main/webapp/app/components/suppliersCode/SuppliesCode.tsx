import { useQuery } from '@tanstack/react-query';
import { BasicSelect, MapKeySelect } from 'app/shared/model/arr-obj.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';
import { Controller, Form, Path, useFormContext } from 'react-hook-form';
import { Label } from 'reactstrap';
import SelectV3 from '../select/SelectV3';

type Props<T> = {
  name?: Path<T>;
  label?: string;
  autoSetValue?: (data?: Suppliers) => void
  isShowLabel?: boolean
  placeholder?: string;
  isDisabled?: boolean;
};

const SupplierCodes = <T extends object>(props: Props<T>) => {
  const { name = 'a', label = 'Mã NCC', autoSetValue, isShowLabel = true, placeholder, isDisabled } = props;
  const { control } = useFormContext<T>();

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
        obj[item.id] = item
      })
      return {
        arr: data,
        obj,
        total: res?.data?.totalRecord,
      };
    },
  });

  const handleSelectChange = (value: any) => {
    autoSetValue?.(query?.data?.obj?.[value]);
  }

  return (
    <Controller
      control={control}
      name={name as Path<T>}
      render={({ field }) => {
        return (
          <Form className='form-group-v2'>
            {isShowLabel && label && <Label className='form-label-v2' htmlFor='supplierId-ncc'>{label}</Label>}
            <SelectV3
              id='supplierId-ncc'
              options={query?.data?.arr || []}
              onChange={(value: BasicSelect) => {
                field.onChange(value?.value);
                handleSelectChange(value?.value);
              }}
              value={field?.value && [{
                value: field?.value,
                label: query?.data?.obj?.[field?.value]?.code + ' - ' + query?.data?.obj?.[field?.value]?.name,
              }]}
              placeholder={placeholder || 'Chọn nhà cung cấp'}
              isDisabled={isDisabled}
            />
          </Form>
        )
      }}
    />
  );
};

export default SupplierCodes;

export interface Suppliers {
  id:                string;
  code:              string;
  birthday:          Date;
  name:              string;
  email:             string;
  address:           string;
  addressService:    string;
  phone:             string;
  bankInfo:          string;
  taxCode:           string;
  contact:           string;
  paymentTerm:       Date;
  paymentTermNumber: number;
  fax:               string;
  shortName:         string;
  note:              string;
  isActive:          boolean;
  createAt:          Date;
  createBy:          string;
  updateAt:          Date;
  updateBy:          Date;
  company:           string;
  supplierGroup:     SupplierGroup;
  supplierGroupId:   string;
  debtEmployees:     DebtEmployees;
}

export interface DebtEmployees {
  key5:    number;
  key102:  number;
  key1144: number;
  key9034: number;
}

export interface SupplierGroup {
}
