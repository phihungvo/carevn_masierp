import React from 'react';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id} data-cy={props['data-cy']}>
    <DropdownToggle nav caret className="d-flex align-items-center account">
      <span>{props.name}</span>
      {props.img ?? <img src="https://upload.wikimedia.org/wikipedia/commons/4/40/Image_test.png" />}
    </DropdownToggle>
    <DropdownMenu end style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);
