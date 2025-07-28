import classNames from 'classnames';
import React, { ComponentProps, ReactNode, useState } from 'react';
import './SelectV2.scss';

type Props = ComponentProps<'select'> & {
  options?: { value: string; label: string }[];
  placeholder?: ReactNode;
};

const SelectV2 = (props: Props) => {
  const { className, options, placeholder, ...rest } = props;

  const [value, setValue] = useState('');

  return (
    <select className={classNames('form-select-v2', className)} {...rest}>
      {placeholder && (
        <option value="" disabled>
          {placeholder ?? ''}
        </option>
      )}
      {options?.map((x, idx) => (
        <option key={idx} value={x?.['value']}>
          {x?.['label'] ?? ''}
        </option>
      ))}
    </select>
  );
};

export default SelectV2;
