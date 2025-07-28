import './dropdown.scss';
import { ColorType } from 'app/shared/model/enumerations/color.model';
import React from 'react';
import { Dropdown as DropdownStrap, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';

interface IDropdown {
  label: React.ReactNode;
  isOpen: boolean;
  toggle: () => void;
  items: { label: React.ReactNode; onClick?: () => void; disabled?: boolean }[];
  color?: ColorType;
  outline?: boolean;
  className?: string;
}

const Dropdown = (props: IDropdown) => {
  const { label, isOpen, toggle, items, color = 'primary', outline, className } = props;

  const colorClass = `${color ? 'btn-' + color : ''} ${outline ? 'btn-outline' : ''} ${className ? className : ''}`.trim();

  return (
    <DropdownStrap isOpen={isOpen} toggle={toggle} className="dropdown-custom">
      <DropdownToggle color={color} className={colorClass}>
        {label}
      </DropdownToggle>

      <DropdownMenu>
        {items.map((item, index) => (
          <DropdownItem key={index} onClick={item.onClick} disabled={item?.disabled}>
            {item.label}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </DropdownStrap>
  );
};

export default Dropdown;
