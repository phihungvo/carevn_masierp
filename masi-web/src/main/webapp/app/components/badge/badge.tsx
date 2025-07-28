import { BadgeProps, Badge as BadgeStrap } from 'reactstrap';
import './badge.scss';
import React from 'react';
import { ColorType } from 'app/shared/model/enumerations/color.model';

interface IBadge extends BadgeProps {
  color?: ColorType;
  dot?: boolean;
}

const Badge = (props: IBadge) => {
  const { pill = true, children, color, className, dot = true } = props;

  return (
    <BadgeStrap {...props} pill={pill} className={`badge-${color} ${className ? className : ''}`}>
      {/* {dot && <span className={`dot dot-${color}`} />} */}
      {children}
    </BadgeStrap>
  );
};

export default Badge;
