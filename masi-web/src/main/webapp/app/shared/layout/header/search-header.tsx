import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './header.scss';
import { InputGroup } from 'reactstrap';
import { useMemo, useState } from 'react';

// const leftSidebarWidth = 122;

const SearchHeader = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(!!document.getElementById('sub-sidebar'))

  const toggleSidebar = () => {
    const leftMenu = document.getElementById('left-menu');
    const appView = document.getElementById('app-view-container');
    const subSidebar = document.getElementById('sub-sidebar');
    const navbar = document.getElementById('navbar-head');
    if (leftMenu.style.left === '0px' || leftMenu.style.left === '') {
      leftMenu.style.left = `-${leftMenu.clientWidth}px`;
      if (subSidebar !== null) {
        subSidebar.style.left = `-${subSidebar.clientWidth}px`;
      }
      navbar.style.marginLeft = '0px';
      appView.style.marginLeft = '0px';
    } else {
      leftMenu.style.left = '0px';
      appView.style.marginLeft = `${leftMenu.clientWidth}px`;
      navbar.style.marginLeft = `${leftMenu.clientWidth}px`;
    }
    setSidebarOpen(p => !p);
  }

  return (
    <InputGroup className="header-search">
      <button type="button" className="btn btn-icon btn-toggle-sidebar" onClick={() => toggleSidebar()}>
        <img src="../../../../content/images/vuesax/bulk/li_arrow-left-from-line.svg" className={isSidebarOpen ? `flipped-180` : ''} alt="menu-toggle"/>
      </button>
      {/* <Input placeholder="Tìm kiếm" className="header-search-input" /> */}
      {/* <img src="content/images/vuesax/linear/search-normal.svg" /> */}
    </InputGroup>
  );
};

export default SearchHeader;
