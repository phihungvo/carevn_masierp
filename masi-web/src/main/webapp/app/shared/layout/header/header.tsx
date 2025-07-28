import './header.scss';

import { useState } from 'react';
import { Storage, Translate } from 'react-jhipster';
import LoadingBar from 'react-redux-loading-bar';
import { Collapse, Nav, Navbar, NavbarToggler } from 'reactstrap';

import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { AccountMenu } from '../menus';
import NotificationHeader from './notification';
import SearchHeader from './search-header';
import SwitchCompany from './swtich-company';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" id="navbar-head" expand="md" className="navbar-custom fixed">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="header" navbar>
            <SearchHeader />
            {/* <Home />
            {props.isAuthenticated && <EntitiesMenu />}
            {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />} */}
            {/* <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} /> */}
            <div className="header-right">
              <SwitchCompany />
              <NotificationHeader />
              <AccountMenu isAuthenticated={props.isAuthenticated} />
            </div>
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
