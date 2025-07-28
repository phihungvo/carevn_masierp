import React, { useEffect, useState } from 'react';

import './menu-item.scss';
import { useLocation, useNavigate } from 'react-router';

export interface ISubMenuItem {
  label: string;
  to?: string;
  key?: string;
  items?: ISubMenuItem[];
  isPermitted?: boolean;
  selected?: boolean;
  setParentSelectedKey?: () => void,
  setIsCheckChildren?: React.Dispatch<React.SetStateAction<boolean>>
}

const SubMenuChildItem = (props: ISubMenuItem) => {
  const { label, to, isPermitted, selected, setIsCheckChildren, setParentSelectedKey = () => {} } = props;

  const [selectedKey, setSelectedKey] = useState<string>('');

  // const { toggleSelectedKeys } = useToggleMenu([]);
  const { pathname } = useLocation();
  const navigate = useNavigate();

  // const splitPath = to.split(':id');
  // const joinPath = splitPath.join('/');

  const toggleSelectedKeys = (key: string) => setSelectedKey(key)

  // Để render lại giao diện khi click vào item khác.
  useEffect(() => {
    if (selectedKey) setIsCheckChildren(pathname === selectedKey)
  }, [selectedKey])

  return (
    <>
      {isPermitted && (
        <div
          onClick={e => {
            e.stopPropagation();
            toggleSelectedKeys(to);
            to && navigate(to);
            setParentSelectedKey();
            const leftMenu = document.getElementById('left-menu');
            const navbar = document.getElementById('navbar-head');
            navbar.style.marginLeft = `${leftMenu.clientWidth}px`;
          }}
          className={`sub-menu-child-item ${pathname === to && selected ? 'active' : ''}`}
        >
          {label}
        </div>
      )}
    </>
  );
};

export default SubMenuChildItem;
