import React from 'react';
import { UncontrolledTooltip, UncontrolledTooltipProps } from 'reactstrap';

interface TooltipProps extends UncontrolledTooltipProps {
  children: React.ReactNode;
  label: string;
}

const Tooltip = (props: TooltipProps) => {
  const { children, label, target, placement = 'top-start' } = props;
  return (<>
      {children}
      <UncontrolledTooltip target={target} placement={placement}>
        {label}
      </UncontrolledTooltip>
    </>
  );
};

export default Tooltip;
