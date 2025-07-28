import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import SelectV3 from 'app/components/select/SelectV3';
import { BasicSelect, MapKeySelect } from 'app/shared/model/arr-obj.model';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';

type Props<T> = ComponentProps<typeof SelectV3> & {
  label?: string;
  name: Path<T>;
  data?: MapKeySelect['data'];
  onSelectChange?: (data: any) => void;
  isExcludeHookForm?: boolean;
};

const WrapSelect = <T = any,>(props: Props<T>) => {
  const { label, name, data, onSelectChange, isExcludeHookForm, value } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormWrap label={label}>
          <SelectV3
            {...props}
            onChange={(option: BasicSelect) => {
              onSelectChange && onSelectChange(data?.obj?.[option?.value]);
            }}
            options={data?.arr}
            value={value && [{
                label: data?.obj?.[value as string]?.label,
                value,
            }]}
          />
        </FormWrap>
      ) : (
        <Controller
          name={name}
          control={methods?.control}
          render={({ field, fieldState }) => (
            <FormWrap label={label} error={fieldState?.error?.message}>
              <SelectV3
                {...props}
                onChange={(option: BasicSelect) => {
                  field.onChange(option?.value);
                  onSelectChange && onSelectChange(data?.obj?.[option?.value]);
                }}
                options={data?.arr}
                value={
                  field?.value && [
                    {
                      label: data?.obj?.[field?.value]?.label,
                      value: field?.value,
                    },
                  ]
                }
              />
            </FormWrap>
          )}
        />
      )}
    </>
  );
};

export default WrapSelect;
