import './select.scss';
import React from 'react';
import { GroupBase } from 'react-select';

import RCreatableSelect, { CreatableProps } from 'react-select/creatable';

interface ICreatableSelect<Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>
  extends CreatableProps<Option, IsMulti, Group> {}

const CreatableSelect = <Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>(
  props: ICreatableSelect<Option, IsMulti, Group>,
) => {
  const { isSearchable = true, isClearable = true, className, ...rest } = props;

  return (
    <RCreatableSelect
      {...rest}
      className={`custom-select ${className ? className : ''}`}
      classNamePrefix="r-select"
      isSearchable={isSearchable}
      isClearable={isClearable}
    />
  );
};

export default CreatableSelect;
