import classNames from 'classnames';
import React, { ComponentProps } from 'react';
import { Spinner } from 'reactstrap';
import './ButotnV2.scss';
import { useAppSelector } from 'app/config/store';

export type BtnV2Props = ComponentProps<'button'> & {
  children?: React.ReactNode;
  left_section?: React.ReactNode;
  right_section?: React.ReactNode;
  variant?: 'solid' | 'outline' | 'fill' | 'text' | 'primary';
  color?: 'blue' | 'red' | 'gray';
  isLoading?: boolean;
  isBoxShadow?: boolean;
};

const ButtonV2 = (props: BtnV2Props) => {
  const {
    children,
    isBoxShadow = false,
    isLoading,
    type,
    left_section,
    right_section,
    disabled,
    ...rest
  } = props;

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  return (
    <button
      {...rest}
      className={classNames('btn-v2', rest.className)}
      data-variant={props.variant}
      data-color={props.color}
      data-box-shadow={isBoxShadow}
      disabled={isLoading || disabled}
      type={type || 'button'}
    >
      {left_section && (
        <div className="btn-v2__left" data-mr={!!children}>
          {isLoading ? <Spinner color="primary" /> : left_section}
        </div>
      )}
      {!left_section && isLoading && (
        <div className="btn-v2__left">
          <Spinner color="primary" size="sm" />
        </div>
      )}
      {children}
      {right_section && <div className="btn-v2__right">{right_section}</div>}
    </button>
  );
};

ButtonV2.defaultProps = {
  variant: 'outline',
  color: 'gray',
  isLoading: false,
  isBoxShadow: true,
  type: 'button',
};

export default ButtonV2;
