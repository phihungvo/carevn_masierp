import React, { ComponentProps } from 'react';
import {
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  UncontrolledDropdown,
} from 'reactstrap';
import ButtonV2 from './ButtonV2';

type Props = {
  items: ComponentProps<typeof DropdownItem>[];
  children: string | React.ReactNode;
};

const SplitButton = (props: Props) => {
  const { items, children } = props;

  return (
    <UncontrolledDropdown group className="btn-dropdown-v2 btn-v2__split-btn">
      <DropdownToggle
        caret
        color="primary"
        style={{ padding: '0px 12px', gap: '4px' }}
      >
        <ButtonV2
          variant="solid"
          style={{ background: 'transparent', border: 'none' }}
        >
          {children}
          <div className="btn-v2__divider" />
        </ButtonV2>
      </DropdownToggle>
      <DropdownMenu>
        {items.map((item, index) => {
          const { children, onClick, ...rest } = item;
          return (
            <DropdownItem
              key={`dropdown-item-${index}`}
              {...rest}
              onClick={e => {
                if (onClick) onClick(e);
              }}
            >
              {children}
            </DropdownItem>
          );
        })}
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default SplitButton;
