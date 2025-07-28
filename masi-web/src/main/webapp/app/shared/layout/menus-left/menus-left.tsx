import React, { useEffect, useRef, useState } from 'react';
import { Nav } from 'reactstrap';

import Flex from 'app/components/flex/flex';
import { Brand } from '../header/header-components';
import MenuItem from '../menu-item/menu-item';
import { ISubMenuItem } from '../menu-item/sub-menu-item';
import './menus-left.scss';

export type Menu = {
  key: string;
  icon: React.ReactNode;
  selectedIcon: React.ReactNode;
  label: React.ReactNode;
  items: ISubMenuItem[];
  isPermitted?: boolean;
};

interface IMenuProps {
  // defaultSelectedKeys?: string[];
  items?: Menu[];
}

const MenuLeft = (props: IMenuProps) => {
  const {
    items,
    // defaultSelectedKeys
  } = props;

  const [selectedKey, setSelectedKey] = useState<string>('');

  // const { toggleSelectedKeys, selectedKeys } = useToggleMenu(defaultSelectedKeys);

  const handleSelectedKey = (key: string) => {
    if (key === selectedKey) setSelectedKey('')
      else setSelectedKey(key)
  };

  const checkActiveMenu = data => {
    let result = null;

    const traverse = (items: ISubMenuItem[], parentKey = null) => {
      items.forEach(item => {
        if (item.to === location.pathname && parentKey) result = parentKey;
        if (item.items && !result) traverse(item.items, parentKey || item.key);
      });
    };

    traverse(data);
    return result;
  };

  useEffect(() => {
    const keyActive = checkActiveMenu(items);
    setSelectedKey(keyActive);
  }, []);

  
  const menuRef = useRef<HTMLDivElement | null>(null);

  const handleClickOutside = event => {
    if (menuRef.current && !menuRef.current.contains(event.target)) {
      setSelectedKey('')
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div ref={menuRef} id="menus-left">
      <Brand />

      <Flex direction="column" className="menus-left-container">
        {items
          ?.filter(item => item?.isPermitted)
          ?.map(item => (
            <MenuItem
              onClick={() => handleSelectedKey(item.key)}
              key={item.key}
              id={item.key}
              icon={selectedKey === item?.key ? item.selectedIcon : item.icon}
              items={item.items}
              label={item.label}
              selectedKeys={selectedKey}
            />
          ))}
      </Flex>

      </div>
  );
};

export default MenuLeft;
