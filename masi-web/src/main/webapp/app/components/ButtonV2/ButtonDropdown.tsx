import React, { ComponentProps, useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
} from 'reactstrap';
import { Direction } from 'reactstrap/types/lib/Dropdown';

type Props = {
  items: ComponentProps<typeof DropdownItem>[];
  children: string | React.ReactNode;
  direction?: Direction;
  dropdown_menu_props?: ComponentProps<typeof DropdownMenu>;
  isClose?: boolean;
};

const ButtonDropDown = (props: Props) => {
  const {
    items,
    direction,
    children,
    dropdown_menu_props,
    isClose = false,
  } = props;

  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggle = () => setDropdownOpen(prevState => !prevState);

  useEffect(() => {
    if (isClose) setDropdownOpen(!isClose);
  }, [isClose]);

  return (
    <Dropdown
      className="btn-dropdown-v2 btn-v2__dropdown-btn"
      isOpen={dropdownOpen}
      toggle={toggle}
      direction={direction}
    >
      <DropdownToggle className="dropdown-btn">{children}</DropdownToggle>
      {createPortal(
        <DropdownMenu
          className="dropdown-item__portal"
          {...dropdown_menu_props}
        >
          {items.map((item, index) => {
            const { children, ...rest } = item;
            return (
              <DropdownItem key={`dropdown-item-${index}`} {...rest}>
                {children}
              </DropdownItem>
            );
          })}
        </DropdownMenu>,
        document.body,
      )}
    </Dropdown>
  );
};

export default ButtonDropDown;
