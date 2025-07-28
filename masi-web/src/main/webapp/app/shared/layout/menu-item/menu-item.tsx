import { Alert, Collapse, Dropdown, Nav } from 'reactstrap';
import React, { useEffect, useMemo, useState } from 'react';

import './menu-item.scss';
import SubMenuItem, { ISubMenuItem } from './sub-menu-item';


export interface IMenuItem {
  id: string;
  label: React.ReactNode;
  icon: React.ReactNode;
  items?: ISubMenuItem[];
  onClick?: () => void;
  selectedKeys?: string;
}

export const ChevronDown = ({ className, onClick }: { className?: string; onClick?: () => void }) => {
  return <img onClick={onClick} className={className} src="content/images/vuesax/bulk/chevron-down.svg" alt="down" />;
};

export const ChevronUp = ({ className, onClick }: { className?: string; onClick?: () => void }) => {
  return <img onClick={onClick} className={className} src="content/images/vuesax/bulk/chevron-up.svg" alt="up" />;
};

const MenuItem = (props: IMenuItem) => {
  const { id, label, icon, selectedKeys, items, onClick } = props;
  const [isOpen, setIsOpen] = useState(false);
  const [selectedKey, setSelectedKey] = useState<string>('');
  const selected = selectedKeys?.includes(id);
  
  const isChildOpen = (items: ISubMenuItem[]) => {
    return items.some(item =>
      item.items?.length > 0 ? isChildOpen(item.items) : item.to === window.location.pathname,
    ) || id === window.location.pathname.split('/')[1]
  }

  const childOpen = isChildOpen(items)

  const toggleSubSidebar = () => {
    if (isOpen) {
      onClick();
    } else if (!selected && !isOpen) {
      onClick();
      setIsOpen(!isOpen);
    } else {
      setIsOpen(!isOpen);
    }
  };

  return (
    <div className="menu-parent">
      {/* MENU ITEMS */}
      <Dropdown onClick={toggleSubSidebar} className={`menu-item ${selected ? 'selected' : ''} ${childOpen ? 'open' : ''}`}>
        {icon}
        {label}
      </Dropdown>
      <div className={selected && isOpen ? null : 'sub-menu-header-hidden'} id={selected && isOpen ? 'sub-sidebar' : null}>
        <div className={'sub-menu-header'}>
          <div className="sub-menu-header-tilte">{label}</div>
          <img onClick={toggleSubSidebar} className="sub-menu-header-icon" src="content/images/vuesax/bulk/x.svg" alt="close-sub-sidebar" />
        </div>
        <div className={`sub-menu ${selected && isOpen ? 'sub-menu-selected' : ''}`}>
          {items?.map(item => (
            <SubMenuItem
              key={item.key}
              id={item.key}
              to={item.to}
              label={item.label}
              items={item.items}
              isPermitted={item?.isPermitted}
              selectedKey={selectedKey}
              setParentSelectedKey={() => setIsOpen(false)}
              setSelectedKey={setSelectedKey}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default MenuItem;
