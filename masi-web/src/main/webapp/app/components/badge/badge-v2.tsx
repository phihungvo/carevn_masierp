import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { BadgeProps, Badge as BadgeStrap } from 'reactstrap';
import './badge.scss';

export type BadgeVariant = 'outline' | 'solid';

interface IBadge extends BadgeProps {
  color?: ColorType;
  dot?: boolean;
  variant?: BadgeVariant;
}

const BadgeV2 = (props: IBadge) => {
  const { pill = true, children, color, className, variant = 'solid' } = props;

  return (
    <BadgeStrap
      {...props}
      pill={pill}
      className={`badge-v2 badge-v2-${color} badge-v2-${variant} ${
        className ? className : ''
      }`}
    >
      {children}
    </BadgeStrap>
  );
};

export default BadgeV2;
