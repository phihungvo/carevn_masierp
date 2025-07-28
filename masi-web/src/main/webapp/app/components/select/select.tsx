import './select.scss';
import React from 'react';
import RSelect, { GroupBase, Props } from 'react-select';

// const normalizeString = (str: string) => {
//   return str
//     .toLowerCase() // Convert to lowercase
//     .normalize('NFD') // Normalize to separate base letters and accents
//     .replace(/[\u0300-\u036f]/g, ''); // Remove the accent marks
// };

const Select = <
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
>(
  props: Props<Option, IsMulti, Group>,
) => {
  const {
    isSearchable = true,
    isClearable = true,
    className,
    maxMenuHeight = 200,
    ...rest
  } = props;

  // const filterOption = (option: any, inputValue: string) => {
  //   const normalizedLabel = normalizeString(option.label);
  //   const normalizedInput = normalizeString(inputValue);

  //   return normalizedLabel.includes(normalizedInput);
  // };

  return (
    <RSelect
      {...rest}
      className={`custom-select ${className ? className : ''}`}
      classNamePrefix="r-select"
      isSearchable={isSearchable}
      isClearable={isClearable}
      maxMenuHeight={maxMenuHeight}
      menuPortalTarget={document.body}
      styles={{
        menuPortal: base => ({ ...base, zIndex: 9999 }),
        input: base => ({ ...base, flexGrow: 1 }),
      }}
      placeholder={rest.placeholder || 'Chọn'}
      // filterOption={filterOption}
    />
  );
};

export default Select;
