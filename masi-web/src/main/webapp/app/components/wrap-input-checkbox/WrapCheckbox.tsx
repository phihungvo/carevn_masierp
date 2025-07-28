import FormWrap from 'app/components/formV2/form-wrap/form-wrap';
import { ComponentProps } from 'react';
import { Controller, Path, useFormContext } from 'react-hook-form';
import { FormGroup, Input, Label } from 'reactstrap';
import Flex from '../flex/flex';
import FormError from '../form/form-error';
import './index.scss'

type Props<T> = ComponentProps<typeof Input> & Pick<ComponentProps<typeof Flex>, 'direction' | 'gap' | 'justify'> & {
  label?: string | React.ReactNode;
  name: Path<T>;
  disabled?: boolean;
  isExcludeHookForm?: boolean;
};

const WrapCheckbox = <T = any>(props: Props<T>) => {
  const {
    label, name, disabled,
    isExcludeHookForm, direction, gap = 10,
    value, onChange, justify,
    ...rest
  } = props;

  const methods = useFormContext();

  return (
    <>
      {isExcludeHookForm ? (
        <FormGroup className="form-group-v2 wrap-checkbox" row={true}>
          <Flex direction={direction} align="center" gap={gap} justify={justify}>
            {label && (
              <Label className="form-label-v2" htmlFor={name}>
                {label}
              </Label>
            )}
            <Input
              id={name}
              type="checkbox"
              checked={!!value}
              onChange={onChange}
            />
          </Flex>
        </FormGroup>
      ) : (
        <Controller
          name={name}
          control={methods.control}
          render={({ field, fieldState: { error } }) => (
            <FormGroup className="form-group-v2 wrap-checkbox" row={true}>
              <Flex direction={direction} align="center" gap={gap} justify={justify}>
                {label && (
                  <Label className="form-label-v2" htmlFor={name}>
                    {label}
                  </Label>
                )}
                <Input
                  {...rest}
                  {...field}
                  id={name}
                  type="checkbox"
                  checked={field?.value}
                  disabled={disabled}
                  onChange={e => {
                    field.onChange(e.target.checked)
                    onChange && onChange(e)
                  }}
                  style={{ marginTop: '0 !important' }}
                />
              </Flex>
              {!!error && <FormError message={error?.message} />}
            </FormGroup>
          )}
        />
      )}
    </>
  );
};

export default WrapCheckbox;
