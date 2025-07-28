import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import './menu-item.scss';
import Flex from 'app/components/flex/flex';
import SubMenuChildItem from './sub-menu-child-item';
import { ChevronDown, ChevronUp } from './menu-item';

export interface ISubMenuItem {
  label: string;
  to?: string;
  key?: string;
  items?: ISubMenuItem[];
  id?: string;
  isPermitted?: boolean;
  selectedKey?: string;
  setParentSelectedKey?: () => void,
  setSelectedKey?: (key: string) => void;
}

const SubMenuItem = (props: ISubMenuItem) => {
  const { label, to, items, id, isPermitted, selectedKey, setSelectedKey, setParentSelectedKey = () => {}  } = props;

  const [selected, setSelected] = useState<boolean>(false);
  const [isCheckChildren, setIsCheckChildren] = useState<boolean>(false);

  // const { toggleSelectedKeys, selectedKeys } = useToggleMenu([]);
  const navigate = useNavigate();
  const { pathname } = useLocation();

  // Nếu có phần tử con thì chọn theo selected và ngược lại.
  const isCheck = items?.length > 0 ? !!(selected && isCheckChildren) : pathname === to

  const toggleSelectedKeys = (key: string) => {
    if (selectedKey === key) setSelectedKey(null);
    else setSelectedKey(key);
  };

  // Để render lại giao diện khi click vào item khác.
  useEffect(() => {
    if (selectedKey === id) setSelected(true);
    else setSelected(false);
  }, [selectedKey])

  return (
    <>
      {isPermitted !== undefined && isPermitted && (
        <div
          className="sub-menu-item-container"
          key={id}
        >
{/* SUB-MENU ITEMS */}
<Flex justify="start" align="center">
            <div className={`sub-menu-item`}>
              <div className="label">{label}</div>
            </div>
          </Flex>
          {/* DROPDOWN SUBMENU CHILD ITEMS */}
          {items?.length > 0 ? (
            <div className={`sub-menu sub-menu-selected sub-menu-child `}>
              {items?.map(item => (
                <SubMenuChildItem
                  key={item.to}
                  to={item.to}
                  label={item.label}
                  isPermitted={item.isPermitted}
                  selected={isCheckChildren}
                  setParentSelectedKey={setParentSelectedKey}
                  setIsCheckChildren={setIsCheckChildren}
                />
              ))}
            </div>
          ) : (
            <div className={`sub-menu sub-menu-child sub-menu-selected`}>
              <SubMenuChildItem
                key={to}
                to={to}
                label={label}
                isPermitted={isPermitted}
                selected={isCheckChildren}
                setParentSelectedKey={setParentSelectedKey}
                setIsCheckChildren={setIsCheckChildren}
              />
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default SubMenuItem;
