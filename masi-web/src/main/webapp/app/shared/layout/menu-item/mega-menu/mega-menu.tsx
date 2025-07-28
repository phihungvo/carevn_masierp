import React, { ReactNode, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { ISubMenuItem } from '../sub-menu-child-item';
import './mega-menu.scss';

interface IMageMenuProps {
  label: ReactNode;
  items?: ISubMenuItem[];
  setSelectedKey: () => void;
}
export const MegaMenu = (props: IMageMenuProps) => {
  const { label, items, setSelectedKey } = props;
  const navigate = useNavigate();

  const handleDirect = (link: string) => {
    if (link) {
      setSelectedKey();
      navigate(link);
    }
  };

  return (
    <div className="mega_menu_container">
      <div className="mega_menu_header">
        <span className="mega_menu_title">{label}</span>
      </div>
      <div className="mega_menu_list">
        {items.map(x => (
          <div className="mega_menu_list_item" key={x.key}>
            <span
              key={x.key}
              className="mega_menu_item_title"
              style={{ cursor: x?.items?.length ? 'context-menu' : 'pointer' }}
              onClick={e => {
                if (!x?.items) handleDirect(x.to);
              }}
            >
              {x.label}
            </span>
            {x?.items?.length && (
              <div className="mega_menu_item_list">
                {x.items?.map(xx => (
                  <span
                    key={xx.key}
                    className={`mega_menu_item_list_second ${xx.to === location.pathname ? 'selected' : ''}`}
                    onClick={e => handleDirect(xx.to)}
                  >
                    {xx.label}
                  </span>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
