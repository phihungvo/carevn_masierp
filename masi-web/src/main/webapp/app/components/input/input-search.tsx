import './input.scss';
import React from 'react';
import Input from './input';
import { InputProps } from 'reactstrap';
import { ColorType } from 'app/shared/model/enumerations/color.model';

interface IInputSearch extends InputProps {
  color?: ColorType;
}

const InputSearch = (props: IInputSearch) => {
  const { className, color, ...rest } = props;

  const mergeClassName = `search ${className ? className : ''}`.trim();

  return (
    <div className={mergeClassName}>
      <Input
        {...rest}
        placeholder="Tìm kiếm"
        className="search-input"
        color={color}
      />
      <img src="content/images/vuesax/linear/search-normal.svg" alt="search" />
    </div>
  );
};

export default InputSearch;
