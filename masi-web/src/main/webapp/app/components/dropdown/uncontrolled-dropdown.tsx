import './dropdown.scss';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown as UncontrolledDropdownStrap } from 'reactstrap';

interface IUncontrolledDropdownProps {
  className?: string;
  items: React.ReactNode[];
}

const UncontrolledDropdown = (props: IUncontrolledDropdownProps) => {
  const { className, items } = props;

  const classes = ['actions-dropdown', className].filter(Boolean).join(' ');

  return (
    <UncontrolledDropdownStrap className={classes} direction="down">
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" />
      </DropdownToggle>

      <DropdownMenu>
        {items.map((item, index) => (
          <DropdownItem key={index}>{item}</DropdownItem>
        ))}
      </DropdownMenu>
    </UncontrolledDropdownStrap>
  );
};

export default UncontrolledDropdown;
